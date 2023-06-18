package com.yostoya.shoptill.exception;

import com.auth0.jwt.exceptions.InvalidClaimException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.yostoya.shoptill.domain.HttpResponse;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.LockedException;

import java.io.OutputStream;
import java.util.Map;

import static java.time.LocalDateTime.now;
import static org.springframework.http.HttpStatus.*;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Slf4j
public class ExceptionUtil {
    public static void processError(HttpServletResponse response,
                                    Exception exception) {

        if (exception instanceof ApiException ||
                exception instanceof BadCredentialsException ||
                exception instanceof InvalidClaimException) {

            var httpResponse = getHttpResponse(response, exception.getMessage(), BAD_REQUEST);
            writeResponse(response, httpResponse);

        } else if (exception instanceof TokenExpiredException) {

            var httpResponse = getHttpResponse(response, exception.getMessage(), UNAUTHORIZED);
            writeResponse(response, httpResponse);

        } else {
            var httpResponse = getHttpResponse(response, "An error occurred. Please try again.", INTERNAL_SERVER_ERROR);
            writeResponse(response, httpResponse);
        }
        log.error(exception.getMessage());
    }

    private static void writeResponse(HttpServletResponse response, HttpResponse httpResponse) {

        OutputStream out;
        try {

            out = response.getOutputStream();
            var mapper = new ObjectMapper();

            mapper.writeValue(out, httpResponse);
            out.flush();

        } catch (Exception exception) {
            log.error(exception.getMessage());
            exception.printStackTrace();
        }
    }

    private static HttpResponse getHttpResponse(HttpServletResponse response,
                                                String message,
                                                HttpStatus httpStatus
    ) {
        var httpResponse = HttpResponse.builder()
                .timestamp(now().toString())
                .message(message)
                .status(httpStatus)
                .statusCode(httpStatus.value())
                .build();

        response.setContentType(APPLICATION_JSON_VALUE);
        response.setStatus(httpStatus.value());

        return httpResponse;
    }
}
