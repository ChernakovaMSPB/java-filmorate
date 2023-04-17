package rru.yandex.practicum.filmorate.storage;

import rru.yandex.practicum.filmorate.model.Mpa;

import java.util.List;

public interface MpaStorage {
    Mpa getMpaById(int id);
    List<Mpa> getAllMpa();
}
