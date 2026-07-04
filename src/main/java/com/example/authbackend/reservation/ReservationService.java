package com.example.authbackend.reservation;

import com.example.authbackend.activity.*;
import com.example.authbackend.exception.BadRequestException;
import com.example.authbackend.exception.ResourceNotFoundException;
import com.example.authbackend.rating.Rating;
import com.example.authbackend.user.User;
import com.example.authbackend.user.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import com.example.authbackend.rating.RatingRepository;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.format.DateTimeParseException;
import java.util.List;

@Service
public class ReservationService {

        private final ReservationRepository reservationRepository;
        private final ActivityRepository activityRepository;
        private final ActivityAvailabilityRepository availabilityRepository;
        private final UserRepository userRepository;
        private final RatingRepository ratingRepository;

        public ReservationService(ReservationRepository reservationRepository,
                              ActivityRepository activityRepository,
                              ActivityAvailabilityRepository availabilityRepository,
                              UserRepository userRepository, RatingRepository ratingRepository) {
                this.reservationRepository = reservationRepository;
                this.activityRepository = activityRepository;
                this.availabilityRepository = availabilityRepository;
                this.userRepository = userRepository;
                this.ratingRepository = ratingRepository;
        }

        private User getAuthenticatedUser() {
                SecurityContext securityContext = SecurityContextHolder.getContext();
                Authentication authentication = securityContext.getAuthentication();

                if (authentication == null || !authentication.isAuthenticated()) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Usuario no autenticado");
        }

        // Intenta extraer email directamente si es CustomUserDetails
        if (authentication.getPrincipal() instanceof com.example.authbackend.security.user.CustomUserDetails customUserDetails) {
            String email = customUserDetails.getEmail();
            return userRepository.findByEmail(email)
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Usuario no autenticado"));
        }

        // Fallback: authentication.getName() devuelve username o email del JWT
        String principal = authentication.getName();

        // Intenta buscar por email primero (si contiene @)
        if (principal != null && principal.contains("@")) {
            return userRepository.findByEmail(principal)
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Usuario no autenticado"));
        }

        // Si no contiene @, busca por username
        return userRepository.findByUsername(principal)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Usuario no autenticado"));
    }

    /**
     * Obtiene el primer usuario registrado (fallback)
     */
        private User getDefaultUser() {
                return userRepository.findAll()
                        .stream()
                        .findFirst()
                        .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "No hay usuarios registrados en el sistema"));
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

    @Transactional
    public void checkInReservation(Long id, String qrCode) {
        Reservation r = reservationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Reserva no encontrada"));

        User user = getAuthenticatedUser();
        if (!r.getUser().getId().equals(user.getId())) {
            throw new ResponseStatusException(
                    HttpStatus.FORBIDDEN,
                    "No tienes permiso para completar el check-in de esta reserva"
            );
        }

        if (r.getStatus() == ReservationStatus.CANCELLED || r.getStatus() == ReservationStatus.FINISHED) {
            throw new BadRequestException("Código QR inválido o expirado");
        }

        CheckInQrPayload payload = parseQrCode(qrCode);

        String storedGuideName = r.getActivity().getGuideName();
        String qrGuideName = payload.getGuideName();
        boolean guideMatches = (storedGuideName == null || storedGuideName.isBlank())
                ? (qrGuideName == null || qrGuideName.isBlank())
                : storedGuideName.equals(qrGuideName == null ? null : qrGuideName.trim());

        if (payload == null
                || payload.getAction() == null
                || !payload.getAction().equals("check-in")
                || payload.getActivityId() == null
                || !payload.getActivityId().equals(r.getActivity().getId())
                || !guideMatches) {
            throw new BadRequestException("Código QR inválido o expirado");
        }

        r.setStatus(ReservationStatus.FINISHED);
        r.setCheckInAt(parseTimestamp(payload.getTimestamp()));

        reservationRepository.save(r);
    }

    private CheckInQrPayload parseQrCode(String qrCode) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            return mapper.readValue(qrCode, CheckInQrPayload.class);
        } catch (Exception ex) {
            throw new BadRequestException("Código QR inválido o expirado");
        }
    }

    private LocalDateTime parseTimestamp(String timestamp) {
        if (timestamp == null || timestamp.isBlank()) {
            throw new BadRequestException("Código QR inválido o expirado");
        }

        try {
            return OffsetDateTime.parse(timestamp).toLocalDateTime();
        } catch (DateTimeParseException ex) {
            throw new BadRequestException("Código QR inválido o expirado");
        }
    }

    private static class CheckInQrPayload {
        private String action;
        private Long activityId;
        private String guideName;
        private String timestamp;

        public String getAction() {
            return action;
        }

        public void setAction(String action) {
            this.action = action;
        }

        public Long getActivityId() {
            return activityId;
        }

        public void setActivityId(Long activityId) {
            this.activityId = activityId;
        }

        public String getGuideName() {
            return guideName;
        }

        public void setGuideName(String guideName) {
            this.guideName = guideName;
        }

        public String getTimestamp() {
            return timestamp;
        }

        public void setTimestamp(String timestamp) {
            this.timestamp = timestamp;
        }
    }

    public List<ReservationResponse> getMyReservations() {
        User user = getAuthenticatedUser();
        return reservationRepository.findByUserIdOrderByCreatedAtDesc(user.getId())
                .stream()
                .map(this::map)
                .toList();
    }

    public ReservationResponse getReservationById(Long id) {
        Reservation r = reservationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Reserva no encontrada"));

        User user = getAuthenticatedUser();
        if (!r.getUser().getId().equals(user.getId())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "No tienes permiso para ver esta reserva");
        }

        return map(r);
    }

    @Transactional
    public ReservationResponse rescheduleReservation(Long id, RescheduleRequest request) {
        Reservation r = reservationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Reserva no encontrada"));

        User user = getAuthenticatedUser();

        if (!r.getUser().getId().equals(user.getId())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "No tienes permiso para reprogramar esta reserva");
        }

        if (r.getStatus() == ReservationStatus.CANCELLED || r.getStatus() == ReservationStatus.FINISHED) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "No se puede reprogramar esta reserva");
        }

        ActivityAvailability newAvailability = availabilityRepository
                .findByActivityIdAndDateAndTime(
                        r.getActivity().getId(),
                        request.getDate(),
                        request.getTime()
                )
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "El nuevo horario no está disponible"));

        if (newAvailability.getAvailableSlots() < request.getParticipants()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "No hay suficientes cupos disponibles");
        }

        ActivityAvailability oldAvailability = r.getAvailability();
        oldAvailability.setReservedSlots(Math.max(oldAvailability.getReservedSlots() - r.getParticipants(), 0));
        availabilityRepository.save(oldAvailability);

        newAvailability.setReservedSlots(newAvailability.getReservedSlots() + request.getParticipants());
        availabilityRepository.save(newAvailability);

        r.setAvailability(newAvailability);
        r.setParticipants(request.getParticipants());

        Reservation saved = reservationRepository.save(r);

        return map(saved);
    }

    private ReservationResponse map(Reservation r) {
        User user = r.getUser();

        Rating rating = ratingRepository
                .findByUserIdAndActivityId(user.getId(), r.getActivity().getId())
                .orElse(null);

        Integer activityScore = null;
        Integer guideScore = null;
        String ratingComment = null;

        if (rating != null) {
            activityScore = rating.getActivityScore();
            guideScore = rating.getGuideScore();
            ratingComment = rating.getComment();
        }

        return ReservationResponse.builder()
            .id(r.getId())
            .activityName(r.getActivity().getName())
            .destination(r.getActivity().getDestination())
            .imageUrl(r.getActivity().getImageUrl())
            .date(r.getAvailability().getDate())
            .time(r.getAvailability().getTime())
            .participants(r.getParticipants())
            .status(r.getStatus())
            .guideName(r.getActivity().getGuideName())
            .cancellationPolicy("Cancelación disponible hasta 24 hs antes")
            .createdAt(r.getCreatedAt())
            .totalPrice(r.getTotalPrice())
            .activityId(r.getActivity().getId())
            .activityScore(activityScore)
            .guideScore(guideScore)
            .ratingComment(ratingComment)
            .build();
    }
}

