package rru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import rru.yandex.practicum.filmorate.model.Film;
import rru.yandex.practicum.filmorate.service.FilmService;

import java.util.*;

@RestController
@RequestMapping("/films")
@Slf4j
public class FilmController {
    //    private final Map<Integer, Film> films = new HashMap<>();
//    private Integer id = 1;
//    private FilmStorage filmStorage;
    FilmService filmService;

    @Autowired
    public FilmController(FilmService filmService) {
        this.filmService = filmService;
    }

    @GetMapping
    public List<Film> findAll() {
//        return films.values();
        return filmService.findAll();
    }

    @GetMapping("/{id}")
    public Film getFilmById(@PathVariable long id) {
        return filmService.getFilmById(id);
    }

    @GetMapping("/popular")
    public List<Film> getPopular(@RequestParam(required = false) Integer count) {
        if (count == null) count = 10;
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
        return filmService.create(film);
//        return film;
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
        return filmService.update(film);
//        return film;
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
    public void addLike(@PathVariable long id, @PathVariable long userId) {
        filmService.addLike(id, userId);
    }

    @DeleteMapping("/{id}/like/{userId}")
    public void deleteLike(@PathVariable long id, @PathVariable long userId) {
        filmService.deleteLike(id, userId);
    }

//    @DeleteMapping("/{id}")
//    public Film delete(@PathVariable Long id) {
//        log.info("Получен DELETE-запрос на удаление фильма с id {}", id);
//        return filmStorage.delete(id);
//    }
}