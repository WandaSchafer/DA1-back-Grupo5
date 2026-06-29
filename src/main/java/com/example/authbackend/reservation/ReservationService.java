package com.example.authbackend.reservation;

import com.example.authbackend.activity.Activity;
import com.example.authbackend.activity.ActivityAvailability;
import com.example.authbackend.activity.ActivityAvailabilityRepository;
import com.example.authbackend.exception.BadRequestException;
import com.example.authbackend.exception.ForbiddenException;
import com.example.authbackend.exception.ResourceNotFoundException;
import com.example.authbackend.rating.RatingRepository;
import com.example.authbackend.transaction.Transaction;
import com.example.authbackend.transaction.TransactionStatus;
import com.example.authbackend.user.User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ReservationService {

    private final ReservationRepository reservationRepository;
    private final ActivityAvailabilityRepository activityAvailabilityRepository;
    private final RatingRepository ratingRepository;

    public ReservationService(ReservationRepository reservationRepository,
                              ActivityAvailabilityRepository activityAvailabilityRepository,
                              RatingRepository ratingRepository) {
        this.reservationRepository = reservationRepository;
        this.activityAvailabilityRepository = activityAvailabilityRepository;
        this.ratingRepository = ratingRepository;
    }

    @Transactional
    public ReservationResponse createReservation(CreateReservationRequest request, User user) {
        if (request.getParticipants() == null || request.getParticipants() <= 0) {
            throw new BadRequestException("Participants count must be at least 1");
        }

        if (request.getDate() == null || request.getTime() == null) {
            throw new BadRequestException("Date and time are required to reserve an activity");
        }

        ActivityAvailability availability = activityAvailabilityRepository
                .findByActivityIdAndDateAndTime(request.getActivityId(), request.getDate(), request.getTime())
                .orElseThrow(() -> new ResourceNotFoundException("Activity availability not found for the requested date/time"));

        if (availability.getAvailableSlots() <= 0 || availability.getAvailableSlots() < request.getParticipants()) {
            throw new BadRequestException("No available slots for this activity");
        }

        availability.setReservedSlots(availability.getReservedSlots() + request.getParticipants());
        activityAvailabilityRepository.save(availability);

        Activity activity = availability.getActivity();

        Reservation reservation = new Reservation();
        reservation.setUser(user);
        reservation.setActivityAvailability(availability);
        reservation.setParticipants(request.getParticipants());
        reservation.setReservationDate(LocalDateTime.now());
        reservation.setStatus(ReservationStatus.CONFIRMED);
        reservation.setTotalPrice(java.math.BigDecimal.valueOf(activity.getPrice())
                .multiply(java.math.BigDecimal.valueOf(request.getParticipants())));

        Transaction transaction = new Transaction(
                reservation.getTotalPrice().doubleValue(),
                LocalDateTime.now(),
                activity.getPrice() == 0.0 ? "FREE" : "XXXX-XXXX-XXXX-1234",
                TransactionStatus.APPROVED,
                user
        );
        reservation.setTransaction(transaction);

        Reservation savedReservation = reservationRepository.save(reservation);

        return new ReservationResponse(savedReservation);
    }
    
    @Transactional
    public void cancelReservation(Long reservationId, User user) {
        Reservation reservation = reservationRepository.findById(reservationId)
                .orElseThrow(() -> new ResourceNotFoundException("Reservation not found"));

        if (!reservation.getUser().getId().equals(user.getId())) {
            throw new ForbiddenException("Cannot cancel another user's reservation");
        }

        if (reservation.getStatus() == ReservationStatus.CANCELLED) {
            return;
        }

        ActivityAvailability availability = reservation.getActivityAvailability();
        if (availability != null) {
            int reservedSlots = Optional.ofNullable(availability.getReservedSlots()).orElse(0);
            availability.setReservedSlots(Math.max(0, reservedSlots - Optional.ofNullable(reservation.getParticipants()).orElse(0)));
            activityAvailabilityRepository.save(availability);
        }

        reservation.setStatus(ReservationStatus.CANCELLED);
        reservationRepository.save(reservation);
    }

    public List<ReservationResponse> getReservationsForUser(User user) {
        List<Reservation> reservations = reservationRepository.findByUser(user);
        return reservations.stream()
                .map(reservation -> {
                    ReservationResponse response = new ReservationResponse(reservation);
                    ratingRepository.findByUserIdAndActivityId(user.getId(), response.getActivityId())
                            .ifPresent(response::addRatingDetails);
                    return response;
                })
                .collect(Collectors.toList());
    }
}