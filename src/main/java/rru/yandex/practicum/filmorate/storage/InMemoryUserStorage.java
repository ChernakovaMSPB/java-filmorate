package rru.yandex.practicum.filmorate.storage;

import org.springframework.stereotype.Component;
import rru.yandex.practicum.filmorate.model.User;

import java.util.*;

@Component
public class InMemoryUserStorage implements UserStorage {

    Map<Long, User> users = new LinkedHashMap<>();

    @Override
    public List<User> findAll() {
        return new ArrayList<>(users.values());
    }

    @Override
    public User create(User user) {
        users.put(user.getId(), user);
        return user;
    }

    @Override
    public User findById(long id) {
        return users.get(id);
    }

    @Override
    public User update(User user) {
        return user;
    }

    @Override
    public void deleteUser(long id) {

    }
}

