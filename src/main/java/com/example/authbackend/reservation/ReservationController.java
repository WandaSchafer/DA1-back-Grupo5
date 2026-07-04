package com.example.authbackend.reservation;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;


@RestController
@RequestMapping("/api/v1/reservations")
@CrossOrigin
public class ReservationController {

    private final ReservationService service;
    private final ReservationRepository reservationRepository;

    public ReservationController(ReservationService service, ReservationRepository reservationRepository) {
        this.service = service;
        this.reservationRepository = reservationRepository;
    }

    @PostMapping
    public ReservationResponse create(@RequestBody CreateReservationRequest request) {
        return service.createReservation(request);
    }

    @PatchMapping("/{id}/cancel")
    public ReservationResponse cancel(@PathVariable Long id) {
        return service.cancelReservation(id);
    }

    @GetMapping("/me")
    public List<ReservationResponse> myReservations() {
        return service.getMyReservations();
    }

    @PutMapping("/{id}/reschedule")
    public ReservationResponse reschedule(@PathVariable Long id, @RequestBody RescheduleRequest request) {
        return service.rescheduleReservation(id, request);
    }

    @GetMapping("/updates")
    public ResponseEntity<?> checkUpdates(@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime lastCheck) {
        long timeout = 25000;
        long startTime = System.currentTimeMillis();

        while (System.currentTimeMillis() - startTime < timeout) {
            List<Reservation> changes = reservationRepository.findRecentUpdates(lastCheck);

            if (!changes.isEmpty()) {
                List<ReservationUpdateResponse> response = changes.stream().map(r -> 
                    new ReservationUpdateResponse(
                        r.getId(),
                        r.getActivity().getName(),
                        r.getStatus().name(),
                        "Estado actualizado a: " + r.getStatus(),
                        r.getUpdatedAt()
                    )
                ).toList();
                
                return ResponseEntity.ok(response);
            }

            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
        return ResponseEntity.noContent().build();
    }
    
}