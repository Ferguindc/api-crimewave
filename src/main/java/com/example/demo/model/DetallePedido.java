package com.example.demo.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.fasterxml.jackson.annotation.JsonBackReference;

@Entity
@Table(name = "detalle_pedido")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DetallePedido {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Información del producto
    @Column(name = "producto_id")
    private Long productoId;

    @Column(name = "nombre_producto", length = 200)
    private String nombreProducto;

    @Positive(message = "La cantidad debe ser mayor a 0")
    @Column(nullable = false)
    private Integer cantidad;

    @Column(length = 20)
    private String talla; // XS, S, M, L, XL, XXL, etc.

    // Precios
    @Column(name = "precio_unitario")
    private Double precioUnitario;

    @Column
    private Double subtotal;

    @Column(name = "imagen_url", length = 255)
    private String imagenUrl;

    // Relación con pedido
    @ManyToOne
    @JoinColumn(name = "pedido_id", nullable = false)
    @JsonBackReference
    private Pedido pedido;

    @PrePersist
    @PreUpdate
    protected void calcularSubtotal() {
        if (cantidad != null && precioUnitario != null) {
            subtotal = cantidad * precioUnitario;
        }
    }
}

