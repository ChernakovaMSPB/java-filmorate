package rru.yandex.practicum.filmorate.service;

import org.springframework.stereotype.Service;
import rru.yandex.practicum.filmorate.model.Mpa;
import rru.yandex.practicum.filmorate.storage.MpaStorage;

import java.util.List;

@Service
public class MpaService {
    private final MpaStorage mpaStorage;

    public MpaService(MpaStorage mpaStorage) {
        this.mpaStorage = mpaStorage;
    }

    public Mpa getMpaById(int id) {
        Mpa mpa = mpaStorage.getMpaById(id);
        if (mpa == null) {
            throw new RuntimeException("Mpa по " + id + " не найдено");
        }
        return mpa;
    }

    public List<Mpa> getAllMpa() {
        return mpaStorage.getAllMpa();
    }
}
