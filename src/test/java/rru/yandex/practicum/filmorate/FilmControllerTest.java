package rru.yandex.practicum.filmorate;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import rru.yandex.practicum.filmorate.controller.FilmController;
import rru.yandex.practicum.filmorate.exceptions.ValidationException;
import rru.yandex.practicum.filmorate.model.Film;
import rru.yandex.practicum.filmorate.service.FilmService;
import rru.yandex.practicum.filmorate.storage.FilmStorage;
import rru.yandex.practicum.filmorate.storage.InMemoryFilmStorage;
import rru.yandex.practicum.filmorate.storage.InMemoryUserStorage;
import rru.yandex.practicum.filmorate.storage.UserStorage;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
public class FilmControllerTest {
    Film film;
    FilmController filmController;
    FilmStorage filmStorage;
    UserStorage userStorage;

    @BeforeEach
    public void beforeEach() {
        filmStorage = new InMemoryFilmStorage();
        userStorage = new InMemoryUserStorage();
        filmController = new FilmController(filmStorage, new FilmService(filmStorage, userStorage));
        film = Film.builder()
                .name("Breakfast at Tiffany's")
                .description("American romantic comedy film directed by Blake Edwards, written by George Axelrod," +
                        " adapted from Truman Capote's 1958 novella of the same name.")
                .releaseDate(LocalDate.of(1961, 10, 5))
                .duration(114)
                .build();
    }

    @Test
    public void validateFilmWithWrongDescription() {
//        Film film = new Film();
        film.setName("Непослушник");
        film.setDuration(115);
        film.setDescription("Комедиякомедиякомедиякомедиякомедиякомедиякомедиякомедиякомедиякомедиякомедиякомедиякомедиякомедиякомедиякомедиякомедиякомедиякомедиякомедиякомедиякомедиякомедиякомедиякомедиякомедиякомедиякомедиякомедиякомедия");
        film.setReleaseDate(LocalDate.of(2022, 12, 1));

        assertThrows(ValidationException.class, () -> InMemoryFilmStorage.validationFilm(film));
    }

    @Test
    public void validateFilmWithWrongReleaseDate() {
//        Film film = new Film();
        film.setName("");
        film.setDuration(100);
        film.setDescription("Комедия");
        film.setReleaseDate(LocalDate.of(1852, 10, 15));

        assertThrows(ValidationException.class, () -> InMemoryFilmStorage.validationFilm(film));
    }

    @Test
    public void validateFilmWithWrongDuration() {
//        Film film = new Film();
        film.setName("Темная ночь");
        film.setDuration(-100);
        film.setDescription("Ужасы");
        film.setReleaseDate(LocalDate.of(1992, 7, 30));

        assertThrows(ValidationException.class, () -> InMemoryFilmStorage.validationFilm(film));
    }
}

