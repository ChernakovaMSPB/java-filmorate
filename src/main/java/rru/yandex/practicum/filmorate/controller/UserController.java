package rru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import rru.yandex.practicum.filmorate.exceptions.ValidationException;
import rru.yandex.practicum.filmorate.model.Film;
import rru.yandex.practicum.filmorate.model.User;
import rru.yandex.practicum.filmorate.service.UserService;
import rru.yandex.practicum.filmorate.storage.UserStorage;

import java.time.LocalDate;
import java.util.*;

@RestController
@RequestMapping("/users")
@Slf4j
public class UserController {
//    private final Map<String, User> users = new HashMap<>();
//    private Integer id = 1;
    private UserStorage userStorage;
    private UserService userService;

    @Autowired
    public UserController(UserStorage userStorage, UserService userService) {
        this.userStorage = userStorage;
        this.userService = userService;
    }

    @GetMapping
    public Collection<User> findAll() {
//        return users.values();
        return userStorage.findAll();
    }

    @GetMapping("/{id}")
    public User getUserById(@PathVariable Long id) {
        return userStorage.getUserById(id);
    }

    @GetMapping("/{id}/friends")
    public List<User> getFriends(@PathVariable Long id) {
        return userService.getFriends(id);
    }

    @GetMapping("/{id}/friends/common/{otherId}")
    public List<User> getCommonFriends(@PathVariable Long id, @PathVariable Long otherId) {
        return userService.getCommonFriends(id, otherId);
    }

    @PostMapping
    public User create(@RequestBody User user) {
//        if (validationUser(user)) {
//            user.setId(id);
//            users.put(String.valueOf(id), user);
//            id++;
//            log.info("Добавлен пользователь: " + user);
//        }
        log.info("Добавлен пользователь: " + user);
        user = userStorage.create(user);
        return user;
    }

    @PutMapping
    public User update(@RequestBody User user) {
//        if (validationUser(user)) {
//            User user1 = users.get(String.valueOf(user.getId()));
//            user1.setEmail(user.getEmail());
//            user1.setLogin(user.getLogin());
//            user1.setName(user.getName());
//            user1.setBirthday(user.getBirthday());
//            users.put(String.valueOf(user.getId()), user1);
//            log.info("Данные пользователя " + user1 + "обновлены");
//        }
        log.info("Данные пользователя " + user + "обновлены");
        user = userStorage.update(user);
        return user;
    }

    @PutMapping("/{id}/friends/{friendId}")
    public void addFriend(@PathVariable Long id, @PathVariable Long friendId) {
        userService.addFriend(id, friendId);
    }

    @DeleteMapping("/{id}/friends/{friendId}")
    public void deleteFriend(@PathVariable Long id, @PathVariable Long friendId) {
        userService.deleteFriend(id, friendId);
    }

    @DeleteMapping("/{id}")
    public User delete(@PathVariable Long id) {
        log.info("Получен DELETE-запрос на удаление пользователя с id {}", id);
        return userStorage.delete(id);
    }

//    public static boolean validationUser(User user) {
//        if (user.getEmail() == null || (!user.getEmail().contains("@")) || (user.getEmail().isBlank())) {
//            log.info("Некорректный e-mail пользователя {}", user.getEmail());
//            throw new ValidationException("Некорректный e-mail пользователя: " + user.getEmail());
//        }
//        if ((user.getLogin().isBlank()) || (user.getLogin().contains(" "))) {
//            log.info("Некорректный логин пользователя {}", user.getLogin());
//            throw new ValidationException("Некорректный логин пользователя: " + user.getLogin());
//        }
//        if (user.getName() == null) {
//            user.setName(user.getLogin());
//        }
//        if (user.getBirthday().isAfter(LocalDate.now())) {
//            log.info("Некорректная дата рождения пользователя {}", user.getBirthday());
//            throw new ValidationException("Некорректная дата рождения пользователя: " + user.getBirthday());
//        }
//        return true;
//    }
}