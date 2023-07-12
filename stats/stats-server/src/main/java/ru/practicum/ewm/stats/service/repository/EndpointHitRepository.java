package ru.practicum.ewm.stats.service.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.practicum.ewm.stats.dto.EndpointStatsDto;
import ru.practicum.ewm.stats.service.model.EndpointHit;

import java.util.Date;
import java.util.List;

public interface EndpointHitRepository extends JpaRepository<EndpointHit, Long> {
    @Query("SELECT new ru.practicum.ewm.stats.dto.EndpointStatsDto(e.app, e.uri, count (e.ip))" +
            "FROM EndpointHit e " +
            "WHERE e.timestamp > :start AND e.timestamp < :end AND e.uri " +
            "IN :uris " +
            "GROUP BY e.app, e.uri " +
            "ORDER BY count (e.ip) DESC")
    List<EndpointStatsDto> findByUrisBetweenStartAndEnd(@Param("start") Date start,
                                                        @Param("end") Date end,
                                                        @Param("uris") List<String> uris);

    @Query("SELECT new ru.practicum.ewm.stats.dto.EndpointStatsDto(e.app, e.uri, count (DISTINCT e.ip))" +
            "FROM EndpointHit e " +
            "WHERE e.timestamp > :start AND e.timestamp < :end AND e.uri " +
            "IN :uris " +
            "GROUP BY e.app, e.uri " +
            "ORDER BY count (e.ip) DESC")
    List<EndpointStatsDto> findUniqueByUrisBetweenStartAndEnd(@Param("start") Date start,
                                                              @Param("end") Date end,
                                                              @Param("uris") List<String> uris);

    @Query("SELECT new ru.practicum.ewm.stats.dto.EndpointStatsDto(e.app, e.uri, count (e.ip))" +
            "FROM EndpointHit e " +
            "WHERE e.timestamp > :start AND e.timestamp < :end " +
            "GROUP BY e.app, e.uri " +
            "ORDER BY count (e.ip) DESC")
    List<EndpointStatsDto> findAllBetweenStartAndEnd(@Param("start") Date start,
                                                     @Param("end") Date end);

    @Query("SELECT new ru.practicum.ewm.stats.dto.EndpointStatsDto(e.app, e.uri, count (DISTINCT e.ip))" +
            "FROM EndpointHit e " +
            "WHERE e.timestamp > :start AND e.timestamp < :end " +
            "GROUP BY e.app, e.uri " +
            "ORDER BY count (e.ip) DESC")
    List<EndpointStatsDto> findAllUniqueBetweenStartAndEnd(@Param("start") Date start,
                                                           @Param("end") Date end);
}