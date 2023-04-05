package rru.yandex.practicum.filmorate.exceptions;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class FilmNotFoundException extends IllegalArgumentException {
    public FilmNotFoundException(String message) {
        log.error(message);
    }
}