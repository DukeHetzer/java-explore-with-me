package ru.practicum.ewm.explore.request.dto;

import lombok.*;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@ToString
@Builder
public class RequestStatusUpdate {
    private List<RequestDto> confirmedRequests;
    private List<RequestDto> rejectedRequests;
}