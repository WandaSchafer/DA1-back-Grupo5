package com.example.authbackend.history;

import com.example.authbackend.reservation.Reservation;
import com.example.authbackend.reservation.ReservationRepository;
import com.example.authbackend.reservation.ReservationStatus;
import com.example.authbackend.user.User;
import com.example.authbackend.user.UserRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class ActivityHistoryService {

    private final ReservationRepository reservationRepository;
    private final UserRepository userRepository;

    public ActivityHistoryService(ReservationRepository reservationRepository,
                                  UserRepository userRepository) {
        this.reservationRepository = reservationRepository;
        this.userRepository = userRepository;
    }

    private User getDefaultUser() {
        return userRepository.findAll()
                .stream()
                .findFirst()
                .orElseThrow(() -> new RuntimeException("No hay usuarios cargados"));
    }

    public List<ActivityHistoryItemResponse> getHistory(LocalDate fromDate,
                                                        LocalDate toDate,
                                                        String destination) {

        updateFinishedReservations();

        User user = getDefaultUser();

        String normalizedDestination = (destination == null || destination.isBlank())
                ? null
                : destination.trim();

        return reservationRepository
                .findFinishedReservationsByFilters(user.getId(), fromDate, toDate, normalizedDestination)
                .stream()
                .map(this::mapToHistoryItem)
                .toList();
    }

    private ActivityHistoryItemResponse mapToHistoryItem(Reservation r) {
        return new ActivityHistoryItemResponse(
                r.getId(),
                r.getActivity().getId(),
                r.getAvailability().getDate(),
                r.getActivity().getName(),
                r.getActivity().getDestination(),
                r.getActivity().getGuideName(),
                r.getActivity().getDuration()
        );
    }

    private void updateFinishedReservations() {
        List<Reservation> reservations = reservationRepository.findAll();

        for (Reservation r : reservations) {
            if (r.getStatus() == ReservationStatus.CONFIRMED) {
                LocalDateTime activityDateTime = LocalDateTime.of(
                        r.getAvailability().getDate(),
                        r.getAvailability().getTime()
                );

                if (activityDateTime.isBefore(LocalDateTime.now())) {
                    r.setStatus(ReservationStatus.FINISHED);
                }
            }
        }

        reservationRepository.saveAll(reservations);
    }
}
