package ru.practicum.ewm.stats.service;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.ewm.stats.dto.EndpointHitDto;
import ru.practicum.ewm.stats.dto.EndpointStatsDto;
import ru.practicum.ewm.stats.exception.WrongDateException;
import ru.practicum.ewm.stats.mapper.EndpointHitMapper;
import ru.practicum.ewm.stats.model.EndpointHit;
import ru.practicum.ewm.stats.repository.EndpointHitRepository;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Service
public class StatsServiceImpl implements StatsService {
    private final EndpointHitRepository repository;
    public static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @Transactional
    @Override
    public EndpointHit addHit(EndpointHitDto endpointHitDto) {
        EndpointHit endpointHit = EndpointHitMapper.toHit(endpointHitDto);
        repository.save(endpointHit);

        log.info(endpointHit + " создан");
        return endpointHit;
    }

    @SneakyThrows
    @Override
    public List<EndpointStatsDto> getStats(String start, String end, List<String> uris, Boolean unique) {
        if (LocalDateTime.parse(start, TIME_FORMATTER).isAfter(LocalDateTime.parse(end, TIME_FORMATTER)))
            throw new WrongDateException("Стартовая дата некорректна");
        if (LocalDateTime.parse(start, TIME_FORMATTER).equals(LocalDateTime.parse(end, TIME_FORMATTER)))
            throw new WrongDateException("Финишная дата некорректна");
        List<EndpointHit> hits;
        if (uris.isEmpty())
            hits = repository.findAllByTimestampBetween(LocalDateTime.parse(start, TIME_FORMATTER),
                    LocalDateTime.parse(end, TIME_FORMATTER));
        else
            hits = repository.findAllByTimestampBetweenAndUriIn(LocalDateTime.parse(start, TIME_FORMATTER),
                    LocalDateTime.parse(end, TIME_FORMATTER), uris);
        return hits.stream()
                .collect(Collectors.groupingBy(EndpointHit::getUri))
                .values()
                .stream()
                .map(list -> {
                    EndpointHit endpointHit = list.get(0);
                    Integer hitCount = unique ? repository.getCountOfUniqueIpByUri(endpointHit.getUri()) :
                            repository.getCountIpByUri(endpointHit.getUri());
                    return new EndpointStatsDto(endpointHit.getApp(), endpointHit.getUri(), hitCount);
                })
                .sorted(Comparator.comparing(EndpointStatsDto::getHits, Comparator.reverseOrder()))
                .collect(Collectors.toList());
    }
}