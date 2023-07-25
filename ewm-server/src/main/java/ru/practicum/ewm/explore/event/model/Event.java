package ru.practicum.ewm.explore.event.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import ru.practicum.ewm.explore.category.model.Category;
import ru.practicum.ewm.explore.comments.dto.EventCommentDto;
import ru.practicum.ewm.explore.enumerated.StatusEvent;
import ru.practicum.ewm.explore.user.model.User;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Getter
@Setter
@Table(name = "events")
public class Event {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;
    @ManyToOne
    @JoinColumn(name = "initiator_id")
    private User initiator;
    @ManyToOne
    @JoinColumn(name = "location_id")
    private Location location;
    @Column(name = "annotation")
    @Size(min = 20, max = 2000)
    private String annotation;
    @Column(name = "confirmed_requests")
    private Long confirmedRequests;
    @Column(name = "description")
    private String description;
    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private StatusEvent state;
    @Column(name = "title")
    @Size(min = 3, max = 120)
    private String title;
    @Column(name = "created_on")
    private LocalDateTime createdOn;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Column(name = "event_date")
    private LocalDateTime eventDate;
    @Column(name = "paid")
    private Boolean paid;
    @Column(name = "participant_limit")
    private Integer participantLimit;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Column(name = "published_on")
    private LocalDateTime publishedOn;
    @Column(name = "request_moderation")
    private Boolean requestModeration;
    @Column(name = "views")
    private Long views;
    @Transient
    private List<EventCommentDto> comments;

    public void addView() {
        views++;
    }
}