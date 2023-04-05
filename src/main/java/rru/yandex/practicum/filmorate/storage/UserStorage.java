package rru.yandex.practicum.filmorate.storage;

import rru.yandex.practicum.filmorate.model.User;

import java.util.Collection;

public interface UserStorage {
    Collection<User> findAll();
    User create(User user);
    User update(User user);
    User getUserById(Long userId);
    User delete(Long userId);
}
