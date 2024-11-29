// package com.arjuncodes.studentsystem.controller;

// import com.arjuncodes.studentsystem.model.User;
// import com.arjuncodes.studentsystem.service.UserService;
// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.http.ResponseEntity;
// import org.springframework.security.crypto.password.PasswordEncoder;
// import org.springframework.web.bind.annotation.*;

// import java.util.HashMap;
// import java.util.Map;
// import java.util.Optional;

// @RestController
// @RequestMapping("/api/auth")
// public class AuthController {

//     @Autowired
//     private UserService userService;

//     @Autowired
//     private PasswordEncoder passwordEncoder;

//     // Signup API
//     @PostMapping("/signup")
//     public ResponseEntity<?> signup(@RequestBody User user) {
//         Optional<User> existingUser = userService.findByUsername(user.getUsername());
//         if (existingUser.isPresent()) {
//             return ResponseEntity.badRequest().body("Username already exists.");
//         }
//         User newUser = userService.signup(user);
//         return ResponseEntity.ok(newUser);
//     }

//     // Login API
//     @PostMapping("/login")
//     public ResponseEntity<?> login(@RequestBody Map<String, String> credentials) {
//         String username = credentials.get("username");
//         String password = credentials.get("password");

//         Optional<User> user = userService.findByUsername(username);
//         if (user.isPresent() && passwordEncoder.matches(password, user.get().getPassword())) {
//             Map<String, String> response = new HashMap<>();
//             response.put("message", "Login successful.");
//             response.put("userId", user.get().getId());
//             return ResponseEntity.ok(response);
//         } else {
//             return ResponseEntity.status(401).body("Invalid credentials.");
//         }
//     }
// }
package com.arjuncodes.studentsystem.controller;

import com.arjuncodes.studentsystem.model.User;
import com.arjuncodes.studentsystem.service.UserService;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private UserService userService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    // Generate a secure key for HS512 (minimum 512 bits)
    private Key secretKey = Keys.secretKeyFor(SignatureAlgorithm.HS512);
    private long expirationTime = 86400000; // 24 hours in milliseconds

    // Signup API
    @PostMapping("/signup")
    public ResponseEntity<?> signup(@RequestBody User user) {
        try {
            Optional<User> existingUser = userService.findByUsername(user.getUsername());
            if (existingUser.isPresent()) {
                return ResponseEntity.badRequest().body("Username already exists.");
            }
            User newUser = userService.signup(user);
            return ResponseEntity.ok(newUser);
        } catch (Exception e) {
            e.printStackTrace(); // Print the stack trace for debugging
            return ResponseEntity.status(500).body("Internal Server Error: " + e.getMessage());
        }
    }

    // Login API
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> credentials) {
        try {
            String username = credentials.get("username");
            String password = credentials.get("password");

            Optional<User> user = userService.findByUsername(username);
            if (user.isPresent() && passwordEncoder.matches(password, user.get().getPassword())) {
                // Generate JWT Token
                String token = generateToken(user.get());

                // Response Map
                Map<String, String> response = new HashMap<>();
                response.put("message", "Login successful.");
                response.put("userId", user.get().getId());
                response.put("token", token); // Include the token in the response
                return ResponseEntity.ok(response);
            } else {
                return ResponseEntity.status(401).body("Invalid credentials.");
            }
        } catch (Exception e) {
            e.printStackTrace(); // Print the stack trace for debugging
            return ResponseEntity.status(500).body("Internal Server Error: " + e.getMessage());
        }
    }

    // Method to generate JWT Token
    private String generateToken(User user) {
        try {
            return Jwts.builder()
                    .setSubject(user.getUsername())
                    .setId(user.getId())
                    .setIssuedAt(new Date())
                    .setExpiration(new Date(System.currentTimeMillis() + expirationTime))
                    .signWith(secretKey) // Use the secure key
                    .compact();
        } catch (Exception e) {
            e.printStackTrace(); // Print the stack trace for debugging
            throw new RuntimeException("Error generating token: " + e.getMessage());
        }
    }
}

