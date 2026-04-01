package com.example.demo.Service;

import com.example.demo.entity.Role;
import com.example.demo.entity.User;
import com.example.demo.entity.Patient;
import com.example.demo.entity.Doctor;
import com.example.demo.exception.BadRequestException;
import com.example.demo.repository.UserRepository;
import com.example.demo.repository.PatientRepository;
import com.example.demo.repository.DoctorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.core.userdetails.UserDetailsService;
import com.example.demo.config.JwtUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AuthServiceImpl implements AuthService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private PatientRepository patientRepository;

    @Autowired
    private DoctorRepository doctorRepository;

    @Override
    @Transactional
    public String registerPatient(String username, String password) {
        if (userRepository.existsByUsername(username)) {
            throw new BadRequestException("Username already exists: " + username);
        }
        User user = new User();
        user.setUsername(username);
        user.setPassword(passwordEncoder.encode(password));
        user.setRole(Role.PATIENT);
        User savedUser = userRepository.save(user);

        // Create associated Patient entity
        Patient patient = new Patient();
        patient.setUser(savedUser);
        patient.setFullName(username); // Default to username for now
        patientRepository.save(patient);

        return jwtUtil.generateToken(userDetailsService.loadUserByUsername(username));
    }

    @Override
    @Transactional
    public String registerDoctor(String username, String password) {
        if (userRepository.existsByUsername(username)) {
            throw new BadRequestException("Username already exists: " + username);
        }
        User user = new User();
        user.setUsername(username);
        user.setPassword(passwordEncoder.encode(password));
        user.setRole(Role.DOCTOR);
        User savedUser = userRepository.save(user);

        // Create associated Doctor entity
        Doctor doctor = new Doctor();
        doctor.setUser(savedUser);
        doctor.setName("Dr. " + username); // Default
        doctorRepository.save(doctor);

        return jwtUtil.generateToken(userDetailsService.loadUserByUsername(username));
    }

    @Override
    public String registerAdmin(String username, String password) {
        if (userRepository.existsByUsername(username)) {
            throw new BadRequestException("Username already exists: " + username);
        }
        User user = new User();
        user.setUsername(username);
        user.setPassword(passwordEncoder.encode(password));
        user.setRole(Role.ADMIN);
        userRepository.save(user);
        return jwtUtil.generateToken(userDetailsService.loadUserByUsername(username));
    }

    @Override
    public String login(String username, String password) {
        try {
            authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(username, password)
            );
            return jwtUtil.generateToken(userDetailsService.loadUserByUsername(username));
        } catch (Exception e) {
            throw new BadRequestException("Invalid username or password");
        }
    }
}