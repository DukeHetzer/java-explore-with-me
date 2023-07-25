package ru.practicum.ewm.explore.user.model;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.Size;

@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Getter
@Setter
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "email")
    @Email
    @Size(min = 6, max = 254)
    private String email;

    @Column(name = "name")
    @Size(min = 2, max = 250)
    private String name;
}