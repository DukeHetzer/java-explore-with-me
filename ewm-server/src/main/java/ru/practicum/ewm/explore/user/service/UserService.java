package ru.practicum.ewm.explore.user.service;

import ru.practicum.ewm.explore.user.dto.UserDto;
import ru.practicum.ewm.explore.user.dto.UserIncomeDto;
import ru.practicum.ewm.explore.user.model.User;

import java.util.List;

public interface UserService {
    UserDto createUser(UserIncomeDto userIncomeDto);

    User readUser(Long userId);

    List<UserDto> getUsers(List<Long> ids, Integer from, Integer size);

    void deleteUser(Long userId);
}