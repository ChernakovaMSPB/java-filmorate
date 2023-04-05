package rru.yandex.practicum.filmorate.storage;

import rru.yandex.practicum.filmorate.model.Film;

import java.util.Collection;

public interface FilmStorage {

    Collection<Film> findAll();

    Film create(Film film);

    Film update(Film film);

    Film getFilmById(Long filmId);

    Film delete(Long filmId);
}
