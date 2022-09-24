package fr.cpe.filmforyou.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class FilmForYouException extends RuntimeException {

    private final HttpStatus httpStatus;

    public FilmForYouException(String message, HttpStatus httpStatus) {
        super(message);
        this.httpStatus = httpStatus;
    }

    public FilmForYouException(String message) {
        super(message);
        this.httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
    }

}
