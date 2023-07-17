package ru.practicum.ewm.explore.request.model;

import lombok.*;
import lombok.extern.jackson.Jacksonized;
import ru.practicum.ewm.explore.enumerated.ParticipationStatus;
import ru.practicum.ewm.explore.event.model.Event;
import ru.practicum.ewm.explore.user.model.User;

import javax.persistence.*;
import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@ToString
@EqualsAndHashCode(of = "id")
@Entity
@Table(name = "requests")
@Jacksonized
public class ParticipationRequest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "event_id", nullable = false)
    private Event event;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "requester_id", nullable = false)
    private User requester;

    @Column(name = "created", nullable = false)
    private LocalDateTime created;

    @Column(name = "status", nullable = false)
    @Enumerated(EnumType.STRING)
    private ParticipationStatus status;
}