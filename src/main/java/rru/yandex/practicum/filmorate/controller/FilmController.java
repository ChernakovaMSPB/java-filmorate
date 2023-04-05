package rru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import rru.yandex.practicum.filmorate.exceptions.ValidationException;
import rru.yandex.practicum.filmorate.model.Film;
import rru.yandex.practicum.filmorate.service.FilmService;
import rru.yandex.practicum.filmorate.storage.FilmStorage;

import javax.validation.Valid;
import java.time.LocalDate;
import java.util.*;

@RestController
@RequestMapping("/films")
@Slf4j
public class FilmController {
    //    private final Map<Integer, Film> films = new HashMap<>();
//    private Integer id = 1;
    private FilmStorage filmStorage;
    private FilmService filmService;

    @Autowired
    public FilmController(FilmStorage filmStorage, FilmService filmService) {
        this.filmStorage = filmStorage;
        this.filmService = filmService;
    }

    @GetMapping
    public Collection<Film> findAll() {
//        return films.values();
        return filmStorage.findAll();
    }

    @GetMapping("/{id}")
    public Film getFilmById(@PathVariable Long id) {
        return filmStorage.getFilmById(id);
    }

    @GetMapping("/popular")
    public List<Film> getPopular(@RequestParam(name = "count", defaultValue = "10") Integer count) {
        return filmService.getPopular(count);
    }

    @PostMapping
    public Film create(@RequestBody Film film) {
//        if (validationFilm(film)) {
//            film.setId(id);
//            films.put(id, film);
//            id++;
//            log.info("Добавлен фильм: " + film);
//        }
        log.info("Добавлен фильм: " + film);
        film = filmStorage.create(film);
        return film;
    }

    @PutMapping
    public Film update(@RequestBody Film film) {
//        if (validationFilm(film)) {
//            Film film1 = films.get(film.getId());
//            if (!film.getName().isBlank()) {
//                film1.setName(film.getName());
//            }
//            film1.setDescription(film.getDescription());
//            film1.setReleaseDate(film.getReleaseDate());
//            film1.setDuration(film.getDuration());
//            films.put(film.getId(), film1);
//            log.info("Фильм обновлен " + film1);
//        }
        log.info("Фильм обновлен " + film);
        film = filmStorage.update(film);
        return film;
    }

    //    public static boolean validationFilm(Film film) {
//        if (film.getName().isBlank()) {
//            throw new ValidationException("Название фильма не должно быть пустым");
//        }
//        if ((film.getDescription().length()) > 200 || (film.getDescription().isBlank())) {
//            log.info("Описание фильма {} не больше 200 символов", film.getDescription().length());
//            throw new ValidationException("Описание фильма не больше 200 символов: " + film.getDescription().length());
//        }
//        if (film.getReleaseDate().isBefore(LocalDate.of(1895, 12, 28))) {
//            log.info("Некорректная дата реализации фильма {}", film.getReleaseDate());
//            throw new ValidationException("Некорректная дата реализации фильма: " + film.getReleaseDate());
//        }
//        if (film.getDuration() <= 0) {
//            log.info("Продолжительность фильма {} должна быть положительной", film.getDuration());
//            throw new ValidationException("Продолжительность фильма должна быть положительной: " + film.getDuration());
//        }
//        return true;
//    }
    @PutMapping("/{id}/like/{userId}")
    public void addLike(@PathVariable Long id, @PathVariable Long userId) {
        filmService.addLike(id, userId);
    }

    @DeleteMapping("/{id}/like/{userId}")
    public void deleteLike(@PathVariable Long id, @PathVariable Long userId) {
        filmService.deleteLike(id, userId);
    }

    @DeleteMapping("/{id}")
    public Film delete(@PathVariable Long id) {
        log.info("Получен DELETE-запрос к эндпоинту: '/films' на удаление фильма с ID={}", id);
        return filmStorage.delete(id);
    }
}