package com.demo.personalfinancetracker.controller;

import com.demo.personalfinancetracker.dto.UserDTO;
import com.demo.personalfinancetracker.model.User;
import com.demo.personalfinancetracker.repository.UserRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
@RestController
@RequestMapping("/api/auth")
@CrossOrigin(
        allowCredentials = "true",
        origins = {
                "http://localhost:5173",
                "https://personal-transaction-management-sys.vercel.app"
        }
)
public class AuthController {

    @Autowired private UserRepository userRepository;
    @Autowired private PasswordEncoder passwordEncoder;

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody User user) {
        if (user.getFullName() == null || user.getPhoneNumber() == null || user.getUsername() == null || user.getPassword() == null) {
            return ResponseEntity.badRequest().body("All fields are required");
        }

        if (userRepository.findByUsername(user.getUsername()).isPresent()) {
            return ResponseEntity.badRequest().body("Username already exists");
        }

        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);
        return ResponseEntity.ok("User registered successfully");
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody User loginUser, HttpSession session) {
        Optional<User> userOptional = userRepository.findByUsername(loginUser.getUsername());
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            if (passwordEncoder.matches(loginUser.getPassword(), user.getPassword())) {
                session.setAttribute("userId", user.getId());
                UserDTO userDTO = new UserDTO(user); // Reuse the existing DTO to exclude password

                Map<String, Object> response = new HashMap<>();
                response.put("message", "Login successful");
                response.put("user", userDTO);

                return ResponseEntity.ok(response);
            }
        }
        return ResponseEntity.status(401).body("Invalid credentials");
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(HttpSession session) {
        session.invalidate();
        return ResponseEntity.ok("Logged out successfully");
    }

    @GetMapping("/me")
    public ResponseEntity<?> currentUser(HttpSession session) {
        Long userId = (Long) session.getAttribute("userId");
        if (userId == null) {
            return ResponseEntity.status(401).body("Unauthorized");
        }

        Optional<User> userOptional = userRepository.findById(userId);
        if (userOptional.isEmpty()) {
            return ResponseEntity.status(404).body("User not found");
        }

        UserDTO userDTO = new UserDTO(userOptional.get());
        Map<String, Object> response = new HashMap<>();
        response.put("message", "User fetched successfully");
        response.put("user", userDTO);

        return ResponseEntity.ok(response);
    }

}
