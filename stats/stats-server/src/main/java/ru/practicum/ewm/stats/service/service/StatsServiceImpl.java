package ru.practicum.ewm.stats.service.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.ewm.stats.dto.EndpointHitDto;
import ru.practicum.ewm.stats.dto.EndpointStatsDto;
import ru.practicum.ewm.stats.service.exception.WrongDateException;
import ru.practicum.ewm.stats.service.mapper.EndpointHitMapper;
import ru.practicum.ewm.stats.service.model.EndpointHit;
import ru.practicum.ewm.stats.service.repository.EndpointHitRepository;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class StatsServiceImpl implements StatsService {
    private final EndpointHitRepository endpointHitRepository;
    private final String datePattern = "yyyy-MM-dd HH:mm:ss";
    private final SimpleDateFormat formatter = new SimpleDateFormat(datePattern);

    @Override
    public EndpointHit addHit(EndpointHitDto endpointHitDto) {
        EndpointHit endpointHit = EndpointHitMapper.map(endpointHitDto);
        endpointHitRepository.save(endpointHit);

        log.info("EndpointHit успешно добавлен");
        return endpointHit;
    }

    @Override
    public List<EndpointStatsDto> getStats(String start, String end, List<String> uris, Boolean unique) {
        Date startDate;
        Date endDate;
        try {
            startDate = formatter.parse(start);
        } catch (ParseException e) {
            throw new WrongDateException("Стартовая дата некорректна");
        }
        try {
            endDate = formatter.parse(end);
        } catch (ParseException e) {
            throw new WrongDateException("Финишная дата некорректна");
        }
        if (uris != null) {
            return unique ? endpointHitRepository.findUniqueByUrisBetweenStartAndEnd(startDate, endDate, uris) :
                    endpointHitRepository.findByUrisBetweenStartAndEnd(startDate, endDate, uris);
        } else {
            return unique ? endpointHitRepository.findAllUniqueBetweenStartAndEnd(startDate, endDate) :
                    endpointHitRepository.findAllBetweenStartAndEnd(startDate, endDate);
        }
    }
}