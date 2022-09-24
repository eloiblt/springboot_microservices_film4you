package fr.cpe.filmforyou.config;

import fr.cpe.filmforyou.exception.FilmForYouException;
import fr.cpe.filmforyou.model.ErrorDetails;
import org.springframework.context.annotation.Primary;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.time.LocalDateTime;
import java.util.stream.Collectors;

@Primary
@ControllerAdvice
public class RestExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(FilmForYouException.class)
    public ResponseEntity<ErrorDetails> filmForYouHandler(FilmForYouException ex, WebRequest webRequest) {
        ex.printStackTrace();
        String requestPath = ((ServletWebRequest) webRequest).getRequest().getRequestURI();
        ErrorDetails errorDetails = new ErrorDetails(LocalDateTime.now(), ex.getLocalizedMessage(), requestPath, ex.getClass().getName());
        return ResponseEntity.status(ex.getHttpStatus()).body(errorDetails);
    }


    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        ex.printStackTrace();

        String messageError = ex.getBindingResult().getAllErrors().stream().map(DefaultMessageSourceResolvable::getDefaultMessage).collect(Collectors.joining("\n"));
        String requestPath = ((ServletWebRequest) request).getRequest().getRequestURI();

        ErrorDetails errorDetails = new ErrorDetails(LocalDateTime.now(), messageError, requestPath, ex.getClass().getName());

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorDetails);
    }
}
