package com.example.demo.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "pedidos")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Pedido {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Información del usuario
    @Column(name = "usuario_id")
    private Long usuarioId;

    @Column(name = "nombre_cliente", length = 100)
    private String nombreCliente;

    @Column(name = "email_cliente", length = 100)
    private String emailCliente;

    @Column(name = "telefono_cliente", length = 20)
    private String telefonoCliente;

    // Información de envío
    @Column(name = "direccion_envio", length = 255)
    private String direccionEnvio;

    @Column(name = "ciudad_envio", length = 100)
    private String ciudadEnvio;

    @Column(name = "comuna_envio", length = 100)
    private String comunaEnvio;

    @Column(name = "notas_pedido", columnDefinition = "TEXT")
    private String notasPedido;

    // Información de pago
    @Column(name = "metodo_pago", length = 50)
    private String metodoPago;

    @Column(name = "estado_pago", length = 50)
    private String estadoPago; // pendiente, pagado, fallido, reembolsado

    // Estado del pedido
    @Column(name = "estado_pedido", length = 50)
    private String estadoPedido; // pendiente, confirmado, enviado, entregado, cancelado

    // Total
    @Column
    private Double total;

    // Relación con detalles
    @OneToMany(mappedBy = "pedido", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<DetallePedido> detalles = new ArrayList<>();

    @PrePersist
    protected void onCreate() {
        if (estadoPedido == null) {
            estadoPedido = "pendiente";
        }
        if (estadoPago == null) {
            estadoPago = "pendiente";
        }
    }
}

