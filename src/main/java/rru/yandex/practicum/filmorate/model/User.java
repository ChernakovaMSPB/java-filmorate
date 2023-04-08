package rru.yandex.practicum.filmorate.model;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import javax.validation.Valid;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class User {

    long id;

    String email;

    String login;
    String name;

    LocalDate birthday;
    Set<Long> friends = new HashSet<>();

    public Set<Long> getFriends() {
        return friends;
    }

    public void addFriends(Long id) {
        friends.add(id);
    }

    public void deleteFriends(Long id) {
        friends.remove(id);
    }
}