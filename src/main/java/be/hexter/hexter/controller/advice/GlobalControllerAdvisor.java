package be.patryksitko.contest.ip2location.com.controller.advice;

import java.util.List;

import org.apache.commons.lang3.NotImplementedException;
import org.json.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;

import com.fasterxml.jackson.databind.exc.InvalidFormatException;

import be.patryksitko.contest.ip2location.com.other.builder.ResponseBuilder;
import be.patryksitko.contest.ip2location.com.other.builder.ResponseBuilder.ResponseType;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestControllerAdvice
public class GlobalControllerAdvisor {

    @ExceptionHandler(InvalidFormatException.class)
    public ResponseEntity<Object> handleHttpMessageNotReadableException(RuntimeException ex, WebRequest request) {
        log.info(ex.getMessage());
        final String uri = ((ServletWebRequest) request).getRequest().getRequestURI().toString();
        switch (uri) {
            default:
                log.error(new NotImplementedException(
                        String.format("Switch case: \"%s\". Case is not implemented in class: \"%s\".", uri,
                                this.getClass()))
                        .toString());
                break;
            case "/api/user/validate-authentication-token":
                final HttpStatus httpStatus = HttpStatus.UNPROCESSABLE_ENTITY;
                final JSONObject responseBody = new JSONObject();
                responseBody.put("isProvidedAuthenticationTokenValid", false);
                return ResponseEntity.status(httpStatus).body(ResponseBuilder.builder().status(httpStatus)
                        .responseType(ResponseType.ERROR).errors(List.of(ex.getMessage())).body(responseBody.toString())
                        .build());
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }
}
