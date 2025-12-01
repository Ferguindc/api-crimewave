# Documentación Swagger - API CrimeWave

## ¿Qué es Swagger?

Swagger es una herramienta que permite documentar, probar y explorar APIs REST de forma interactiva. Con Swagger UI, puedes:

- Ver todos los endpoints disponibles
- Probar las APIs directamente desde el navegador
- Ver los modelos de datos (schemas)
- Entender qué parámetros requiere cada endpoint
- Ver ejemplos de respuestas

## Cómo acceder a la documentación Swagger

Una vez que inicies tu aplicación Spring Boot, puedes acceder a Swagger UI en:

```
http://localhost:8082/swagger-ui.html
```

O también puedes acceder a la documentación JSON de OpenAPI en:

```
http://localhost:8082/api-docs
```

## Características implementadas

### 1. Configuración OpenAPI
- Se creó `OpenApiConfig.java` con información básica de la API
- Configuración del servidor de desarrollo
- Información de contacto y licencia

### 2. Documentación de controladores

#### ProductoController (`/api/productos`)
- **GET /api/productos** - Obtener todos los productos
- **GET /api/productos/{id}** - Obtener producto por ID
- **GET /api/productos/buscar?nombre={nombre}** - Buscar productos por nombre
- **GET /api/productos/categoria/{categoriaId}** - Productos por categoría
- **POST /api/productos** - Crear nuevo producto
- **PUT /api/productos/{id}** - Actualizar producto
- **DELETE /api/productos/{id}** - Eliminar producto
- **POST /api/productos/{id}/imagen** - Subir imagen del producto
- **GET /api/productos/imagen/{fileName}** - Obtener imagen del producto
- **POST /api/productos/{productoId}/categorias** - Agregar categorías al producto

#### CategoriaController (`/api/categorias`)
- **GET /api/categorias** - Obtener todas las categorías
- **GET /api/categorias/{id}** - Obtener categoría por ID
- **POST /api/categorias** - Crear nueva categoría
- **PUT /api/categorias/{id}** - Actualizar categoría
- **DELETE /api/categorias/{id}** - Eliminar categoría

#### UsuarioController (`/api/usuarios`)
- **GET /api/usuarios** - Obtener todos los usuarios
- **GET /api/usuarios/{id}** - Obtener usuario por ID
- **GET /api/usuarios/email/{email}** - Obtener usuario por email
- **POST /api/usuarios** - Crear nuevo usuario
- **PUT /api/usuarios/{id}** - Actualizar usuario
- **DELETE /api/usuarios/{id}** - Eliminar usuario

#### PedidoController (`/api/pedidos`)
- **GET /api/pedidos** - Obtener todos los pedidos
- **GET /api/pedidos/{id}** - Obtener pedido por ID
- **GET /api/pedidos/usuario/{usuarioId}** - Obtener pedidos por usuario
- **GET /api/pedidos/estado/{estado}** - Obtener pedidos por estado
- **POST /api/pedidos** - Crear nuevo pedido
- **PATCH /api/pedidos/{id}/estado** - Actualizar estado del pedido
- **DELETE /api/pedidos/{id}** - Eliminar pedido

### 3. Anotaciones utilizadas

- `@Tag` - Para agrupar endpoints por controlador
- `@Operation` - Para describir cada operación/endpoint
- `@ApiResponse` / `@ApiResponses` - Para documentar respuestas posibles
- `@Parameter` - Para describir parámetros de entrada

### 4. Configuración en application.properties

```properties
# Swagger/OpenAPI Configuration
springdoc.swagger-ui.path=/swagger-ui.html
springdoc.api-docs.path=/api-docs
springdoc.swagger-ui.operationsSorter=method
springdoc.swagger-ui.tagsSorter=alpha
springdoc.swagger-ui.tryItOutEnabled=true
```

## Cómo usar Swagger UI

1. **Inicia tu aplicación**: `./mvnw.cmd spring-boot:run`
2. **Abre tu navegador** y ve a `http://localhost:8082/swagger-ui.html`
3. **Explora los endpoints**: Los verás organizados por categorías (Productos, Categorías, Usuarios, Pedidos)
4. **Prueba un endpoint**: 
   - Haz clic en el endpoint que quieras probar
   - Haz clic en "Try it out"
   - Completa los parámetros requeridos
   - Haz clic en "Execute"
   - Verás la respuesta del servidor

## Ejemplos de uso

### Probar GET /api/productos
1. Expande "Productos" → "GET /api/productos"
2. Haz clic en "Try it out"
3. Haz clic en "Execute"
4. Verás la lista de todos los productos

### Probar POST /api/productos (crear producto)
1. Expande "Productos" → "POST /api/productos"
2. Haz clic en "Try it out"
3. Modifica el JSON de ejemplo con los datos del producto
4. Haz clic en "Execute"
5. Verás el producto creado con su ID asignado

### Subir imagen de producto
1. Expande "Productos" → "POST /api/productos/{id}/imagen"
2. Haz clic en "Try it out"
3. Ingresa el ID del producto
4. Selecciona un archivo de imagen
5. Haz clic en "Execute"

## Beneficios de tener Swagger

1. **Documentación siempre actualizada**: Se genera automáticamente del código
2. **Pruebas fáciles**: No necesitas herramientas externas como Postman
3. **Mejor comunicación**: Los desarrolladores frontend pueden ver exactamente qué esperar
4. **Validación**: Puedes probar diferentes scenarios directamente
5. **Onboarding**: Nuevos desarrolladores pueden entender la API rápidamente

## URLs importantes

- **Swagger UI**: http://localhost:8082/swagger-ui.html
- **OpenAPI JSON**: http://localhost:8082/api-docs
- **OpenAPI YAML**: http://localhost:8082/api-docs.yaml

¡Tu API ahora está completamente documentada con Swagger! 🎉
