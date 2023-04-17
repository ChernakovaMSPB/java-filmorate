package rru.yandex.practicum.filmorate.storage;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

@Component
public class LikeDbStorage implements LikeStorage {
    private final JdbcTemplate jdbcTemplate;

    public LikeDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void addLikeToFilm(long filmId, long userId) {
        String sqlQuery = "insert into film_likes (user_id, film_id) " +
                "values (?, ?)";
        jdbcTemplate.update(sqlQuery, userId, filmId);
    }

    @Override
    public void deleteLikeFromFilm(long filmId, long userId) {
        String sqlQuery = "delete from film_likes where user_id = ? and film_id = ?";
        jdbcTemplate.update(sqlQuery, userId, filmId);
    }
}

