package rru.yandex.practicum.filmorate.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import rru.yandex.practicum.filmorate.exceptions.FilmNotFoundException;
import rru.yandex.practicum.filmorate.exceptions.ValidationException;
import rru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;
import java.util.*;

@Component
@Slf4j
public class InMemoryFilmStorage implements FilmStorage {
    private Map<Long, Film> films = new HashMap<>();
    private Long id = 0L;

    @Override
    public Collection<Film> findAll() {
        return films.values();
    }

    @Override
    public Film create(Film film) {
        if (validationFilm(film)) {
            film.setId(id);
            films.put(id, film);
            id++;
            log.info("Добавлен фильм: " + film); // надо подумать над этой строкой
        }
        return film;
    }

    @Override
    public Film update(Film film) {
        if (validationFilm(film)) {
            Film film1 = films.get(film.getId());
            if (!film.getName().isBlank()) {
                film1.setName(film.getName());
            }
            film1.setDescription(film.getDescription());
            film1.setReleaseDate(film.getReleaseDate());
            film1.setDuration(film.getDuration());
            films.put(film.getId(), film1);
            log.info("Фильм обновлен " + film1);
        }
        return film;
    }

    @Override
    public Film getFilmById(Long filmId) {
        if (!films.containsKey(filmId)) {
            throw new FilmNotFoundException("Фильм по id " + filmId + "не найден");
        }
        return films.get(filmId);
    }

    @Override
    public Film delete(Long filmId) {
        if (filmId == null) {
            throw new ValidationException("Передан пустой аргумент");
        }
        if (!films.containsKey(filmId)) {
            throw new FilmNotFoundException("Фильм по id " + filmId + "не найден");
        }
        return  films.remove(filmId);
    }

    public static boolean validationFilm(Film film) {
        if (film.getName().isBlank()) {
            throw new ValidationException("Название фильма не должно быть пустым");
        }
        if ((film.getDescription().length()) > 200 || (film.getDescription().isBlank())) {
            log.info("Описание фильма {} не больше 200 символов", film.getDescription().length());
            throw new ValidationException("Описание фильма не больше 200 символов: " + film.getDescription().length());
        }
        if (film.getReleaseDate().isBefore(LocalDate.of(1895, 12, 28))) {
            log.info("Некорректная дата реализации фильма {}", film.getReleaseDate());
            throw new ValidationException("Некорректная дата реализации фильма: " + film.getReleaseDate());
        }
        if (film.getDuration() <= 0) {
            log.info("Продолжительность фильма {} должна быть положительной", film.getDuration());
            throw new ValidationException("Продолжительность фильма должна быть положительной: " + film.getDuration());
        }
        return true;
    }
}




