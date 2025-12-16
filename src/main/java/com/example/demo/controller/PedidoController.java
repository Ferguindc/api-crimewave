package com.example.demo.controller;

import com.example.demo.model.Pedido;
import com.example.demo.service.PedidoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Tag(name = "Pedidos", description = "API para la gestión de pedidos")
@RestController
@RequestMapping("/api/pedidos")
public class PedidoController {

    @Autowired
    private PedidoService pedidoService;

    @Operation(summary = "Obtener todos los pedidos",
               description = "Retorna una lista con todos los pedidos del sistema")
    @ApiResponse(responseCode = "200", description = "Lista de pedidos obtenida exitosamente")
    @GetMapping
    public ResponseEntity<List<Pedido>> getAllPedidos() {
        return ResponseEntity.ok(pedidoService.getAllPedidos());
    }

    @Operation(summary = "Obtener pedido por ID",
               description = "Retorna un pedido específico basado en su ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Pedido encontrado"),
        @ApiResponse(responseCode = "404", description = "Pedido no encontrado")
    })
    @GetMapping("/{id}")
    public ResponseEntity<Pedido> getPedidoById(
            @Parameter(description = "ID del pedido", required = true)
            @PathVariable Long id) {
        return pedidoService.getPedidoById(id)
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Obtener pedidos por usuario",
               description = "Retorna todos los pedidos realizados por un usuario específico")
    @ApiResponse(responseCode = "200", description = "Lista de pedidos del usuario obtenida exitosamente")
    @GetMapping("/usuario/{usuarioId}")
    public ResponseEntity<List<Pedido>> getPedidosByUsuario(
            @Parameter(description = "ID del usuario", required = true)
            @PathVariable Long usuarioId) {
        return ResponseEntity.ok(pedidoService.getPedidosByUsuario(usuarioId));
    }

    @Operation(summary = "Obtener pedidos por estado",
               description = "Retorna todos los pedidos que tienen un estado específico")
    @ApiResponse(responseCode = "200", description = "Lista de pedidos con el estado especificado obtenida exitosamente")
    @GetMapping("/estado/{estado}")
    public ResponseEntity<List<Pedido>> getPedidosByEstado(
            @Parameter(description = "Estado del pedido (ej: PENDIENTE, PROCESANDO, ENVIADO, ENTREGADO)", required = true)
            @PathVariable String estado) {
        return ResponseEntity.ok(pedidoService.getPedidosByEstado(estado));
    }

    @Operation(summary = "Crear nuevo pedido",
               description = "Crea un nuevo pedido en el sistema")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Pedido creado exitosamente"),
        @ApiResponse(responseCode = "400", description = "Datos de pedido inválidos")
    })
    @PostMapping
    public ResponseEntity<?> createPedido(
            @Parameter(description = "Datos del pedido a crear", required = true)
            @Valid @RequestBody Pedido pedido) {
        try {
            Pedido nuevoPedido = pedidoService.createPedido(pedido);
            return ResponseEntity.status(HttpStatus.CREATED).body(nuevoPedido);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @Operation(summary = "Actualizar estado del pedido",
               description = "Actualiza el estado de un pedido específico")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Estado del pedido actualizado exitosamente"),
        @ApiResponse(responseCode = "404", description = "Pedido no encontrado"),
        @ApiResponse(responseCode = "400", description = "Estado inválido")
    })
    @PatchMapping("/{id}/estado")
    public ResponseEntity<?> updateEstadoPedido(
            @Parameter(description = "ID del pedido", required = true)
            @PathVariable Long id,
            @Parameter(description = "Nuevo estado del pedido", required = true)
            @RequestBody Map<String, String> body) {
        try {
            String nuevoEstado = body.get("estado");
            Pedido pedidoActualizado = pedidoService.updateEstadoPedido(id, nuevoEstado);
            return ResponseEntity.ok(pedidoActualizado);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @Operation(summary = "Actualizar estado de pago",
               description = "Actualiza el estado de pago de un pedido específico")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Estado de pago actualizado exitosamente"),
        @ApiResponse(responseCode = "404", description = "Pedido no encontrado")
    })
    @PatchMapping("/{id}/estado-pago")
    public ResponseEntity<?> updateEstadoPago(
            @Parameter(description = "ID del pedido", required = true)
            @PathVariable Long id,
            @Parameter(description = "Nuevo estado de pago", required = true)
            @RequestBody Map<String, String> body) {
        try {
            String nuevoEstadoPago = body.get("estadoPago");
            Pedido pedidoActualizado = pedidoService.updateEstadoPago(id, nuevoEstadoPago);
            return ResponseEntity.ok(pedidoActualizado);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @Operation(summary = "Obtener pedidos por email",
               description = "Retorna todos los pedidos realizados por un email específico")
    @ApiResponse(responseCode = "200", description = "Lista de pedidos obtenida exitosamente")
    @GetMapping("/email/{email}")
    public ResponseEntity<List<Pedido>> getPedidosByEmail(
            @Parameter(description = "Email del cliente", required = true)
            @PathVariable String email) {
        return ResponseEntity.ok(pedidoService.getPedidosByEmail(email));
    }

    @Operation(summary = "Eliminar pedido",
               description = "Elimina un pedido del sistema")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Pedido eliminado exitosamente"),
        @ApiResponse(responseCode = "404", description = "Pedido no encontrado")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deletePedido(
            @Parameter(description = "ID del pedido a eliminar", required = true)
            @PathVariable Long id) {
        try {
            pedidoService.deletePedido(id);
            return ResponseEntity.ok().body("Pedido eliminado correctamente");
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
}

