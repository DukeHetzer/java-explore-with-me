package ru.practicum.ewm.explore.user.mapper;

import lombok.experimental.UtilityClass;
import ru.practicum.ewm.explore.user.dto.UserDto;
import ru.practicum.ewm.explore.user.dto.UserIncomeDto;
import ru.practicum.ewm.explore.user.dto.UserShortDto;
import ru.practicum.ewm.explore.user.model.User;

@UtilityClass
public class UserMapper {
    public static UserDto toDto(User user) {
        return UserDto.builder()
                .id(user.getId())
                .email(user.getEmail())
                .name(user.getName())
                .build();
    }

    public static UserShortDto toShortDto(User user) {
        return UserShortDto.builder()
                .id(user.getId())
                .name(user.getName())
                .build();
    }

    public static User toUser(UserIncomeDto body) {
        return User.builder()
                .name(body.getName())
                .email(body.getEmail())
                .build();
    }
}