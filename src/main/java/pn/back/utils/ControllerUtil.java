package pn.back.utils;

import jakarta.annotation.Nullable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import pn.back.entities.Message;
import pn.back.errors.AppError;

import java.util.Optional;

public class ControllerUtil {

    public static ResponseEntity<?> getResponseEntity(long id,
                                                      Optional<Message> optionalMessage,
                                                      HttpStatus status, @Nullable String additionalMessage) {
        if (optionalMessage.isPresent())
            return ResponseEntity.ok(optionalMessage.orElseThrow());
        String errorDescribtion = "Request  with id " + id + " not not correct ";
        if (additionalMessage != null) errorDescribtion = errorDescribtion + additionalMessage;
        return new ResponseEntity<>(new AppError(status.value(),
                errorDescribtion),
                status);
    }

    public static ResponseEntity<?> getResponseEntity(long id,
                                                      Optional<Message> optionalMessage,
                                                      HttpStatus status) {
        if (optionalMessage.isPresent())
            return ResponseEntity.ok(optionalMessage.orElseThrow());
        String errorDescribtion = "Request  with id " + id + " not not correct ";

        return new ResponseEntity<>(new AppError(status.value(),
                errorDescribtion),
                status);
    }
}
