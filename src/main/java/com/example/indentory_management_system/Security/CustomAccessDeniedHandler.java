package com.example.indentory_management_system.Security;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.MediaType;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class CustomAccessDeniedHandler implements AccessDeniedHandler {

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) 
            throws IOException, ServletException {
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setStatus(HttpServletResponse.SC_FORBIDDEN);

        String message = accessDeniedException.getMessage() != null ? accessDeniedException.getMessage() : "You do not have permission to access this resource";
        message = message.replace("\"", "\\\"");

        String json = String.format("{\"status\":%d,\"error\":\"Forbidden\",\"message\":\"%s\",\"path\":\"%s\"}",
                HttpServletResponse.SC_FORBIDDEN,
                message,
                request.getServletPath());

        response.getWriter().write(json);
    }
}
