package com.example.demo.controller;

import com.example.demo.model.Producto;
import com.example.demo.service.ProductoService;
import com.example.demo.service.FileStorageService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Path;
import java.util.List;
import java.util.Set;

@Tag(name = "Productos", description = "API para la gestión de productos")
@RestController
@RequestMapping("/api/productos")
public class ProductoController {

    @Autowired
    private ProductoService productoService;

    @Autowired
    private FileStorageService fileStorageService;

    @Operation(summary = "Obtener todos los productos",
               description = "Retorna una lista con todos los productos disponibles")
    @ApiResponse(responseCode = "200", description = "Lista de productos obtenida exitosamente")
    @GetMapping
    public ResponseEntity<List<Producto>> getAllProductos() {
        return ResponseEntity.ok(productoService.getAllProductos());
    }

    @Operation(summary = "Obtener producto por ID",
               description = "Retorna un producto específico basado en su ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Producto encontrado"),
        @ApiResponse(responseCode = "404", description = "Producto no encontrado")
    })
    @GetMapping("/{id}")
    public ResponseEntity<Producto> getProductoById(
            @Parameter(description = "ID del producto", required = true)
            @PathVariable Long id) {
        return productoService.getProductoById(id)
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Buscar productos por nombre",
               description = "Busca productos que contengan el texto especificado en su nombre")
    @ApiResponse(responseCode = "200", description = "Búsqueda realizada exitosamente")
    @GetMapping("/buscar")
    public ResponseEntity<List<Producto>> searchProductos(
            @Parameter(description = "Texto a buscar en el nombre del producto", required = true)
            @RequestParam String nombre) {
        return ResponseEntity.ok(productoService.searchProductosByNombre(nombre));
    }

    @Operation(summary = "Obtener productos por categoría",
               description = "Retorna todos los productos que pertenecen a una categoría específica")
    @ApiResponse(responseCode = "200", description = "Lista de productos de la categoría obtenida exitosamente")
    @GetMapping("/categoria/{categoriaId}")
    public ResponseEntity<List<Producto>> getProductosByCategoria(
            @Parameter(description = "ID de la categoría", required = true)
            @PathVariable Long categoriaId) {
        return ResponseEntity.ok(productoService.getProductosByCategoria(categoriaId));
    }

    @Operation(summary = "Crear nuevo producto",
               description = "Crea un nuevo producto en el sistema")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Producto creado exitosamente"),
        @ApiResponse(responseCode = "400", description = "Datos de producto inválidos")
    })
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Producto> createProducto(
            @Parameter(description = "Nombre del producto", required = true)
            @RequestParam String nombre,
            @Parameter(description = "Descripción del producto")
            @RequestParam(required = false) String descripcion,
            @Parameter(description = "Precio del producto", required = true)
            @RequestParam String precio,
            @Parameter(description = "Stock del producto")
            @RequestParam(required = false, defaultValue = "0") Integer stock,
            @Parameter(description = "IDs de categorías (separados por coma)")
            @RequestParam(required = false) String categoriaIds,
            @Parameter(description = "Archivo de imagen del producto")
            @RequestParam(required = false) MultipartFile imagen) {
        try {
            // Crear el producto
            Producto producto = new Producto();
            producto.setNombre(nombre);
            producto.setDescripcion(descripcion);
            producto.setPrecio(new java.math.BigDecimal(precio));
            producto.setStock(stock);

            // Guardar imagen si se proporciona
            if (imagen != null && !imagen.isEmpty()) {
                String fileName = fileStorageService.storeFile(imagen);
                producto.setImagenUrl(fileName);
            }

            // Crear el producto
            Producto nuevoProducto = productoService.createProducto(producto);

            // Agregar categorías si se proporcionan
            if (categoriaIds != null && !categoriaIds.trim().isEmpty()) {
                String[] idsArray = categoriaIds.split(",");
                Set<Long> categoriaIdSet = new java.util.HashSet<>();
                for (String id : idsArray) {
                    categoriaIdSet.add(Long.parseLong(id.trim()));
                }
                nuevoProducto = productoService.addCategoriasToProducto(nuevoProducto.getId(), categoriaIdSet);
            }

            return ResponseEntity.status(HttpStatus.CREATED).body(nuevoProducto);
        } catch (Exception e) {
            throw new RuntimeException("Error al crear producto: " + e.getMessage());
        }
    }

    @Operation(summary = "Crear nuevo producto (JSON)",
               description = "Crea un nuevo producto en el sistema usando JSON (sin imagen)")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Producto creado exitosamente"),
        @ApiResponse(responseCode = "400", description = "Datos de producto inválidos")
    })
    @PostMapping(value = "/json", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Producto> createProductoJson(
            @Parameter(description = "Datos del producto a crear", required = true)
            @Valid @RequestBody Producto producto) {
        Producto nuevoProducto = productoService.createProducto(producto);
        return ResponseEntity.status(HttpStatus.CREATED).body(nuevoProducto);
    }

    @Operation(summary = "Actualizar producto",
               description = "Actualiza los datos de un producto existente")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Producto actualizado exitosamente"),
        @ApiResponse(responseCode = "404", description = "Producto no encontrado"),
        @ApiResponse(responseCode = "400", description = "Datos de producto inválidos")
    })
    @PutMapping(value = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> updateProducto(
            @Parameter(description = "ID del producto a actualizar", required = true)
            @PathVariable Long id,
            @Parameter(description = "Nombre del producto")
            @RequestParam(required = false) String nombre,
            @Parameter(description = "Descripción del producto")
            @RequestParam(required = false) String descripcion,
            @Parameter(description = "Precio del producto")
            @RequestParam(required = false) String precio,
            @Parameter(description = "Stock del producto")
            @RequestParam(required = false) Integer stock,
            @Parameter(description = "Archivo de imagen del producto")
            @RequestParam(required = false) MultipartFile imagen) {
        try {
            // Buscar el producto existente
            Producto producto = productoService.getProductoById(id)
                    .orElseThrow(() -> new RuntimeException("Producto no encontrado"));

            // Actualizar solo los campos proporcionados
            if (nombre != null) {
                producto.setNombre(nombre);
            }
            if (descripcion != null) {
                producto.setDescripcion(descripcion);
            }
            if (precio != null) {
                producto.setPrecio(new java.math.BigDecimal(precio));
            }
            if (stock != null) {
                producto.setStock(stock);
            }

            // Actualizar imagen si se proporciona
            if (imagen != null && !imagen.isEmpty()) {
                // Eliminar imagen anterior si existe
                if (producto.getImagenUrl() != null && !producto.getImagenUrl().isEmpty()) {
                    try {
                        fileStorageService.deleteFile(producto.getImagenUrl());
                    } catch (Exception e) {
                        // Continuar aunque falle la eliminación
                    }
                }
                // Guardar nueva imagen
                String fileName = fileStorageService.storeFile(imagen);
                producto.setImagenUrl(fileName);
            }

            Producto productoActualizado = productoService.updateProducto(id, producto);
            return ResponseEntity.ok(productoActualizado);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @Operation(summary = "Actualizar producto (JSON)",
               description = "Actualiza los datos de un producto existente usando JSON (sin imagen)")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Producto actualizado exitosamente"),
        @ApiResponse(responseCode = "404", description = "Producto no encontrado"),
        @ApiResponse(responseCode = "400", description = "Datos de producto inválidos")
    })
    @PutMapping(value = "/{id}/json", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> updateProductoJson(
            @Parameter(description = "ID del producto a actualizar", required = true)
            @PathVariable Long id,
            @Parameter(description = "Nuevos datos del producto", required = true)
            @Valid @RequestBody Producto producto) {
        try {
            Producto productoActualizado = productoService.updateProducto(id, producto);
            return ResponseEntity.ok(productoActualizado);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @Operation(summary = "Agregar categorías al producto",
               description = "Asocia múltiples categorías a un producto específico")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Categorías agregadas exitosamente"),
        @ApiResponse(responseCode = "400", description = "Error al agregar categorías")
    })
    @PostMapping("/{productoId}/categorias")
    public ResponseEntity<?> addCategoriasToProducto(
            @Parameter(description = "ID del producto", required = true)
            @PathVariable Long productoId,
            @Parameter(description = "IDs de las categorías a agregar", required = true)
            @RequestBody Set<Long> categoriaIds) {
        try {
            Producto producto = productoService.addCategoriasToProducto(productoId, categoriaIds);
            return ResponseEntity.ok(producto);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @Operation(summary = "Eliminar producto",
               description = "Elimina un producto del sistema")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Producto eliminado exitosamente"),
        @ApiResponse(responseCode = "404", description = "Producto no encontrado")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteProducto(
            @Parameter(description = "ID del producto a eliminar", required = true)
            @PathVariable Long id) {
        try {
            productoService.deleteProducto(id);
            return ResponseEntity.ok().body("Producto eliminado correctamente");
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @Operation(summary = "Subir imagen del producto",
               description = "Sube una imagen para un producto específico")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Imagen subida exitosamente"),
        @ApiResponse(responseCode = "404", description = "Producto no encontrado"),
        @ApiResponse(responseCode = "400", description = "Archivo inválido")
    })
    @PostMapping("/{id}/imagen")
    public ResponseEntity<?> uploadImagen(
            @Parameter(description = "ID del producto", required = true)
            @PathVariable Long id,
            @Parameter(description = "Archivo de imagen", required = true)
            @RequestParam("file") MultipartFile file) {
        try {
            // Buscar el producto
            Producto producto = productoService.getProductoById(id)
                    .orElseThrow(() -> new RuntimeException("Producto no encontrado"));

            // Eliminar imagen anterior si existe
            if (producto.getImagenUrl() != null && !producto.getImagenUrl().isEmpty()) {
                try {
                    fileStorageService.deleteFile(producto.getImagenUrl());
                } catch (Exception e) {
                    // Continuar aunque falle la eliminación
                }
            }

            // Guardar nueva imagen
            String fileName = fileStorageService.storeFile(file);
            producto.setImagenUrl(fileName);
            productoService.updateProducto(id, producto);

            return ResponseEntity.ok().body("Imagen subida correctamente: " + fileName);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @Operation(summary = "Obtener imagen del producto",
               description = "Retorna la imagen de un producto por nombre de archivo")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Imagen encontrada",
                    content = @Content(mediaType = "image/*")),
        @ApiResponse(responseCode = "404", description = "Imagen no encontrada")
    })
    @GetMapping("/imagen/{fileName:.+}")
    public ResponseEntity<Resource> getImagen(
            @Parameter(description = "Nombre del archivo de imagen", required = true)
            @PathVariable String fileName) {
        try {
            Path filePath = fileStorageService.loadFile(fileName);
            Resource resource = new UrlResource(filePath.toUri());

            if (resource.exists() || resource.isReadable()) {
                // Determinar el tipo de contenido
                String contentType = "application/octet-stream";
                if (fileName.endsWith(".jpg") || fileName.endsWith(".jpeg")) {
                    contentType = "image/jpeg";
                } else if (fileName.endsWith(".png")) {
                    contentType = "image/png";
                } else if (fileName.endsWith(".gif")) {
                    contentType = "image/gif";
                } else if (fileName.endsWith(".webp")) {
                    contentType = "image/webp";
                }

                return ResponseEntity.ok()
                        .contentType(MediaType.parseMediaType(contentType))
                        .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + fileName + "\"")
                        .body(resource);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }
}

