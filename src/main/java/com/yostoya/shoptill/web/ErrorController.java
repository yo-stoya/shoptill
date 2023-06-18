package com.yostoya.shoptill.web;

import com.yostoya.shoptill.domain.HttpResponse;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

import static org.springframework.http.HttpStatus.NOT_FOUND;

@RestController
public class ErrorController {

    @RequestMapping("/error")
    public ResponseEntity<HttpResponse> handleError(HttpServletRequest request) {
        return new ResponseEntity<>(new HttpResponse(NOT_FOUND,
                "No mapping found for " + request.getMethod() + "request on the server", Map.of()), NOT_FOUND);
    }
}
