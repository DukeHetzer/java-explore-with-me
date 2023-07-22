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
    List<RequestDto> confirmedRequests;
    List<RequestDto> rejectedRequests;
}