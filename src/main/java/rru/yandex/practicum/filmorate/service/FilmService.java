package rru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import rru.yandex.practicum.filmorate.exceptions.ValidationException;
import rru.yandex.practicum.filmorate.model.Film;
import rru.yandex.practicum.filmorate.storage.FilmStorage;
import rru.yandex.practicum.filmorate.storage.UserStorage;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class FilmService {
    @Autowired
    FilmStorage filmStorage;
    @Autowired
    UserStorage userStorage;
    long id = 1;

    public List<Film> findAll() {
        return filmStorage.findAll();
    }

    public Film getFilmById(long id) {
        Film film = filmStorage.getById(id);
        if (film == null) {
            throw new RuntimeException("Фильм не найден");
        }
        return film;
    }

    public Film create(Film film) {
        validationFilm(film);
        film.setId(id++);
        filmStorage.create(film);
        return film;
    }

    public Film update(Film film) {
        validationFilm(film);
        if (filmStorage.getById(film.getId()) == null) {
            throw new RuntimeException("Фильм для обновления не найден");
        }
        filmStorage.create(film);
        return film;
    }

    public void addLike(long id, long userId) {
        if (userStorage.getById(userId) == null) {
            throw new RuntimeException("Пользователь не найден");
        }
        Film film = filmStorage.getById(id);
        if (film == null) {
            throw new RuntimeException("Фильм не найден");
        }
        film.addLikes(userId);
    }

    public void deleteLike(long id, long userId) {
        if (userStorage.getById(userId) == null) {
            throw new RuntimeException("Пользователь не найден");
        }
        Film film = filmStorage.getById(id);
        if (film == null) {
            throw new RuntimeException("Фильм не найден");
        }
        film.deleteLikes(userId);
    }

    public List<Film> getPopular(Integer count) {
        List<Film> topFilms = new ArrayList<>();
        List<Film> allFilms = filmStorage.findAll();
        allFilms.sort((Film f1, Film f2) -> f2.getLikes().size() - f1.getLikes().size());

        if (allFilms.size() > count) {
            for (int i = 0; i < count; i++) {
                topFilms.add(allFilms.get(i));
            }
        } else {
            topFilms.addAll(allFilms);
        }
        return topFilms;
    }

    public static void validationFilm(Film film) {
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
    }

}
