package ru.andreymarkelov.interview.infobip.rest;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.hibernate.validator.constraints.URL;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.andreymarkelov.interview.infobip.domain.Url;
import ru.andreymarkelov.interview.infobip.service.UrlService;

import javax.validation.Valid;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Controller
@RequestMapping(path = "/register")
public class RegisterController {
    @Autowired
    private UrlService urlService;

    @PostMapping(consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<Map> registerUrl(@Valid @RequestBody RegisterRequest registerRequest, @AuthenticationPrincipal UserDetails userDetails) {
        if (registerRequest.redirectType == null) {
            registerRequest.redirectType = 301;
        }
        Url url = urlService.createUrl(userDetails.getUsername(), registerRequest.url, registerRequest.redirectType);
        return new ResponseEntity<>(Collections.singletonMap("shortUrl", url.getShortUrl()), HttpStatus.OK);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map> badRequestHandler(Exception ex) {
        Map<String, Object> responseBody = new HashMap<>();
        responseBody.put("success", false);
        responseBody.put("description", "Invalid payload");
        return new ResponseEntity<>(responseBody, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map> internalErrorHandler(Exception ex) {
        Map<String, Object> responseBody = new HashMap<>();
        responseBody.put("success", false);
        responseBody.put("description", "Internal Server Error");
        return new ResponseEntity<>(responseBody, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    private static class RegisterRequest {
        @URL
        @JsonProperty(value = "url", required = true)
        private String url;
        @JsonProperty(value = "redirectType", required = false)
        private Integer redirectType;
    }
}
