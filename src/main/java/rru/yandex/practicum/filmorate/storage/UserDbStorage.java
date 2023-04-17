package rru.yandex.practicum.filmorate.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import rru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Component("UserDbStorage")
@Slf4j
public class UserDbStorage implements UserStorage {
    private final JdbcTemplate jdbcTemplate;

    public UserDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public User create(User user) {
        String sqlQuery = "insert into users (email, login, name, birthday) values (?, ?, ?, ?)";
        jdbcTemplate.update(sqlQuery,
                user.getEmail(),
                user.getLogin(),
                user.getName(),
                user.getBirthday()
        );
        String sqlQueryForLastId = "select user_id from users order by user_id desc limit 1";
        SqlRowSet rowSet = jdbcTemplate.queryForRowSet(sqlQueryForLastId);
        if (rowSet.next()) {
            user.setId(rowSet.getLong("user_id"));
            return user;
        } else {
            return null;
        }
    }

    @Override
    public User findById(long id) {
        String sqlQuery = "select * from users where user_id = ?";
        SqlRowSet rowSet = jdbcTemplate.queryForRowSet(sqlQuery, id);
        log.info("SQL Query for user id:" + id);
        if (rowSet.next()) {
            LocalDate birthday = dateFormatter(rowSet.getString("birthday"));
            return new User(rowSet.getLong("user_id"),
                    rowSet.getString("email"),
                    rowSet.getString("login"),
                    rowSet.getString("name"),
                    birthday);
        } else {
            return null;
        }
    }

    @Override
    public List<User> findAll() {
        List<User> users = new ArrayList<>();
        String sqlQuery = "select * from users";
        SqlRowSet rowSet = jdbcTemplate.queryForRowSet(sqlQuery);
        while (rowSet.next()) {
            LocalDate birthday = dateFormatter(rowSet.getString("birthday"));
            users.add(new User(rowSet.getLong("user_id"),
                    rowSet.getString("email"),
                    rowSet.getString("login"),
                    rowSet.getString("name"),
                    birthday));
        }
        return users;
    }

    @Override
    public User update(User user) {
        String sqlQuery = "update users set email = ?, login = ?, name = ?, birthday = ? where user_id = ?";
        jdbcTemplate.update(sqlQuery,
                user.getEmail(),
                user.getLogin(),
                user.getName(),
                user.getBirthday(),
                user.getId());
        return user;
    }

    @Override
    public void deleteUser(long id) {
        String sqlQuery = "delete from users where user_id = ?";
        jdbcTemplate.update(sqlQuery, id);
    }

    private LocalDate dateFormatter(String date) {
        String[] parts = new String[3];
        parts = date.split("-");
        LocalDate localDate = LocalDate.of(Integer.parseInt(parts[0]),
                Integer.parseInt(parts[1]),
                Integer.parseInt(parts[2]));
        return localDate;
    }
}

