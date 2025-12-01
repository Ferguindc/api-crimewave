package com.example.demo.controller;

import com.example.demo.model.Categoria;
import com.example.demo.service.CategoriaService;
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

@Tag(name = "Categorías", description = "API para la gestión de categorías de productos")
@RestController
@RequestMapping("/api/categorias")
public class CategoriaController {

    @Autowired
    private CategoriaService categoriaService;

    @Operation(summary = "Obtener todas las categorías",
               description = "Retorna una lista con todas las categorías disponibles")
    @ApiResponse(responseCode = "200", description = "Lista de categorías obtenida exitosamente")
    @GetMapping
    public ResponseEntity<List<Categoria>> getAllCategorias() {
        return ResponseEntity.ok(categoriaService.getAllCategorias());
    }

    @Operation(summary = "Obtener categoría por ID",
               description = "Retorna una categoría específica basada en su ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Categoría encontrada"),
        @ApiResponse(responseCode = "404", description = "Categoría no encontrada")
    })
    @GetMapping("/{id}")
    public ResponseEntity<Categoria> getCategoriaById(
            @Parameter(description = "ID de la categoría", required = true)
            @PathVariable Long id) {
        return categoriaService.getCategoriaById(id)
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Crear nueva categoría",
               description = "Crea una nueva categoría en el sistema")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Categoría creada exitosamente"),
        @ApiResponse(responseCode = "400", description = "Datos de categoría inválidos")
    })
    @PostMapping
    public ResponseEntity<Categoria> createCategoria(
            @Parameter(description = "Datos de la categoría a crear", required = true)
            @Valid @RequestBody Categoria categoria) {
        Categoria nuevaCategoria = categoriaService.createCategoria(categoria);
        return ResponseEntity.status(HttpStatus.CREATED).body(nuevaCategoria);
    }

    @Operation(summary = "Actualizar categoría",
               description = "Actualiza los datos de una categoría existente")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Categoría actualizada exitosamente"),
        @ApiResponse(responseCode = "404", description = "Categoría no encontrada"),
        @ApiResponse(responseCode = "400", description = "Datos de categoría inválidos")
    })
    @PutMapping("/{id}")
    public ResponseEntity<?> updateCategoria(
            @Parameter(description = "ID de la categoría a actualizar", required = true)
            @PathVariable Long id,
            @Parameter(description = "Nuevos datos de la categoría", required = true)
            @Valid @RequestBody Categoria categoria) {
        try {
            Categoria categoriaActualizada = categoriaService.updateCategoria(id, categoria);
            return ResponseEntity.ok(categoriaActualizada);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @Operation(summary = "Eliminar categoría",
               description = "Elimina una categoría del sistema")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Categoría eliminada exitosamente"),
        @ApiResponse(responseCode = "404", description = "Categoría no encontrada")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteCategoria(
            @Parameter(description = "ID de la categoría a eliminar", required = true)
            @PathVariable Long id) {
        try {
            categoriaService.deleteCategoria(id);
            return ResponseEntity.ok().body("Categoría eliminada correctamente");
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
}

