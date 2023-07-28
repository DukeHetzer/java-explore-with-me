package ru.practicum.ewm.stats.model;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;

@Entity
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@Table(name = "endpoint_hits")
public class EndpointHit {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    @NotBlank
    @Column(name = "app")
    private String app;
    @NotBlank
    @Column(name = "uri")
    private String uri;
    @NotBlank
    @Column(name = "ip")
    private String ip;
    @Column(name = "hit_time")
    private LocalDateTime timestamp;
}