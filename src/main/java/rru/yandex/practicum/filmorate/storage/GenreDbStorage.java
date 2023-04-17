package rru.yandex.practicum.filmorate.storage;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import rru.yandex.practicum.filmorate.model.Genre;

import java.util.ArrayList;
import java.util.List;

@Component
public class GenreDbStorage implements GenreStorage {
    private final JdbcTemplate jdbcTemplate;

    public GenreDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Genre getGenreById(int id) {
        String sqlQuery = "select * from genre where genre_id = ?";
        SqlRowSet rowSet = jdbcTemplate.queryForRowSet(sqlQuery, id);
        Genre genre = null;
        if (rowSet.next()) {
            genre = new Genre(rowSet.getInt("genre_id"), rowSet.getString("name"));
        }
        return genre;
    }

    @Override
    public List<Genre> getAllGenre() {
        List<Genre> genres = new ArrayList<>();
        String sqlQuery = "select * from genre";
        SqlRowSet rowSet = jdbcTemplate.queryForRowSet(sqlQuery);
        while (rowSet.next()) {
            Genre genre = new Genre(rowSet.getInt("genre_id"), rowSet.getString("name"));
            genres.add(genre);
        }
        return genres;
    }
}

