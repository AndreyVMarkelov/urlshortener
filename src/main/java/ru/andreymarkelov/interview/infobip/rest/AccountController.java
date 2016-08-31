package ru.andreymarkelov.interview.infobip.rest;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.apache.commons.lang3.RandomStringUtils;
import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.andreymarkelov.interview.infobip.service.AccountService;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.Map;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static ru.andreymarkelov.interview.infobip.util.DatabaseUtil.isDatabaseConstraintViolationException;

@Controller
@RequestMapping(path = "/account")
public class AccountController {
    @Autowired
    private AccountService accountService;

    @PostMapping(consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<Map> openingAccount(@Valid @RequestBody AccountRequest accountRequest) {
        String password = RandomStringUtils.randomAlphanumeric(8);
        accountService.createAccount(accountRequest.accountId, password);

        Map<String, Object> responseBody = new HashMap<>();
        responseBody.put("success", true);
        responseBody.put("password", password);
        responseBody.put("description", "Your account is opened");
        return new ResponseEntity<>(responseBody, HttpStatus.OK);
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
        if (isDatabaseConstraintViolationException(ex)) { // check unique constraint violation
            responseBody.put("description", "Account already exist");
            return new ResponseEntity<>(responseBody, HttpStatus.CONFLICT);
        } else {
            responseBody.put("description", "Internal Server Error");
            return new ResponseEntity<>(responseBody, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    private static class AccountRequest {
        @NotEmpty
        @JsonProperty(value = "AccountId", required = true)
        private String accountId;
    }
}
