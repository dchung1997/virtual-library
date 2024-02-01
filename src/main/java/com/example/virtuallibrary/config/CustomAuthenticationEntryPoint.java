package com.example.virtuallibrary.config;

import java.io.IOException;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.web.servlet.FlashMap;
import org.springframework.web.servlet.FlashMapManager;
import org.springframework.web.servlet.support.RequestContextUtils;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response,
                         AuthenticationException authException) throws IOException, ServletException {
                FlashMapManager flashMapManager = RequestContextUtils.getFlashMapManager(request);
                FlashMap flashMap = RequestContextUtils.getOutputFlashMap(request);

                // Add the message as a flash attribute
                flashMap.put("message", "Please sign in to access.");

                // Save the FlashMap before redirecting
                flashMapManager.saveOutputFlashMap(flashMap, request, response);

                // Save the FlashMap before redirecting
                response.sendRedirect("/login");
    }
}