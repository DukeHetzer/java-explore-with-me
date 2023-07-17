package ru.practicum.ewm.explore.user.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ru.practicum.ewm.explore.exception.NotFoundException;
import ru.practicum.ewm.explore.user.dto.UserDto;
import ru.practicum.ewm.explore.user.dto.UserIncomeDto;
import ru.practicum.ewm.explore.user.mapper.UserMapper;
import ru.practicum.ewm.explore.user.model.User;
import ru.practicum.ewm.explore.user.repository.UserRepository;

import java.util.List;
import java.util.stream.Collectors;


@RequiredArgsConstructor
@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    @Override
    public User getUserById(Long userId) {
        return userRepository.findById(userId).orElseThrow(() -> new NotFoundException(
                String.format("User with id=%s was not found.", userId)));
    }

    @Override
    public UserDto addUser(UserIncomeDto body) {
        return UserMapper.userToDto(userRepository.save(UserMapper.newDtoToUser(body)));
    }

    @Override
    public void deleteUserById(Long userId) {
        getUserById(userId);
        userRepository.deleteById(userId);
    }

    @Override
    public List<UserDto> getUsers(List<Long> ids, Integer from, Integer size) {
        if (ids != null && ids.size() > 0) {
            return userRepository.findAllByIds(ids, PageRequest.of(from, size, Sort.unsorted())).stream()
                    .map(UserMapper::userToDto)
                    .collect(Collectors.toList());
        } else {
            return userRepository.findAll(PageRequest.of(from, size, Sort.unsorted())).stream()
                    .map(UserMapper::userToDto)
                    .collect(Collectors.toList());
        }
    }
}