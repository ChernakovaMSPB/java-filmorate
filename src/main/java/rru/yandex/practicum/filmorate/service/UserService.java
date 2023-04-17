package rru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import rru.yandex.practicum.filmorate.exceptions.ValidationException;
import rru.yandex.practicum.filmorate.model.User;
import rru.yandex.practicum.filmorate.storage.FriendStorage;
import rru.yandex.practicum.filmorate.storage.UserStorage;

import java.time.LocalDate;
import java.util.*;

@Service
@Slf4j
public class UserService {
    private final UserStorage userStorage;
    private final FriendStorage friendStorage;

    public UserService(@Qualifier("UserDbStorage") UserStorage userStorage, FriendStorage friendStorage) {
        this.userStorage = userStorage;
        this.friendStorage = friendStorage;
    }

    public List<User> findAll() {
        return userStorage.findAll();
    }

    public User create(User user) {
        validationUser(user);
        if (user.getName() == null || user.getName().equals(" ")) {
            user.setName(user.getLogin());
        }
        User addedUser = userStorage.create(user);
        return addedUser;
    }

    public User update(User user) {
        validationUser(user);
        if (userStorage.findById(user.getId()) == null) {
            throw new RuntimeException("Пользователь для обновления не найден");
        }
        userStorage.update(user);
        return user;
    }

    public User getUserById(long id) {
        User user = userStorage.findById(id);
        if (user == null) {
            throw new RuntimeException("Пользователь не найден");
        }
        return user;
    }

    public void addFriend(long userId, long friendId) {
        User user = userStorage.findById(userId);
        User friend = userStorage.findById(friendId);
        if (user == null || friend == null) {
            throw new RuntimeException("Нет возможности добавить пользователя в друзья (один из пользователей не найден)");
        }
        friendStorage.addFriends(userId, friendId);
    }

    public void deleteFriend(long userId, long friendId) {
        User user = userStorage.findById(userId);
        User friend = userStorage.findById(friendId);
        if (user == null || friend == null) {
            throw new RuntimeException("Нет возможности удалить пользователя из друзей (один из пользователей не найден)");
        }
        friendStorage.deleteFriends(userId, friendId);
    }

    public List<User> getAllFriends(long userId) {
        User user = userStorage.findById(userId);
        if (user == null)
            throw new RuntimeException("Пользователь не найден");
        return friendStorage.getAllFriends(userId);
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
        if ((user.getName().isBlank()) || (user.getName().isEmpty())) {
            user.setName(user.getLogin());
        }
        if (user.getBirthday().isAfter(LocalDate.now())) {
            log.info("Некорректная дата рождения пользователя {}", user.getBirthday());
            throw new ValidationException("Некорректная дата рождения пользователя: " + user.getBirthday());
        }
    }
}
