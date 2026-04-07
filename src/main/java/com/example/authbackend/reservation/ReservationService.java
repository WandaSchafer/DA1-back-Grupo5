package com.example.authbackend.reservation;

import com.example.authbackend.activity.*;
import com.example.authbackend.user.User;
import com.example.authbackend.user.UserRepository;
import jakarta.transaction.Transactional;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

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

    /**
     * Obtiene el usuario autenticado desde SecurityContext
     */
    private User getAuthenticatedUser() {
        SecurityContext securityContext = SecurityContextHolder.getContext();
        Authentication authentication = securityContext.getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()) {
            // Fallback a getDefaultUser() para compatibilidad
            return getDefaultUser();
        }

        String email = authentication.getName();
        return userRepository.findByEmail(email)
                .orElse(getDefaultUser());
    }

    /**
     * Obtiene el primer usuario registrado (fallback)
     */
    private User getDefaultUser() {
        return userRepository.findAll()
                .stream()
                .findFirst()
                .orElseThrow(() -> new RuntimeException("No hay usuarios cargados"));
    }

    @Transactional
    public ReservationResponse createReservation(CreateReservationRequest request) {

        User user = getAuthenticatedUser();

        Activity activity = activityRepository.findById(request.getActivityId())
                .orElseThrow(() -> new RuntimeException("Actividad no encontrada"));

        ActivityAvailability availability = availabilityRepository
                .findByActivityIdAndDateAndTime(
                        request.getActivityId(),
                        request.getDate(),
                        request.getTime()
                )
                .orElseThrow(() -> new RuntimeException("Horario no disponible"));
        
        if (reservationRepository.existsByUserIdAndActivityIdAndAvailability_DateAndAvailability_TimeAndStatus(
                user.getId(),
                request.getActivityId(),
                request.getDate(),
                request.getTime(),
                ReservationStatus.CONFIRMED
        )) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Ya tenés una reserva activa para esta actividad en ese horario"
            );
        }

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
                .orElseThrow(() -> new RuntimeException("Reserva no encontrada"));

        // Verificar que el usuario autenticado sea dueño de la reserva
        User user = getAuthenticatedUser();
        if (!r.getUser().getId().equals(user.getId())) {
            throw new ResponseStatusException(
                    HttpStatus.FORBIDDEN,
                    "No tienes permiso para cancelar esta reserva"
            );
        }

        if (r.getStatus() == ReservationStatus.CANCELLED) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "La reserva ya fue cancelada"
            );
        }

        if (r.getStatus() == ReservationStatus.FINISHED) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "No se puede cancelar una reserva finalizada"
            );
        }

        ActivityAvailability availability = r.getAvailability();

        int newReservedSlots = availability.getReservedSlots() - r.getParticipants();
        availability.setReservedSlots(Math.max(newReservedSlots, 0));
        availabilityRepository.save(availability);

        r.setStatus(ReservationStatus.CANCELLED);

        Reservation saved = reservationRepository.save(r);

        return map(saved);
    }

    public List<ReservationResponse> getMyReservations() {

        User user = getAuthenticatedUser();

        return reservationRepository.findByUserIdOrderByCreatedAtDesc(user.getId())
                .stream()
                .map(this::map)
                .toList();
    }

    private ReservationResponse map(Reservation r) {
        return new ReservationResponse(
                r.getId(),
                r.getActivity().getName(),
                r.getActivity().getDestination(),
                r.getActivity().getImageUrl(),
                r.getAvailability().getDate(),
                r.getAvailability().getTime(),
                r.getParticipants(),
                r.getStatus(),
                "Cancelación hasta 24h antes",
                r.getCreatedAt(),
                r.getTotalPrice()
        );
    }
}
