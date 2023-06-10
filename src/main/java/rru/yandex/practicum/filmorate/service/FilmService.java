package rru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import rru.yandex.practicum.filmorate.exceptions.ValidationException;
import rru.yandex.practicum.filmorate.model.Film;
import rru.yandex.practicum.filmorate.storage.FilmStorage;
import rru.yandex.practicum.filmorate.storage.LikeStorage;
import rru.yandex.practicum.filmorate.storage.UserStorage;

import java.time.LocalDate;
import java.util.List;

@Service
@Slf4j
public class FilmService {
    private final FilmStorage filmStorage;
    private final UserStorage userStorage;
    private final LikeStorage likeStorage;

    public FilmService(@Qualifier("FilmDbStorage") FilmStorage filmStorage,
                       @Qualifier("UserDbStorage") UserStorage userStorage,
                       LikeStorage likeStorage) {
        this.filmStorage = filmStorage;
        this.userStorage = userStorage;
        this.likeStorage = likeStorage;
    }

    public List<Film> findAll() {
        return filmStorage.findAll();
    }

    public Film findById(long id) {
        Film film = filmStorage.getById(id);
        if (film == null) {
            throw new RuntimeException("Фильм " + id + " не найден");
        }
        return film;
    }

    public Film create(Film film) {
        validationFilm(film);
        return filmStorage.create(film);
    }

    public Film update(Film film) {
        validationFilm(film);
        if (filmStorage.getById(film.getId()) == null) {
            throw new RuntimeException("Фильм для обновления не найден");
        }
        return filmStorage.update(film);
    }

    public void addLike(long id, long userId) {
        if (userStorage.findById(userId) == null) {
            throw new RuntimeException("Пользователь " + userId + " не найден");
        }
        Film film = filmStorage.getById(id);
        if (film == null) {
            throw new RuntimeException("Фильм по " + id + " не найден");
        }
        likeStorage.addLikeToFilm(id, userId);
    }

    public void deleteLike(long id, long userId) {
        if (userStorage.findById(userId) == null) {
            throw new RuntimeException("Пользователь " + userId + " не найден");
        }
        Film film = filmStorage.getById(id);
        if (film == null) {
            throw new RuntimeException("Фильм по " + id + " не найден");
        }
        likeStorage.deleteLikeFromFilm(id, userId);
    }

    public List<Film> getPopular(Integer count) {
        return filmStorage.getPopular(count);
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
