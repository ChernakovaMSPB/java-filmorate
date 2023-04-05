package rru.yandex.practicum.filmorate.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import rru.yandex.practicum.filmorate.exceptions.UserNotFoundException;
import rru.yandex.practicum.filmorate.exceptions.ValidationException;
import rru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.*;

@Component
@Slf4j
public class InMemoryUserStorage implements UserStorage {
    public Map<Long, User> users = new HashMap<>();

    private Long id = 0L;

    @Override
    public Collection<User> findAll() {
        return users.values();
    }

    @Override
    public User create(User user) {
        if (validationUser(user)) {
            user.setId(id);
            users.put(Long.valueOf(id), user);
            id++;
            log.info("Добавлен пользователь: " + user);
        }
        return user;
    }

    @Override
    public User update(User user) {
        if (validationUser(user)) {
            User user1 = users.get(user.getId());
            user1.setEmail(user.getEmail());
            user1.setLogin(user.getLogin());
            user1.setName(user.getName());
            user1.setBirthday(user.getBirthday());
            users.put(user.getId(), user1);
            log.info("Данные пользователя " + user1 + "обновлены");
        }
        return user;
    }

    @Override
    public User getUserById(Long userId) {
        if (!users.containsKey(userId)) {
            throw new UserNotFoundException("Пользователь с ID=" + userId + " не найден!");
        }
        return users.get(userId);
    }

    @Override
    public User delete(Long userId) {
        if (userId == null) {
            throw new ValidationException("Передан пустой аргумент!");
        }
        if (!users.containsKey(userId)) {
            throw new UserNotFoundException("Пользователь с ID=" + userId + " не найден!");
        }
        // удаляем из списка друзей пользователя у других пользователей
        for (User user : users.values()) {
            user.getFriends().remove(userId);
        }
        return users.remove(userId);
    }

    public static boolean validationUser(User user) {
        if (user.getEmail() == null || (!user.getEmail().contains("@")) || (user.getEmail().isBlank())) {
            log.info("Некорректный e-mail пользователя {}", user.getEmail());
            throw new ValidationException("Некорректный e-mail пользователя: " + user.getEmail());
        }
        if ((user.getLogin().isBlank()) || (user.getLogin().contains(" "))) {
            log.info("Некорректный логин пользователя {}", user.getLogin());
            throw new ValidationException("Некорректный логин пользователя: " + user.getLogin());
        }
        if (user.getName() == null) {
            user.setName(user.getLogin());
        }
        if (user.getBirthday().isAfter(LocalDate.now())) {
            log.info("Некорректная дата рождения пользователя {}", user.getBirthday());
            throw new ValidationException("Некорректная дата рождения пользователя: " + user.getBirthday());
        }
        return true;
    }
}

