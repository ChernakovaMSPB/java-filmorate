package rru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import rru.yandex.practicum.filmorate.exceptions.ValidationException;
import rru.yandex.practicum.filmorate.model.User;
import rru.yandex.practicum.filmorate.storage.UserStorage;

import java.time.LocalDate;
import java.util.*;

@Service
@Slf4j
public class UserService {
    @Autowired
    private UserStorage userStorage;
    long id = 1;

    public List<User> findAll() {
        return userStorage.findAll();
    }

    public User create(User user) {
        validationUser(user);
        user.setId(id++);
        if (user.getName() == null || user.getName().equals(" ")) {
            user.setName(user.getLogin());
        }
        userStorage.create(user);
        return user;
    }

    public User update(User user) {
        validationUser(user);
        if (userStorage.getById(user.getId()) == null) {
            throw new RuntimeException("Пользователь для обновления не найден");
        }
        userStorage.create(user);
        return user;
    }

    public User getUserById(long id) {
        User user = userStorage.getById(id);
        if (user == null) {
            throw new RuntimeException("Пользователь не найден");
        }
        return user;
    }


    public void addFriend(long userId, long friendId) {
        User user = userStorage.getById(userId);
        User friend = userStorage.getById(friendId);
        if (user == null || friend == null) {
            throw new RuntimeException("Нет возможности добавить пользователя в друзья (один из пользователей не найден)");
        }
        user.addFriends(userId);
        friend.addFriends(friendId);
    }

    public void deleteFriend(long userId, long friendId) {
        User user = userStorage.getById(userId);
        User friend = userStorage.getById(friendId);
        if (user == null || friend == null) {
            throw new RuntimeException("Нет возможности удалить пользователя из друзей (один из пользователей не найден)");
        }
        user.deleteFriends(userId);
        friend.deleteFriends(friendId);
    }

    public List<User> getAllFriends(long userId) {
        User user = userStorage.getById(userId);
        List<User> users = new ArrayList<>();
        if (user == null)
            throw new RuntimeException("Пользователь не найден");
        for (Long friendId : user.getFriends()) {
            users.add(userStorage.getById(friendId));
        }

        return users;
    }

    public List<User> getCommonFriends(long firstUserId, long secondUserId) {
        List<User> firstUsers = getAllFriends(firstUserId);
        List<User> secondUsers = getAllFriends(secondUserId);
        List<User> commonFriends = new ArrayList<>();

        for (User firstUser : firstUsers) {
            if (secondUsers.contains(firstUser))
                commonFriends.add(firstUser);
        }
        return commonFriends;
    }

    public static void validationUser(User user) {
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
    }
}
