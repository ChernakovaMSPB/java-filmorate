package rru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import rru.yandex.practicum.filmorate.exceptions.ValidationException;
import rru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;
import java.util.*;

@RestController
@RequestMapping("/films")
@Slf4j
public class FilmController {
    private final Map<Integer, Film> films = new HashMap<>();
    private Integer id = 1;

    @GetMapping
    public Collection<Film> findAll() {
        return films.values();
    }

    @PostMapping
    public Film create(@RequestBody Film film) {
        if (validationFilm(film)) {
            film.setId(id);
            films.put(id, film);
            id++;
            log.info("Добавлен фильм: " + film);
        }
        return film;
    }

    @PutMapping
    public Film update(@RequestBody Film film) {
        if (validationFilm(film)) {
            Film film1 = films.get(film.getId());
            if (film.getName().isBlank()) {
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

    public static boolean validationFilm(Film film) {
        if (film.getName().isEmpty()) {
            throw new ValidationException("Название фильма не должно быть пустым");
        }
        if ((film.getDescription().length()) > 200 || (film.getDescription().isBlank())) {
            throw new ValidationException("Описание фильма не больше 200 символов: " + film.getDescription().length());
        }
        if (film.getReleaseDate().isBefore(LocalDate.of(1895, 12, 28))) {
            throw new ValidationException("Некорректная дата реализации фильма: " + film.getReleaseDate());
        }
        if (film.getDuration() <= 0) {
            throw new ValidationException("Продолжительность фильма должна быть положительной: " + film.getDuration());
        }
        return true;
    }
}

