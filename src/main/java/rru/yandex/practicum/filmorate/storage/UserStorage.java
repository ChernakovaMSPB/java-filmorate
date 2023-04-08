package rru.yandex.practicum.filmorate.storage;

import rru.yandex.practicum.filmorate.model.User;

import java.util.List;

public interface UserStorage {

    List<User> findAll();

    void create(User user);

    User getById(long id);
}
