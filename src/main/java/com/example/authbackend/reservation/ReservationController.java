package com.example.authbackend.reservation;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.authbackend.reservation.ReservationService.RescheduleReservationRequest;

import java.util.List;

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

    @GetMapping("/me")
    public List<ReservationResponse> myReservations() {
        return service.getMyReservations();
    }

    @PutMapping("/{id}/reschedule")
    public ResponseEntity<ReservationResponse> reschedule(
            @PathVariable Long id, 
            @RequestBody RescheduleReservationRequest request) {
        return ResponseEntity.ok(service.rescheduleReservation(id, request));
    }
}