package rru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import rru.yandex.practicum.filmorate.exceptions.ValidationException;
import rru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.*;

@RestController
@RequestMapping("/users")
@Slf4j
public class UserController {
    private final Map<String, User> users = new HashMap<>();
    private Integer id = 1;

    @GetMapping
    public Collection<User> findAll() {
        return users.values();
    }

    @PostMapping
    public User create(@RequestBody User user) {
        if (validationUser(user)) {
            user.setId(id);
            users.put(String.valueOf(id), user);
            id++;
            log.info("Добавлен пользователь: " + user);
        }
        return user;
    }

    @PutMapping
    public User update(@RequestBody User user) {
        if (validationUser(user)) {
            User user1 = users.get(user.getId());
            user1.setEmail(user.getEmail());
            user1.setLogin(user.getLogin());
            user1.setName(user.getName());
            user1.setBirthday(user.getBirthday());
            users.put(String.valueOf(user.getId()), user1);
            log.info("Данные пользователя " + user1 + "обновлены");
        }
        return user;
    }

    public static boolean validationUser(User user) {
        if (user.getEmail() == null || (!user.getEmail().contains("@")) || (user.getEmail().isBlank())) {
            throw new ValidationException("Некорректный e-mail пользователя: " + user.getEmail());
        }
        if ((user.getLogin().isBlank()) || (user.getLogin().contains(" "))) {
            throw new ValidationException("Некорректный логин пользователя: " + user.getLogin());
        }
        if (user.getName().isBlank()) {
            user.setName(user.getLogin());
        }
        if (user.getBirthday().isAfter(LocalDate.now())) {
            throw new ValidationException("Некорректная дата рождения пользователя: " + user.getBirthday());
        }
        return true;
    }
}

