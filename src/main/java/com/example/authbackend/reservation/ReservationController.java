package com.example.authbackend.reservation;

import com.example.authbackend.security.user.CustomUserDetails;
import com.example.authbackend.user.User;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

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
    public ReservationResponse create(@RequestBody CreateReservationRequest request,
                                      @AuthenticationPrincipal CustomUserDetails userDetails) {
        return service.createReservation(request, userDetails.getUser());
    }

    @PatchMapping("/{id}/cancel")
    public ResponseEntity<Void> cancel(@PathVariable Long id,
                                       @AuthenticationPrincipal CustomUserDetails userDetails) {
        service.cancelReservation(id, userDetails.getUser());
        return ResponseEntity.ok().build();
    }

    @GetMapping("/me")
    public List<ReservationResponse> myReservations(@AuthenticationPrincipal CustomUserDetails userDetails) {
        return service.getReservationsForUser(userDetails.getUser());
    }
}