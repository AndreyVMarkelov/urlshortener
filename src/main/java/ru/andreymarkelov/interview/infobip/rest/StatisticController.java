package ru.andreymarkelov.interview.infobip.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import ru.andreymarkelov.interview.infobip.service.UrlService;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Controller
@RequestMapping(path = "/statistic")
public class StatisticController {
    @Autowired
    private UrlService urlService;

    @GetMapping(produces = APPLICATION_JSON_VALUE)
    public @ResponseBody Map<String, Integer> statistics(@AuthenticationPrincipal UserDetails userDetails) {
        return urlService.getUrlsByAccountId(userDetails.getUsername()).stream().collect(Collectors.toMap(k -> k.getOriginUrl(), v -> v.getCount()));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map> internalErrorHandler(Exception ex) {
        Map<String, Object> responseBody = new HashMap<>();
        responseBody.put("success", false);
        responseBody.put("description", "Internal Server Error");
        return new ResponseEntity<>(responseBody, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
