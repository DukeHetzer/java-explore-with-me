package ru.practicum.ewm.explore.user.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ru.practicum.ewm.explore.exception.BadRequestException;
import ru.practicum.ewm.explore.exception.NotFoundException;
import ru.practicum.ewm.explore.user.dto.UserDto;
import ru.practicum.ewm.explore.user.dto.UserIncomeDto;
import ru.practicum.ewm.explore.user.mapper.UserMapper;
import ru.practicum.ewm.explore.user.model.User;
import ru.practicum.ewm.explore.user.repository.UserRepository;

import java.util.List;
import java.util.stream.Collectors;

import static ru.practicum.ewm.explore.user.mapper.UserMapper.toUser;


@Slf4j
@RequiredArgsConstructor
@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    @Override
    public UserDto createUser(UserIncomeDto body) {
        checkEmail(body);
        User user = userRepository.save(toUser(body));

        log.info(user + " создан");
        return UserMapper.toDto(user);
    }

    @Override
    public User readUser(Long userId) {
        return userRepository.findById(userId).orElseThrow(
                () -> new NotFoundException("User с id=" + userId + " не найден"));
    }

    @Override
    public List<UserDto> getUsers(List<Long> ids, Integer from, Integer size) {
        if (ids != null && ids.size() > 0) {
            return userRepository.findAllByIds(ids, PageRequest.of(from, size, Sort.unsorted())).stream()
                    .map(UserMapper::toDto)
                    .collect(Collectors.toList());
        } else {
            return userRepository.findAll(PageRequest.of(from, size, Sort.unsorted())).stream()
                    .map(UserMapper::toDto)
                    .collect(Collectors.toList());
        }
    }

    @Override
    public void deleteUser(Long userId) {
        readUser(userId);
        userRepository.deleteById(userId);
        log.info("User с id={} удален", userId);
    }

    private void checkEmail(UserIncomeDto body) {
        if (body.getEmail() == null || !body.getEmail().contains("@") || !body.getEmail().contains("."))
            throw new BadRequestException("Почта некорректна");
    }
}