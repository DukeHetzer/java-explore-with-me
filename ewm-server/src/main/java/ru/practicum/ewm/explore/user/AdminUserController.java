package ru.practicum.ewm.explore.user;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.explore.exception.BadRequestException;
import ru.practicum.ewm.explore.user.dto.UserDto;
import ru.practicum.ewm.explore.user.dto.UserIncomeDto;
import ru.practicum.ewm.explore.user.service.UserService;
import ru.practicum.ewm.explore.util.OnCreate;

import javax.validation.Valid;
import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/admin/users")
public class AdminUserController {
    private final UserService service;

    @GetMapping
    public List<UserDto> getAdmins(@RequestParam(name = "ids", defaultValue = "") List<Long> ids,
                                   @RequestParam(name = "from", defaultValue = "0") Integer from,
                                   @RequestParam(name = "size", defaultValue = "10") Integer size) {
        return service.getUsers(ids, from, size);
    }

    @PostMapping
    @Validated({OnCreate.class})
    @ResponseStatus(HttpStatus.CREATED)
    public UserDto addAdmin(@RequestBody @Valid UserIncomeDto body) {
        if (body.getEmail() == null || (!body.getEmail().contains("@") && !body.getEmail().contains(".")))
            throw new BadRequestException("Email is wrong");
        return service.addUser(body);
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<Void> deleteAdmin(@PathVariable Long userId) {
        service.deleteUserById(userId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}