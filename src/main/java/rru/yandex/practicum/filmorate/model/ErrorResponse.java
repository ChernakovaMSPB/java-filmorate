package rru.yandex.practicum.filmorate.model;

import org.springframework.http.HttpStatus;

public class ErrorResponse {
    private final String error;

    private final HttpStatus status;

    public ErrorResponse(HttpStatus status, String error) {
        this.status = status;
        this.error = error;
    }

    public String getError() {
        return error;
    }

    public HttpStatus getStatus() {
        return status;
    }
}
