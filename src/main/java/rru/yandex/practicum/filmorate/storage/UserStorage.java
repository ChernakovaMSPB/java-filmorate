package rru.yandex.practicum.filmorate.storage;

import rru.yandex.practicum.filmorate.model.User;

import java.util.List;

public interface UserStorage {

    List<User> findAll();

    User create(User user);

    User findById(long id);
    User update(User user);
    void deleteUser(long id);
}
