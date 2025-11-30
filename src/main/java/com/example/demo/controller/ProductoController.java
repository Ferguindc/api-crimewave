package com.example.demo.controller;

import com.example.demo.model.Producto;
import com.example.demo.service.ProductoService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/api/productos")
public class ProductoController {

    @Autowired
    private ProductoService productoService;

    @GetMapping
    public ResponseEntity<List<Producto>> getAllProductos() {
        return ResponseEntity.ok(productoService.getAllProductos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Producto> getProductoById(@PathVariable Long id) {
        return productoService.getProductoById(id)
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/buscar")
    public ResponseEntity<List<Producto>> searchProductos(@RequestParam String nombre) {
        return ResponseEntity.ok(productoService.searchProductosByNombre(nombre));
    }

    @GetMapping("/categoria/{categoriaId}")
    public ResponseEntity<List<Producto>> getProductosByCategoria(@PathVariable Long categoriaId) {
        return ResponseEntity.ok(productoService.getProductosByCategoria(categoriaId));
    }

    @PostMapping
    public ResponseEntity<Producto> createProducto(@Valid @RequestBody Producto producto) {
        Producto nuevoProducto = productoService.createProducto(producto);
        return ResponseEntity.status(HttpStatus.CREATED).body(nuevoProducto);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateProducto(@PathVariable Long id, @Valid @RequestBody Producto producto) {
        try {
            Producto productoActualizado = productoService.updateProducto(id, producto);
            return ResponseEntity.ok(productoActualizado);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/{productoId}/categorias")
    public ResponseEntity<?> addCategoriasToProducto(
            @PathVariable Long productoId,
            @RequestBody Set<Long> categoriaIds) {
        try {
            Producto producto = productoService.addCategoriasToProducto(productoId, categoriaIds);
            return ResponseEntity.ok(producto);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteProducto(@PathVariable Long id) {
        try {
            productoService.deleteProducto(id);
            return ResponseEntity.ok().body("Producto eliminado correctamente");
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
}

