package com.example.authbackend.reservation;

import com.example.authbackend.exception.BadRequestException;
import com.example.authbackend.exception.ResourceNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/reservations")
@CrossOrigin
public class ReservationController {

    private final ReservationService service;

    public ReservationController(ReservationService service) {
        this.service = service;
    }

    @PostMapping
    public ReservationResponse create(@RequestBody CreateReservationRequest request) {
        return service.createReservation(request);
    }

    @PatchMapping("/{id}/cancel")
    public ReservationResponse cancel(@PathVariable Long id) {
        return service.cancelReservation(id);
    }

    @PostMapping("/{id}/check-in")
    public ResponseEntity<Map<String, String>> checkIn(@PathVariable Long id,
                                                       @RequestParam String qrCode) {
        try {
            service.checkInReservation(id, qrCode);
            return ResponseEntity.ok(Map.of("message", "¡Asistencia Confirmada!"));
        } catch (BadRequestException ex) {
            return ResponseEntity.badRequest().body(Map.of("message", ex.getMessage()));
        } catch (ResourceNotFoundException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("message", ex.getMessage()));
        }
    }

    @GetMapping("/me")
    public List<ReservationResponse> myReservations() {
        return service.getMyReservations();
    }

    @GetMapping("/{id}")
    public ReservationResponse getReservation(@PathVariable Long id) {
        return service.getReservationById(id);
    }

    @PutMapping("/{id}/reschedule")
    public ReservationResponse reschedule(@PathVariable Long id, @RequestBody RescheduleRequest request) {
        return service.rescheduleReservation(id, request);
    }
}