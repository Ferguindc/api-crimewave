package com.example.demo.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "productos")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Producto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "El nombre del producto es obligatorio")
    @Column(length = 200)
    private String nombre;

    @Column(columnDefinition = "TEXT")
    private String descripcion;

    @NotNull(message = "El precio es obligatorio")
    @Column(precision = 10, scale = 2)
    private BigDecimal precio;

    @Column(name = "imagen_url", length = 255)
    private String imagenUrl;

    @PositiveOrZero(message = "El stock debe ser mayor o igual a 0")
    private Integer stock;

    @ManyToMany
    @JoinTable(
        name = "producto_categoria",
        joinColumns = @JoinColumn(name = "producto_id"),
        inverseJoinColumns = @JoinColumn(name = "categoria_id")
    )
    private Set<Categoria> categorias = new HashSet<>();
}

