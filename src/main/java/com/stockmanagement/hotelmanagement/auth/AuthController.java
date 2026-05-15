package com.stockmanagement.hotelmanagement.auth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import jakarta.servlet.http.HttpSession;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@Controller
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    // Show login page
    @GetMapping("/login")
    public String showLoginPage() {
        return "login";
    }

    // Show admin login page
    @GetMapping("/admin-login")
    public String showAdminLoginPage() {
        return "admin-login";
    }

    // Show guest registration page
    @GetMapping("/register")
    public String showRegisterPage() {
        return "register";
    }

    // Handle guest login
    @PostMapping("/login")
    public String handleLogin(@RequestParam String email,
                              @RequestParam String password,
                              HttpSession session,
                              Model model) {
        try {
            User user = authService.login(email, password);

            if ("ADMIN".equals(user.getRole())) {
                session.setAttribute("userId", user.getId());
                session.setAttribute("userEmail", user.getEmail());
                session.setAttribute("userRole", user.getRole());
                session.setAttribute("userName", user.getFullName());
                return "redirect:/dashboard";
            } else if ("GUEST".equals(user.getRole())) {
                session.setAttribute("userId", user.getId());
                session.setAttribute("userEmail", user.getEmail());
                session.setAttribute("userRole", user.getRole());
                session.setAttribute("userName", user.getFullName());
                return "redirect:/guest-dashboard";
            } else {
                String error = URLEncoder.encode("Invalid user role", StandardCharsets.UTF_8);
                return "redirect:/auth/login?error=" + error;
            }
        } catch (Exception e) {
            String error = URLEncoder.encode(e.getMessage(), StandardCharsets.UTF_8);
            return "redirect:/auth/login?error=" + error;
        }
    }

    // Handle admin login
    @PostMapping("/admin-login")
    public String handleAdminLogin(@RequestParam String email,
                                   @RequestParam String password,
                                   HttpSession session,
                                   Model model) {
        try {
            User user = authService.login(email, password);

            if (!"ADMIN".equals(user.getRole())) {
                String error = URLEncoder.encode("Only admins can access this page", StandardCharsets.UTF_8);
                return "redirect:/auth/admin-login?error=" + error;
            }

            session.setAttribute("userId", user.getId());
            session.setAttribute("userEmail", user.getEmail());
            session.setAttribute("userRole", user.getRole());
            session.setAttribute("userName", user.getFullName());
            return "redirect:/dashboard";
        } catch (Exception e) {
            String error = URLEncoder.encode(e.getMessage(), StandardCharsets.UTF_8);
            return "redirect:/auth/admin-login?error=" + error;
        }
    }

    // Handle guest registration
    @PostMapping("/register")
    public String handleRegister(@RequestParam String email,
                                 @RequestParam String fullName,
                                 @RequestParam String phoneNumber,
                                 @RequestParam String password,
                                 @RequestParam String confirmPassword,
                                 HttpSession session,
                                 Model model) {
        try {
            if (!password.equals(confirmPassword)) {
                String error = URLEncoder.encode("Passwords do not match", StandardCharsets.UTF_8);
                return "redirect:/auth/register?error=" + error;
            }

            User user = authService.registerGuest(email, fullName, phoneNumber, password);

            session.setAttribute("userId", user.getId());
            session.setAttribute("userEmail", user.getEmail());
            session.setAttribute("userRole", user.getRole());
            session.setAttribute("userName", user.getFullName());

            return "redirect:/guest-dashboard";
        } catch (Exception e) {
            String error = URLEncoder.encode(e.getMessage(), StandardCharsets.UTF_8);
            return "redirect:/auth/register?error=" + error;
        }
    }

    // Logout
    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/auth/login";
    }
}




