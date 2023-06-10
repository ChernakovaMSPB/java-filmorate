package rru.yandex.practicum.filmorate.storage;

import rru.yandex.practicum.filmorate.model.User;

import java.util.List;

public interface FriendStorage {
    void addFriends(long userId, long friendId);

    void deleteFriends(long userId, long friendId);

    List<User> getAllFriends(long userId);

}
