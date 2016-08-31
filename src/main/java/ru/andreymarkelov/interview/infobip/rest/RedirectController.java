package ru.andreymarkelov.interview.infobip.rest;

import org.apache.catalina.servlet4preview.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.andreymarkelov.interview.infobip.domain.Url;
import ru.andreymarkelov.interview.infobip.service.UrlService;
import ru.andreymarkelov.interview.infobip.util.UrlNotFoundException;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping(path = "/r")
public class RedirectController {
    @Autowired
    private UrlService urlService;

    @GetMapping(path = "/**")
    public void redirectUrl(HttpServletRequest request, HttpServletResponse response) throws IOException {
        Url targetUrl = urlService.getAndUpdateCount(request.getRequestURL().toString());
        response.setStatus(targetUrl.getRedirectCode());
        response.sendRedirect(targetUrl.getOriginUrl());
    }

    @ExceptionHandler(UrlNotFoundException.class)
    public ResponseEntity<Map> urlNotFoundHandler(Exception ex) {
        Map<String, Object> responseBody = new HashMap<>();
        responseBody.put("success", false);
        responseBody.put("description", "URL not found");
        return new ResponseEntity<>(responseBody, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map> internalErrorHandler(Exception ex) {
        Map<String, Object> responseBody = new HashMap<>();
        responseBody.put("success", false);
        responseBody.put("description", "Internal Server Error");
        return new ResponseEntity<>(responseBody, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
