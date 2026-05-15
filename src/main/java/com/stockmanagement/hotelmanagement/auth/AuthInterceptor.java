package com.stockmanagement.hotelmanagement.auth;

import org.springframework.web.servlet.HandlerInterceptor;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

public class AuthInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {

        String requestURI = request.getRequestURI();

        // Allow public paths (login, register, home, etc.)
        if (isPublicPath(requestURI)) {
            return true;
        }

        // Check if user is authenticated
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("userId") == null) {
            // Redirect to login for unauthenticated users
            response.sendRedirect("/auth/login");
            return false;
        }

        // Check user role and redirect if accessing admin-only pages
        Object userRole = session.getAttribute("userRole");

        // Admin-only paths
        if (isAdminOnlyPath(requestURI)) {
            if (!"ADMIN".equals(userRole)) {
                response.sendRedirect("/access-denied");
                return false;
            }
        }

        // Guest-only paths
        if (isGuestOnlyPath(requestURI)) {
            if (!"GUEST".equals(userRole)) {
                response.sendRedirect("/dashboard");
                return false;
            }
        }

        return true;
    }

    private boolean isPublicPath(String uri) {
        // Explicitly list all public paths
        if (uri.startsWith("/auth/")) return true;
        if (uri.equals("/home")) return true;
        if (uri.equals("/")) return true;
        if (uri.equals("/access-denied")) return true;
        if (uri.equals("/book-room")) return true;
        if (uri.startsWith("/book-room/")) return true;
        if (uri.equals("/bookings")) return true;
        if (uri.startsWith("/bookings/")) return true;
        if (uri.startsWith("/images/")) return true;
        if (uri.startsWith("/css/")) return true;
        if (uri.startsWith("/js/")) return true;
        if (uri.startsWith("/webjars/")) return true;
        if (uri.endsWith(".css")) return true;
        if (uri.endsWith(".js")) return true;
        if (uri.endsWith(".png")) return true;
        if (uri.endsWith(".jpg")) return true;
        if (uri.endsWith(".jpeg")) return true;
        if (uri.endsWith(".gif")) return true;
        if (uri.endsWith(".ico")) return true;
        if (uri.startsWith("/api/debug/")) return true; // Debug endpoints
        // Guest API endpoints for room booking
        if (uri.startsWith("/api/rooms/available-between")) return true;
        if (uri.startsWith("/api/bookings") && uri.contains("guest")) return true;

        return false;
    }

    private boolean isAdminOnlyPath(String uri) {
        // Admin-only paths that guests cannot access
        return uri.equals("/dashboard") ||
                uri.startsWith("/dashboard/") ||
                uri.equals("/staff") ||
                uri.startsWith("/staff/") ||
                uri.equals("/services") ||
                uri.startsWith("/services/") ||
                uri.equals("/rooms") ||
                uri.startsWith("/rooms/") ||
                uri.equals("/guests") ||
                uri.startsWith("/guests/") ||
                uri.equals("/invoices") ||
                uri.startsWith("/invoices/") ||
                // Admin API endpoints
                uri.startsWith("/api/guests/") ||
                uri.equals("/api/guests") ||
                uri.startsWith("/api/staff/") ||
                uri.startsWith("/api/services/") ||
                (uri.startsWith("/api/rooms/") && !uri.contains("available-between")) ||
                uri.startsWith("/api/dashboard/");
    }

    private boolean isGuestOnlyPath(String uri) {
        return uri.equals("/guest-dashboard") ||
                uri.startsWith("/guest-dashboard/") ||
                uri.equals("/book-room") ||
                uri.startsWith("/book-room/") ||
                uri.equals("/bookings") ||
                uri.startsWith("/bookings/") ||
                uri.equals("/guest-invoices") ||
                uri.startsWith("/guest-invoices/");
    }
}
