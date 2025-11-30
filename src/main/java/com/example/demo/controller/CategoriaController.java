package com.example.demo.controller;

import com.example.demo.model.Categoria;
import com.example.demo.service.CategoriaService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/categorias")
public class CategoriaController {

    @Autowired
    private CategoriaService categoriaService;

    @GetMapping
    public ResponseEntity<List<Categoria>> getAllCategorias() {
        return ResponseEntity.ok(categoriaService.getAllCategorias());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Categoria> getCategoriaById(@PathVariable Long id) {
        return categoriaService.getCategoriaById(id)
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Categoria> createCategoria(@Valid @RequestBody Categoria categoria) {
        Categoria nuevaCategoria = categoriaService.createCategoria(categoria);
        return ResponseEntity.status(HttpStatus.CREATED).body(nuevaCategoria);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateCategoria(@PathVariable Long id, @Valid @RequestBody Categoria categoria) {
        try {
            Categoria categoriaActualizada = categoriaService.updateCategoria(id, categoria);
            return ResponseEntity.ok(categoriaActualizada);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteCategoria(@PathVariable Long id) {
        try {
            categoriaService.deleteCategoria(id);
            return ResponseEntity.ok().body("Categoría eliminada correctamente");
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
}

