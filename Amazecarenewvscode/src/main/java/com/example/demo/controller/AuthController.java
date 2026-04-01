package com.example.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.example.demo.Service.AuthService;
import com.example.demo.entity.User;
import org.springframework.http.ResponseEntity;
import jakarta.validation.Valid;


@RestController
@RequestMapping("/auth")
@CrossOrigin(origins = "http://localhost:3000")
public class AuthController {

    @Autowired
    private AuthService authService;

    @PostMapping("/register/patient")
    public ResponseEntity<String> registerPatient(@Valid @RequestBody User user) {
        return ResponseEntity.ok(authService.registerPatient(user.getUsername(), user.getPassword()));
    }

    @PostMapping("/register/doctor")
    public ResponseEntity<String> registerDoctor(@Valid @RequestBody User user) {
        return ResponseEntity.ok(authService.registerDoctor(user.getUsername(), user.getPassword()));
    }

    @PostMapping("/register/admin")
    public ResponseEntity<String> registerAdmin(@Valid @RequestBody User user) {
        return ResponseEntity.ok(authService.registerAdmin(user.getUsername(), user.getPassword()));
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@Valid @RequestBody User user) {
        return ResponseEntity.ok(authService.login(user.getUsername(), user.getPassword()));
    }
}