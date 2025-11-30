package com.example.demo.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "usuarios")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "El nombre es obligatorio")
    @Column(length = 100)
    private String nombre;

    @NotBlank(message = "El email es obligatorio")
    @Email(message = "Email debe ser válido")
    @Column(unique = true, length = 100)
    private String email;

    @NotBlank(message = "La contraseña es obligatoria")
    @Column(name = "password_hash", length = 255)
    private String passwordHash;

    @Column(length = 50)
    private String rol; // admin, cliente, etc
}

