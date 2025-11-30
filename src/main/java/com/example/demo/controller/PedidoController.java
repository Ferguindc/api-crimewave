package com.example.demo.controller;

import com.example.demo.model.Pedido;
import com.example.demo.service.PedidoService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/pedidos")
public class PedidoController {

    @Autowired
    private PedidoService pedidoService;

    @GetMapping
    public ResponseEntity<List<Pedido>> getAllPedidos() {
        return ResponseEntity.ok(pedidoService.getAllPedidos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Pedido> getPedidoById(@PathVariable Long id) {
        return pedidoService.getPedidoById(id)
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/usuario/{usuarioId}")
    public ResponseEntity<List<Pedido>> getPedidosByUsuario(@PathVariable Long usuarioId) {
        return ResponseEntity.ok(pedidoService.getPedidosByUsuario(usuarioId));
    }

    @GetMapping("/estado/{estado}")
    public ResponseEntity<List<Pedido>> getPedidosByEstado(@PathVariable String estado) {
        return ResponseEntity.ok(pedidoService.getPedidosByEstado(estado));
    }

    @PostMapping
    public ResponseEntity<?> createPedido(@Valid @RequestBody Pedido pedido) {
        try {
            Pedido nuevoPedido = pedidoService.createPedido(pedido);
            return ResponseEntity.status(HttpStatus.CREATED).body(nuevoPedido);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PatchMapping("/{id}/estado")
    public ResponseEntity<?> updateEstadoPedido(
            @PathVariable Long id,
            @RequestBody Map<String, String> body) {
        try {
            String nuevoEstado = body.get("estado");
            Pedido pedidoActualizado = pedidoService.updateEstadoPedido(id, nuevoEstado);
            return ResponseEntity.ok(pedidoActualizado);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deletePedido(@PathVariable Long id) {
        try {
            pedidoService.deletePedido(id);
            return ResponseEntity.ok().body("Pedido eliminado correctamente");
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
}

