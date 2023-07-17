package ru.practicum.ewm.explore.user.mapper;

import ru.practicum.ewm.explore.user.dto.UserDto;
import ru.practicum.ewm.explore.user.dto.UserIncomeDto;
import ru.practicum.ewm.explore.user.dto.UserShortDto;
import ru.practicum.ewm.explore.user.model.User;

public class UserMapper {
    public static UserDto userToDto(User user) {
        return UserDto.builder()
                .id(user.getId())
                .email(user.getEmail())
                .name(user.getName())
                .build();
    }

    public static UserShortDto userToShortDto(User user) {
        return UserShortDto.builder()
                .id(user.getId())
                .name(user.getName())
                .build();
    }

    public static User newDtoToUser(UserIncomeDto body) {
        return User.builder()
                .name(body.getName())
                .email(body.getEmail())
                .build();
    }
}