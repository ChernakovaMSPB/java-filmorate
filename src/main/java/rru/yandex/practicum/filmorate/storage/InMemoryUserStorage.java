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
    public void add(User user) {
        users.put(user.getId(), user);
    }

    @Override
    public User getById(long id) {
        return users.get(id);
    }
}

