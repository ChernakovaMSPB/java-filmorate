package rru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import rru.yandex.practicum.filmorate.exceptions.FilmNotFoundException;
import rru.yandex.practicum.filmorate.exceptions.UserNotFoundException;
import rru.yandex.practicum.filmorate.exceptions.ValidationException;
import rru.yandex.practicum.filmorate.model.Film;
import rru.yandex.practicum.filmorate.storage.FilmStorage;
import rru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class FilmService {
    private FilmStorage filmStorage;
    private UserStorage userStorage;

    @Autowired
    public FilmService(FilmStorage filmStorage, UserStorage userStorage) {
        this.filmStorage = filmStorage;
        this.userStorage = userStorage;
    }

    public void addLike(Long filmId, Long userId) {
        Film film = filmStorage.getFilmById(filmId);
        if (film != null) {
            if (userStorage.getUserById(userId) != null) {
                film.getLikes().add(userId);
            } else {
                throw new UserNotFoundException("Пользователь с данным id " + userId + " не найден");
            }
        } else {
            throw new FilmNotFoundException("Фильм по id " + filmId + "не найден");
        }

    }

    public void deleteLike(Long filmId, Long userId) {
        Film film = filmStorage.getFilmById(filmId);
        if (film != null) {
            if (film.getLikes().contains(userId)) {
                film.getLikes().remove(userId);
            } else {
                throw new UserNotFoundException("Лайк от пользователя по id " + userId + " не найден");
            }
        } else {
            throw new FilmNotFoundException("Фильм по id " + filmId + "не найден");
        }
    }

    public List<Film> getPopular(Integer count) {
        if (count < 1) { // 10 популярных фильмов для вывода
            new ValidationException("Количество фильмов для вывода не должно быть меньше 1");
        }
        return filmStorage.findAll().stream()
                .sorted((o1, o2) -> o2.getLikes().size() - o1.getLikes().size())
                .limit(count)
                .collect(Collectors.toList());
    }

}
