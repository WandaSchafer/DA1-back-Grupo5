package com.example.authbackend.history;

import com.example.authbackend.reservation.Reservation;
import com.example.authbackend.reservation.ReservationRepository;
import com.example.authbackend.reservation.ReservationStatus;
import com.example.authbackend.user.User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class ActivityHistoryService {

    private final ReservationRepository reservationRepository;

    public ActivityHistoryService(ReservationRepository reservationRepository) {
        this.reservationRepository = reservationRepository;
    }

    public List<ActivityHistoryItemResponse> getHistory(User user,
                                                        LocalDate fromDate,
                                                        LocalDate toDate,
                                                        String destination) {

        updateFinishedReservations();

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
                r.getActivityAvailability().getActivity().getId(),
                r.getActivityAvailability().getDate(),
                r.getActivityAvailability().getActivity().getName(),
                r.getActivityAvailability().getActivity().getDestination(),
                r.getActivityAvailability().getActivity().getGuideName(),
                r.getActivityAvailability().getActivity().getDuration()
        );
    }

    @Transactional
    private void updateFinishedReservations() {
        List<Reservation> reservations = reservationRepository.findAll();

        for (Reservation r : reservations) {
            if (r.getStatus() == ReservationStatus.CONFIRMED
                    && r.getActivityAvailability() != null
                    && r.getActivityAvailability().getDate() != null
                    && r.getActivityAvailability().getTime() != null) {
                LocalDateTime activityDateTime = LocalDateTime.of(
                        r.getActivityAvailability().getDate(),
                        r.getActivityAvailability().getTime());
                if (activityDateTime.isBefore(LocalDateTime.now())) {
                    r.setStatus(ReservationStatus.FINISHED);
                }
            }
        }

        reservationRepository.saveAll(reservations);
    }
}
