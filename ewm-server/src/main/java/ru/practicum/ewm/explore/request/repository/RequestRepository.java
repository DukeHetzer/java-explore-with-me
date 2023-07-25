package ru.practicum.ewm.explore.request.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.ewm.explore.enumerated.RequestStatus;
import ru.practicum.ewm.explore.request.model.Request;

import java.util.List;

public interface RequestRepository extends JpaRepository<Request, Long> {
    @Query("select r " +
            "from Request as r " +
            "where r.event.id = ?1")
    List<Request> findAllByEventId(Long eventId);

    @Query("select count(r) " +
            "from Request as r " +
            "where r.event.id = ?1 " +
            "and r.status = ?2")
    Long countConfirmedRequests(Long eventId, RequestStatus state);

    List<Request> findAllByRequesterId(Long userId);

    List<Request> findAllByRequesterIdAndEventId(Long userId, Long eventId);
}