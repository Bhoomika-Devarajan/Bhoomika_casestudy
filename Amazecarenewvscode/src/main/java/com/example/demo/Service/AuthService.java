package com.example.demo.Service;

public interface AuthService {

    String registerPatient(String username, String password);

    String registerDoctor(String username, String password);

    String registerAdmin(String username, String password);

    String login(String username, String password);

}