package rru.yandex.practicum.filmorate.storage;

import org.springframework.stereotype.Component;
import rru.yandex.practicum.filmorate.model.Film;

import java.util.*;

@Component
public class InMemoryFilmStorage implements FilmStorage {

    Map<Long, Film> films = new LinkedHashMap<>();

    public Film create(Film film) {
        films.put(film.getId(), film);
        return film;
    }

    public Film getById(long id) {
        return films.get(id);
    }

    public List<Film> findAll() {
        return new ArrayList<>(films.values());
    }

    @Override
    public Film update(Film film) {
        return null;
    }

    @Override
    public void deleteFilm(long id) {

    }

    @Override
    public List<Film> getPopular(int count) {
        return null;
    }
}






