package rru.yandex.practicum.filmorate.storage;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import rru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Component
public class FriendDbStorage implements FriendStorage {
    private final JdbcTemplate jdbcTemplate;

    public FriendDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void addFriends(long userId, long friendId) {
        String sqlQuery = "insert into friends (user_id, friend_id, status) values (?, ?, ?)";
        jdbcTemplate.update(sqlQuery,
                userId,
                friendId,
                "unconfirmed");
    }

    @Override
    public void deleteFriends(long userId, long friendId) {
        String sqlQuery = "delete from friends where user_id = ? and friend_id = ?";
        jdbcTemplate.update(sqlQuery,
                userId,
                friendId);
    }

    @Override
    public List<User> getAllFriends(long userId) {
        List<User> friends = new ArrayList<>();
        String sqlQuery = "select us.* " +
                "from friends as fr " +
                "join users as us on fr.friend_id=us.user_id " +
                "where fr.user_id = ?";
        SqlRowSet rowSet = jdbcTemplate.queryForRowSet(sqlQuery, userId);
        while (rowSet.next()) {
            LocalDate birthday = dateFormatter(rowSet.getString("birthday"));
            User friend = new User(rowSet.getLong("user_id"),
                    rowSet.getString("email"),
                    rowSet.getString("login"),
                    rowSet.getString("name"),
                    birthday);
            friends.add(friend);
        }
        return friends;
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

