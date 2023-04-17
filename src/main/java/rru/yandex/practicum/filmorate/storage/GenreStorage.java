package rru.yandex.practicum.filmorate.storage;

import rru.yandex.practicum.filmorate.model.Genre;

import java.util.List;

public interface GenreStorage {
    Genre getGenreById(int id);
    List<Genre> getAllGenre();
}
