package com.example.authbackend.reservation;

import com.example.authbackend.activity.*;
import com.example.authbackend.user.User;
import com.example.authbackend.user.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReservationService {

    private final ReservationRepository reservationRepository;
    private final ActivityRepository activityRepository;
    private final ActivityAvailabilityRepository availabilityRepository;
    private final UserRepository userRepository;

    public ReservationService(ReservationRepository reservationRepository,
                              ActivityRepository activityRepository,
                              ActivityAvailabilityRepository availabilityRepository,
                              UserRepository userRepository) {
        this.reservationRepository = reservationRepository;
        this.activityRepository = activityRepository;
        this.availabilityRepository = availabilityRepository;
        this.userRepository = userRepository;
    }

    private User getDefaultUser() {
        return userRepository.findAll()
                .stream()
                .findFirst()
                .orElseThrow(() -> new RuntimeException("No hay usuarios cargados"));
    }

    @Transactional
    public ReservationResponse createReservation(CreateReservationRequest request) {

        User user = getDefaultUser();

        Activity activity = activityRepository.findById(request.getActivityId())
                .orElseThrow(() -> new RuntimeException("Actividad no encontrada"));

        ActivityAvailability availability = availabilityRepository
                .findByActivityIdAndDateAndTime(
                        request.getActivityId(),
                        request.getDate(),
                        request.getTime()
                )
                .orElseThrow(() -> new RuntimeException("Horario no disponible"));

        if (availability.getAvailableSlots() < request.getParticipants()) {
            throw new RuntimeException("No hay cupos");
        }

        availability.setReservedSlots(
                availability.getReservedSlots() + request.getParticipants()
        );
        availabilityRepository.save(availability);

        Reservation r = new Reservation();
        r.setUser(user);
        r.setActivity(activity);
        r.setAvailability(availability);
        r.setParticipants(request.getParticipants());
        r.setStatus(ReservationStatus.CONFIRMED);

        Reservation saved = reservationRepository.save(r);

        return map(saved);
    }

    @Transactional
    public ReservationResponse cancelReservation(Long id) {

        Reservation r = reservationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("No encontrada"));

        r.setStatus(ReservationStatus.CANCELLED);

        ActivityAvailability availability = r.getAvailability();
        availability.setReservedSlots(
                availability.getReservedSlots() - r.getParticipants()
        );
        availabilityRepository.save(availability);

        return map(reservationRepository.save(r));
    }

    public List<ReservationResponse> getMyReservations() {

        User user = getDefaultUser();

        return reservationRepository.findByUserIdOrderByCreatedAtDesc(user.getId())
                .stream()
                .map(this::map)
                .toList();
    }

    private ReservationResponse map(Reservation r) {
        return new ReservationResponse(
                r.getId(),
                r.getActivity().getName(),
                r.getAvailability().getDate(),
                r.getAvailability().getTime(),
                r.getParticipants(),
                r.getStatus(),
                "Cancelación hasta 24h antes",
                r.getCreatedAt()
        );
    }
}
