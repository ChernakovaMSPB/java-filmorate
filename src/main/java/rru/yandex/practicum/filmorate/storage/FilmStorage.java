package rru.yandex.practicum.filmorate.storage;

import rru.yandex.practicum.filmorate.model.Film;

import java.util.List;

public interface FilmStorage {

    List<Film> findAll();

    void add(Film film);

    Film getById(long id);
}


