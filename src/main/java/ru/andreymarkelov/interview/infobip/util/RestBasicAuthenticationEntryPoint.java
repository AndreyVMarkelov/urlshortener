package ru.andreymarkelov.interview.infobip.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Component
public class RestBasicAuthenticationEntryPoint implements AuthenticationEntryPoint {
    @Autowired
    private ObjectMapper objectMapper;

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType(APPLICATION_JSON_VALUE);
        Map<String, Object> responseBody = new HashMap<>();
        responseBody.put("success", false);
        responseBody.put("description", "Not authorized");
        PrintWriter writer = response.getWriter();
        writer.write(objectMapper.writeValueAsString(responseBody));
        writer.flush();
    }
}
