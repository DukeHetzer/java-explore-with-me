package ru.practicum.ewm.explore.compilation.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.ewm.explore.compilation.model.Compilation;

import java.util.Set;

public interface CompilationRepository extends JpaRepository<Compilation, Long> {
    @Query(value = "SELECT event_id FROM compilation_event WHERE compilation_id = ?1", nativeQuery = true)
    Set<Long> getEventsByCompilation(Long compId);
}