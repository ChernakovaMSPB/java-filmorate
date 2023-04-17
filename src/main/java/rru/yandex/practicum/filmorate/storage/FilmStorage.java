package rru.yandex.practicum.filmorate.storage;

import rru.yandex.practicum.filmorate.model.Film;

import java.util.List;

public interface FilmStorage {

    List<Film> findAll();

    Film create(Film film);

    Film getById(long id);

    Film update(Film film);

    void deleteFilm(long id);
    List<Film> getPopular(int count);
}


