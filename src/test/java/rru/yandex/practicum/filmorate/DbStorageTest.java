package rru.yandex.practicum.filmorate;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabase;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;
import rru.yandex.practicum.filmorate.model.Film;
import rru.yandex.practicum.filmorate.model.Genre;
import rru.yandex.practicum.filmorate.model.Mpa;
import rru.yandex.practicum.filmorate.model.User;
import rru.yandex.practicum.filmorate.storage.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class DbStorageTest {
    private EmbeddedDatabase embeddedDatabase;
    private JdbcTemplate jdbcTemplate;
    private UserDbStorage userDbStorage;
    private FilmDbStorage filmDbStorage;
    private FriendDbStorage friendDbStorage;
    private GenreDbStorage genreDbStorage;
    private LikeDbStorage likeDbStorage;
    private MpaDbStorage mpaDbStorage;
    User user = new User(0,
            "spb@mail.ru",
            "Oleg83",
            "Oleg",
            LocalDate.of(1983, 10, 10)
    );
    User updatedUser = new User(1,
            "mari@mail.ru",
            "Mari90",
            "Mari",
            LocalDate.of(1990, 7, 15)
    );
    Film film = new Film(0,
            "Name",
            "Description",
            LocalDate.of(1985, 5, 10),
            120,
            new Mpa(1)
    );
    Film updatedFilm = new Film(1,
            "Name",
            "Description",
            LocalDate.of(1989, 10, 12),
            120,
            new Mpa(2)
    );

    @BeforeEach
    void setUp() {
        embeddedDatabase = new EmbeddedDatabaseBuilder()
                .addScript("schema.sql")
                .addScript("data.sql")
                .setType(EmbeddedDatabaseType.H2)
                .build();
        jdbcTemplate = new JdbcTemplate(embeddedDatabase);
        userDbStorage = new UserDbStorage(jdbcTemplate);
        filmDbStorage = new FilmDbStorage(jdbcTemplate);
        friendDbStorage = new FriendDbStorage(jdbcTemplate);
        genreDbStorage = new GenreDbStorage(jdbcTemplate);
        likeDbStorage = new LikeDbStorage(jdbcTemplate);
        mpaDbStorage = new MpaDbStorage(jdbcTemplate);
    }

    @AfterEach
    void tearDown() {
        embeddedDatabase.shutdown();
    }

    @Test
    void createUserToDb() {
        User returnUser = userDbStorage.create(user);
        user.setId(1);
        assertEquals(user, returnUser);
    }

    @Test
    void findUserByIdDb() {
        userDbStorage.create(user);
        User returnUser = userDbStorage.findById(1);
        user.setId(1);
        assertEquals(user, returnUser);
    }

    @Test
    void updateUser() {
        userDbStorage.create(user);
        User returnUser = userDbStorage.update(updatedUser);
        assertEquals(updatedUser, returnUser);
    }

    @Test
    void findAllUsers() {
        userDbStorage.create(user);
        userDbStorage.create(updatedUser);
        List<User> users = new ArrayList<>();
        users.add(user);
        users.add(updatedUser);
        List<User> returnUsers = userDbStorage.findAll();

        assertEquals(2, returnUsers.size());
        assertArrayEquals(users.toArray(), returnUsers.toArray());
    }

    @Test
    void deleteUser() {
        userDbStorage.create(user);
        userDbStorage.deleteUser(1);
        List<User> returnUsers = userDbStorage.findAll();
        assertEquals(0, returnUsers.size());
    }

    @Test
    void filmCreate() {
        Film returnFilm = filmDbStorage.create(film);
        film.setId(1);
        assertEquals(film, returnFilm);
    }

    @Test
    void getFilmById() {
        filmDbStorage.create(film);
        Film returnFilm = filmDbStorage.getById(1);
        assertEquals(film.getName(), returnFilm.getName());
        assertEquals(film.getDescription(), returnFilm.getDescription());
        assertEquals(film.getMpa().getId(), returnFilm.getMpa().getId());
        assertEquals(film.getDuration(), returnFilm.getDuration());
        assertEquals(film.getReleaseDate(), returnFilm.getReleaseDate());
    }

    @Test
    void getAllFilms() {
        filmDbStorage.create(film);
        filmDbStorage.create(updatedFilm);
        List<Film> returnFilms = filmDbStorage.findAll();
        assertEquals(2, returnFilms.size());
        assertEquals(returnFilms.get(0).getName(), film.getName());
        assertEquals(returnFilms.get(1).getName(), updatedFilm.getName());
    }

    @Test
    void updateFilm() {
        filmDbStorage.create(film);
        filmDbStorage.update(updatedFilm);
        Film returnFilm = filmDbStorage.getById(1);
        assertEquals(updatedFilm.getName(), returnFilm.getName());
        assertEquals(updatedFilm.getDescription(), returnFilm.getDescription());
        assertEquals(updatedFilm.getMpa().getId(), returnFilm.getMpa().getId());
        assertEquals(updatedFilm.getDuration(), returnFilm.getDuration());
        assertEquals(updatedFilm.getReleaseDate(), returnFilm.getReleaseDate());
    }

    @Test
    void deleteFilm() {
        filmDbStorage.create(film);
        filmDbStorage.create(updatedFilm);
        filmDbStorage.deleteFilm(1);
        Film returnFilm = filmDbStorage.getById(1);
        assertNull(returnFilm);
    }

    @Test
    void createToFriendAndFindFriends() {
        userDbStorage.create(user);
        userDbStorage.create(updatedUser);
        friendDbStorage.addFriends(1, 2);
        List<User> returnFriends = friendDbStorage.getAllFriends(1);
        assertEquals(updatedUser, returnFriends.get(0));
        assertEquals(1, returnFriends.size());
    }

    @Test
    void deleteFromFriends() {
        userDbStorage.create(user);
        userDbStorage.create(updatedUser);
        friendDbStorage.addFriends(1, 2);
        friendDbStorage.deleteFriends(1, 2);
        List<User> returnFriends = friendDbStorage.getAllFriends(1);
        assertEquals(0, returnFriends.size());
    }

    @Test
    void getGenreById() {
        Genre returnGenre = genreDbStorage.getGenreById(1);
        assertEquals("Комедия", returnGenre.getName());
    }

    @Test
    void getAllGenres() {
        List<Genre> returnGenres = genreDbStorage.getAllGenre();
        assertEquals("Комедия", returnGenres.get(0).getName());
        assertEquals("Драма", returnGenres.get(1).getName());
        assertEquals("Мультфильм", returnGenres.get(2).getName());
        assertEquals("Триллер", returnGenres.get(3).getName());
        assertEquals("Документальный", returnGenres.get(4).getName());
        assertEquals("Боевик", returnGenres.get(5).getName());
    }

    @Test
    void createLikeAndGetPopular() {
        filmDbStorage.create(film);
        filmDbStorage.create(updatedFilm);
        userDbStorage.create(user);
        likeDbStorage.addLikeToFilm(2, 1);
        List<Film> returnFilms = filmDbStorage.getPopular(1);
        assertEquals(updatedFilm.getName(), returnFilms.get(0).getName());
        assertEquals(updatedFilm.getDuration(), returnFilms.get(0).getDuration());
        assertEquals(updatedFilm.getDescription(), returnFilms.get(0).getDescription());
    }

    @Test
    void getMpaById() {
        Mpa mpa = mpaDbStorage.getMpaById(1);
        assertEquals("G", mpa.getName());
    }

    @Test
    void getAllMpas() {
        List<Mpa> returnMpas = mpaDbStorage.getAllMpa();
        assertEquals("G", returnMpas.get(0).getName());
        assertEquals("PG", returnMpas.get(1).getName());
        assertEquals("PG-13", returnMpas.get(2).getName());
        assertEquals("R", returnMpas.get(3).getName());
        assertEquals("NC-17", returnMpas.get(4).getName());
    }
}
