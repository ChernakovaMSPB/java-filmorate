package rru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import rru.yandex.practicum.filmorate.model.Film;
import rru.yandex.practicum.filmorate.service.FilmService;

import java.util.*;

@RestController
@RequestMapping("/films")
@Slf4j
public class FilmController {

    private final FilmService filmService;

    public FilmController(FilmService filmService) {
        this.filmService = filmService;
    }

    @GetMapping
    public List<Film> findAll() {
        return filmService.findAll();
    }

    @GetMapping("/{id}")
    public Film getFilmById(@PathVariable long id) {
        return filmService.findById(id);
    }

    @GetMapping("/popular")
    public List<Film> getPopular(@RequestParam(required = false) Integer count) {
        if (count == null) count = 10;
        return filmService.getPopular(count);
    }

    @PostMapping
    public Film create(@RequestBody Film film) {
        return filmService.create(film);
    }

    @PutMapping
    public Film update(@RequestBody Film film) {
        return filmService.update(film);
    }

    @PutMapping("/{id}/like/{userId}")
    public void addLike(@PathVariable long id, @PathVariable long userId) {
        filmService.addLike(id, userId);
    }

    @DeleteMapping("/{id}/like/{userId}")
    public void deleteLike(@PathVariable long id, @PathVariable long userId) {
        filmService.deleteLike(id, userId);
    }

}