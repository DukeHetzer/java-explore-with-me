package ru.practicum.ewm.explore.event.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.ewm.explore.event.model.Location;

public interface LocationRepository extends JpaRepository<Location, Long> {

}