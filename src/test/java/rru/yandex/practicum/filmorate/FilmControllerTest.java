package rru.yandex.practicum.filmorate;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import rru.yandex.practicum.filmorate.controller.FilmController;
import rru.yandex.practicum.filmorate.exceptions.ValidationException;
import rru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
public class FilmControllerTest {

    @Test
    public void validateFilmWithWrongDescription() {
        Film film = new Film();
        film.setName("Непослушник");
        film.setDuration(115);
        film.setDescription("Комедиякомедиякомедиякомедиякомедиякомедиякомедиякомедиякомедиякомедиякомедиякомедиякомедиякомедиякомедиякомедиякомедиякомедиякомедиякомедиякомедиякомедиякомедиякомедиякомедиякомедиякомедиякомедиякомедиякомедия");
        film.setReleaseDate(LocalDate.of(2022, 12, 1));

        assertThrows(ValidationException.class, () -> FilmController.validationFilm(film));
    }

    @Test
    public void validateFilmWithWrongReleaseDate() {
        Film film = new Film();
        film.setName("");
        film.setDuration(100);
        film.setDescription("Комедия");
        film.setReleaseDate(LocalDate.of(1852, 10, 15));

        assertThrows(ValidationException.class, () -> FilmController.validationFilm(film));
    }

    @Test
    public void validateFilmWithWrongDuration() {
        Film film = new Film();
        film.setName("Темная ночь");
        film.setDuration(-100);
        film.setDescription("Ужасы");
        film.setReleaseDate(LocalDate.of(1992, 7, 30));

        assertThrows(ValidationException.class, () -> FilmController.validationFilm(film));
    }
}
