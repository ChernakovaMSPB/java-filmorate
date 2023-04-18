package rru.yandex.practicum.filmorate.storage;

import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;
import rru.yandex.practicum.filmorate.model.Mpa;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;

@Primary
@Repository
public class MpaDbStorage implements MpaStorage{
    private final JdbcTemplate jdbcTemplate;

    public MpaDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Mpa getMpaById(int id) {
        String sqlQuery = "select * from mpa where rating_id = ?";
        SqlRowSet rowSet = jdbcTemplate.queryForRowSet(sqlQuery, id);
        Mpa mpa = null;
        if (rowSet.next()) {
            mpa = new Mpa(rowSet.getInt("rating_id"), rowSet.getString("name"));
        }
        return mpa;
    }

    @Override
    public List<Mpa> getAllMpa() {
        List<Mpa> mpas = new ArrayList<>();
        String sqlQuery = "select * from mpa";
        SqlRowSet rowSet = jdbcTemplate.queryForRowSet(sqlQuery);
        while (rowSet.next()) {
            Mpa mpa = new Mpa(rowSet.getInt("rating_id"), rowSet.getString("name"));
            mpas.add(mpa);
        }
        return mpas;
    }
}