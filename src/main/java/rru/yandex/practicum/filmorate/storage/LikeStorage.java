package rru.yandex.practicum.filmorate.storage;

public interface LikeStorage {
    void addLikeToFilm(long filmId, long userId);
    void deleteLikeFromFilm(long filmId, long userId);
}
