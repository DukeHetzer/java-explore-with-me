package ru.practicum.ewm.explore.user.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.ewm.explore.user.model.User;

import java.util.List;

public interface UserRepository extends JpaRepository<User, Long> {
    @Query("SELECT u FROM User AS u " +
            "WHERE u.id IN ?1")
    Page<User> findAllByIds(List<Long> ids, Pageable pageable);
}