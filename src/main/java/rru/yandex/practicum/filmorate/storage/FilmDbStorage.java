package rru.yandex.practicum.filmorate.storage;

import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import rru.yandex.practicum.filmorate.model.Film;
import rru.yandex.practicum.filmorate.model.Genre;
import rru.yandex.practicum.filmorate.model.Mpa;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.*;

@Component("FilmDbStorage")
public class FilmDbStorage implements FilmStorage {
    private final JdbcTemplate jdbcTemplate;

    public FilmDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Film create(Film film) {
        String querySql = "insert into films (name, description, release_date, duration, rating_id) " +
                "values (?, ?, ?, ?, ?)";
        int ratingId = film.getMpa().getId();
        film.setGenres(getUniqueGenres(film.getGenres()));

        jdbcTemplate.update(querySql,
                film.getName(),
                film.getDescription(),
                film.getReleaseDate(),
                film.getDuration(),
                ratingId);

        String queryForLastIdSql = "select film_id from films order by film_id desc limit 1";
        SqlRowSet rowSet = jdbcTemplate.queryForRowSet(queryForLastIdSql);
        if (rowSet.next()) {
            film.setId(rowSet.getLong("film_id"));
            if (film.getGenres().size() > 0) {
                String genreQuerySql = "insert into film_genre (film_id, genre_id) " +
                        "values (?, ?)";

                jdbcTemplate.batchUpdate(genreQuerySql, new BatchPreparedStatementSetter() {
                    @Override
                    public void setValues(PreparedStatement ps, int i) throws SQLException {
                        ps.setString(1, String.valueOf(film.getId()));
                        ps.setString(2, String.valueOf(film.getGenres().get(i).getId()));
                    }

                    @Override
                    public int getBatchSize() {
                        return film.getGenres().size();
                    }
                });
            }
            return film;
        } else {
            return null;
        }
    }

    @Override
    public Film getById(long id) {
        Film film = null;
        String querySql = "select * " +
                "from films as f " +
                "join mpa as m on f.rating_id = m.rating_id " +
                "left join film_genre as fg on f.film_id = fg.film_id " +
                "left join genre as g on fg.genre_id = g.genre_id " +
                "where f.film_id = ?";
        SqlRowSet rowSet = jdbcTemplate.queryForRowSet(querySql, id);
        while (rowSet.next()) {
            Genre genre = new Genre(rowSet.getInt("genre_id"),
                    rowSet.getString("genre_name"));
            if (film == null) {
                Mpa mpa = new Mpa(rowSet.getInt("rating_id"), rowSet.getString("mpa_name"));
                film = new Film(rowSet.getInt("film_id"),
                        rowSet.getString("name"),
                        rowSet.getString("description"),
                        dateFormatter(rowSet.getString("release_date")),
                        rowSet.getInt("duration"),
                        mpa);
                if (genre.getId() != 0)
                    film.createGenre(genre);
            } else {
                film.createGenre(genre);
            }
        }
        return film;
    }


    @Override
    public List<Film> findAll() {
        Map<Long, ArrayList<Genre>> filmGenres = getGenresMap();

        List<Film> filmList = new ArrayList<>();
        String querySql = "select * " +
                "from films as f " +
                "join mpa as m on f.rating_id=m.rating_id ";
        SqlRowSet rowSet = jdbcTemplate.queryForRowSet(querySql);
        while (rowSet.next()) {
            Mpa mpa = new Mpa(rowSet.getInt("rating_id"),
                    rowSet.getString("mpa_name"));
            long filmId = rowSet.getLong("film_id");
            Film film = new Film(filmId,
                    rowSet.getString("name"),
                    rowSet.getString("description"),
                    dateFormatter(rowSet.getString("release_date")),
                    rowSet.getInt("duration"),
                    mpa);
            if (filmGenres.containsKey(filmId)) {
                film.setGenres(filmGenres.get(filmId));
            }
            filmList.add(film);
        }
        return filmList;
    }


    @Override
    public Film update(Film film) {
        String querySql = "update films set " +
                "name = ?, description = ?, release_date = ?, duration = ?, rating_id = ? " +
                "where film_id = ?";

        film.setGenres(getUniqueGenres(film.getGenres()));

        int mpaId = film.getMpa().getId();

        jdbcTemplate.update(querySql,
                film.getName(),
                film.getDescription(),
                film.getReleaseDate(),
                film.getDuration(),
                mpaId,
                film.getId());

        String genreRemoveQuerySql = "delete from film_genre where film_id = ?";
        jdbcTemplate.update(genreRemoveQuerySql, film.getId());

        if (film.getGenres().size() > 0) {
            String genreQuerySql = "insert into film_genre (film_id, genre_id) " +
                    "values (?, ?)";

            jdbcTemplate.batchUpdate(genreQuerySql, new BatchPreparedStatementSetter() {
                @Override
                public void setValues(PreparedStatement ps, int i) throws SQLException {
                    ps.setString(1, String.valueOf(film.getId()));
                    ps.setString(2, String.valueOf(film.getGenres().get(i).getId()));
                }

                @Override
                public int getBatchSize() {
                    return film.getGenres().size();
                }
            });
        }

        return film;
    }


    @Override
    public void deleteFilm(long id) {
        String querySql = "delete from films where film_id = ?";
        jdbcTemplate.update(querySql, id);
    }


    @Override
    public List<Film> getPopular(int count) {
        Map<Long, ArrayList<Genre>> filmGenres = getGenresMap();

        String querySql = "select f.*, count(fl.user_id) AS likes, mpa.MPA_NAME " +
                "from films as f " +
                "left join film_likes as fl on f.film_id=fl.film_id " +
                "left join mpa as mpa on f.rating_id=mpa.rating_id " +
                "group by f.film_id " +
                "order by likes desc  " +
                "limit ?";
        SqlRowSet rowSet = jdbcTemplate.queryForRowSet(querySql, count);
        List<Film> filmRating = new ArrayList<>();
        while (rowSet.next()) {
            long filmId = rowSet.getLong("film_id");
            Film film = new Film(filmId,
                    rowSet.getString("name"),
                    rowSet.getString("description"),
                    dateFormatter(rowSet.getString("release_date")),
                    rowSet.getInt("duration"),
                    new Mpa(rowSet.getInt("rating_id"), rowSet.getString("mpa_name"))
            );
            if (filmGenres.containsKey(filmId)) {
                film.setGenres(filmGenres.get(filmId));
            }
            filmRating.add(film);
        }
        return filmRating;
    }

    private LocalDate dateFormatter(String date) {
        String[] parts = new String[3];
        parts = date.split("-");
        LocalDate localDate = LocalDate.of(Integer.parseInt(parts[0]),
                Integer.parseInt(parts[1]),
                Integer.parseInt(parts[2]));
        return localDate;
    }

    private List<Genre> getUniqueGenres(List<Genre> genres) {
        Set<Genre> uniqueGenres = new LinkedHashSet<>();
        for (Genre genre : genres) {
            if (!uniqueGenres.contains(genre)) {
                uniqueGenres.add(genre);
            }
        }
        return new ArrayList<>(uniqueGenres) {
        };
    }

    private Map<Long, ArrayList<Genre>> getGenresMap() {
        Map<Long, ArrayList<Genre>> filmGenres = new HashMap<>();
        String genresQuerySql = "select * " +
                "from film_genre as fg " +
                "join genre as g on fg.genre_id = g.genre_id ";
        SqlRowSet genresRowSet = jdbcTemplate.queryForRowSet(genresQuerySql);
        while (genresRowSet.next()) {
            long filmId = genresRowSet.getLong("film_id");
            Genre genre = new Genre(genresRowSet.getInt("genre_id"),
                    genresRowSet.getString("genre_name"));
            if (!filmGenres.containsKey(filmId)) {
                ArrayList<Genre> newGenreArrayList = new ArrayList<>();
                newGenreArrayList.add(genre);
                filmGenres.put(filmId, newGenreArrayList);
            } else {
                ArrayList<Genre> genreList = filmGenres.get(filmId);
                genreList.add(genre);
                filmGenres.remove(filmId);
                filmGenres.put(filmId, genreList);
            }
        }
        return filmGenres;
    }
}
