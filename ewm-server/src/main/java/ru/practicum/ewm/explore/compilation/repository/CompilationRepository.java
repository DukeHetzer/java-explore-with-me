package ru.practicum.ewm.explore.compilation.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.ewm.explore.compilation.model.Compilation;

import java.util.Set;

public interface CompilationRepository extends JpaRepository<Compilation, Long> {
    @Query(value = "select event_id " +
            "from compilation_event " +
            "where compilation_id = ?1", nativeQuery = true)
    Set<Long> getEventsByCompilation(Long compId);
}