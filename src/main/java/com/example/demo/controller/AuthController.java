package com.example.demo.controller;

import com.example.demo.model.Usuario;
import com.example.demo.repository.UsuarioRepository;
import com.example.demo.service.JwtTokenService;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/auth")
@Validated
@CrossOrigin(origins = "http://localhost:3001")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtTokenService jwtTokenService;
    private final PasswordEncoder passwordEncoder;
    private final UsuarioRepository usuarioRepository;

    public AuthController(AuthenticationManager authenticationManager,
                          JwtTokenService jwtTokenService,
                          PasswordEncoder passwordEncoder,
                          UsuarioRepository usuarioRepository) {
        this.authenticationManager = authenticationManager;
        this.jwtTokenService = jwtTokenService;
        this.passwordEncoder = passwordEncoder;
        this.usuarioRepository = usuarioRepository;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
        );
        User principal = (User) authentication.getPrincipal();
        Map<String, Object> claims = new HashMap<>();
        claims.put("roles", principal.getAuthorities());
        String token = jwtTokenService.generateToken(principal, claims);
        Map<String, Object> response = new HashMap<>();
        response.put("accessToken", token);
        response.put("tokenType", "Bearer");
        response.put("expiresIn", 3600);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequest request) {
        if (usuarioRepository.existsByEmail(request.getEmail())) {
            return ResponseEntity.badRequest().body("El email ya está registrado");
        }
        Usuario u = new Usuario();
        u.setNombre(request.getNombre());
        u.setEmail(request.getEmail());
        u.setPasswordHash(passwordEncoder.encode(request.getPassword()));
        u.setRol(request.getRol() == null ? "USER" : request.getRol());
        usuarioRepository.save(u);
        return ResponseEntity.ok("Usuario registrado");
    }

    public static class LoginRequest {
        @Email
        private String email;
        @NotBlank
        private String password;
        public String getEmail() { return email; }
        public void setEmail(String email) { this.email = email; }
        public String getPassword() { return password; }
        public void setPassword(String password) { this.password = password; }
    }

    public static class RegisterRequest {
        @NotBlank
        private String nombre;
        @Email
        private String email;
        @NotBlank
        private String password;
        private String rol;
        public String getNombre() { return nombre; }
        public void setNombre(String nombre) { this.nombre = nombre; }
        public String getEmail() { return email; }
        public void setEmail(String email) { this.email = email; }
        public String getPassword() { return password; }
        public void setPassword(String password) { this.password = password; }
        public String getRol() { return rol; }
        public void setRol(String rol) { this.rol = rol; }
    }
}

