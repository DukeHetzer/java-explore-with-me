package ru.practicum.ewm.explore.event.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import ru.practicum.ewm.explore.enumerated.RequestStatus;

import java.util.List;

@Setter
@Getter
@ToString
@Builder
public class EventRequestStatusUpdateRequest {
    private List<Long> requestIds;
    private RequestStatus status;
}