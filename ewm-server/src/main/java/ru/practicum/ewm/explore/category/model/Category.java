package ru.practicum.ewm.explore.category.model;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.Size;

@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Getter
@Setter
@Table(name = "categories")
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    @Column(name = "name")
    @Size(max = 50)
    private String name;
}