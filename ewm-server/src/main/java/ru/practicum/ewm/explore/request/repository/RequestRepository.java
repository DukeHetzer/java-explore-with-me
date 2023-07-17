package ru.practicum.ewm.explore.request.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.ewm.explore.enumerated.ParticipationStatus;
import ru.practicum.ewm.explore.request.model.ParticipationRequest;

import java.util.List;

public interface RequestRepository extends JpaRepository<ParticipationRequest, Long> {
    @Query("SELECT r " +
            "FROM ParticipationRequest AS r " +
            "WHERE r.event.id = ?1")
    List<ParticipationRequest> findAllByEventId(Long eventId);

    @Query("SELECT count(r) " +
            "FROM ParticipationRequest AS r " +
            "WHERE r.event.id = ?1 " +
            "AND r.status = ?2")
    Long countConfirmedRequests(Long eventId, ParticipationStatus state);

    List<ParticipationRequest> findAllByRequester_Id(Long userId);

    List<ParticipationRequest> findAllByRequester_IdAndEvent_Id(Long userId, Long eventId);
}