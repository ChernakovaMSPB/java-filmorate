package rru.yandex.practicum.filmorate.model;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Film {

    long id;

    String name;

    String description;

    LocalDate releaseDate;

    Integer duration;
    Set<Long> likes = new HashSet<>();

    public Set<Long> getLikes() {
        return likes;
    }

    public void addLikes(long id) {
        likes.add(id);
    }

    public void deleteLikes(long id) {
        likes.remove(id);
    }
}
