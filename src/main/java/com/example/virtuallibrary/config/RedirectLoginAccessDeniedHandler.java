package com.example.virtuallibrary.config;

import java.io.IOException;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.web.servlet.FlashMap;
import org.springframework.web.servlet.FlashMapManager;
import org.springframework.web.servlet.support.RequestContextUtils;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class RedirectLoginAccessDeniedHandler implements AccessDeniedHandler {

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response,
            AccessDeniedException accessDeniedException) throws IOException, ServletException {
                // Access the FlashMapManager for the current request
                FlashMapManager flashMapManager = RequestContextUtils.getFlashMapManager(request);
                FlashMap flashMap = RequestContextUtils.getOutputFlashMap(request);

                // Add the message as a flash attribute
                flashMap.put("message", "Permission required for this resource.");

                // Save the FlashMap before redirecting
                flashMapManager.saveOutputFlashMap(flashMap, request, response);
                response.sendRedirect("/login");
    }
}