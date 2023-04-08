package rru.yandex.practicum.filmorate.storage;

import org.springframework.stereotype.Component;
import rru.yandex.practicum.filmorate.model.Film;

import java.util.*;

@Component
public class InMemoryFilmStorage implements FilmStorage {

    Map<Long, Film> films = new LinkedHashMap<>();

    public void create(Film film) {
        films.put(film.getId(), film);
    }

    public Film getById(long id) {
        return films.get(id);
    }

    public List<Film> findAll() {
        return new ArrayList<>(films.values());
    }
}






