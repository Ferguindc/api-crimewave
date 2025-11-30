# API REST CRUD - CrimeWave

API REST completa desarrollada con Spring Boot para gestionar Usuarios, Productos, Categorías y Pedidos.

## 🚀 Tecnologías

- **Spring Boot 4.0.0**
- **Spring Data JPA**
- **H2 Database** (desarrollo)
- **Lombok**
- **Maven**

## 📋 Requisitos

- Java 17 o superior
- Maven 3.6+

## ⚙️ Configuración

1. **Clonar o verificar el proyecto**

2. **Configurar base de datos** (opcional)
   - Por defecto usa H2 (en memoria)
   - Para MySQL, descomentar las líneas en `application.properties` y `pom.xml`

3. **Ejecutar el proyecto**
```bash
mvn spring-boot:run
```

4. **Acceder a la aplicación**
   - API: http://localhost:8080/api
   - Consola H2: http://localhost:8080/h2-console
     - JDBC URL: `jdbc:h2:mem:crimewavedb`
     - Usuario: `sa`
     - Password: (vacío)

## 📚 Endpoints de la API

### 👤 USUARIOS (`/api/usuarios`)

| Método | Endpoint | Descripción |
|--------|----------|-------------|
| GET | `/api/usuarios` | Listar todos los usuarios |
| GET | `/api/usuarios/{id}` | Obtener usuario por ID |
| GET | `/api/usuarios/email/{email}` | Obtener usuario por email |
| POST | `/api/usuarios` | Crear nuevo usuario |
| PUT | `/api/usuarios/{id}` | Actualizar usuario |
| DELETE | `/api/usuarios/{id}` | Eliminar usuario |

**Ejemplo JSON - Crear Usuario:**
```json
{
  "nombre": "Juan Pérez",
  "email": "juan@example.com",
  "passwordHash": "hash_de_contraseña",
  "rol": "cliente"
}
```

---

### 📦 PRODUCTOS (`/api/productos`)

| Método | Endpoint | Descripción |
|--------|----------|-------------|
| GET | `/api/productos` | Listar todos los productos |
| GET | `/api/productos/{id}` | Obtener producto por ID |
| GET | `/api/productos/buscar?nombre={nombre}` | Buscar productos por nombre |
| GET | `/api/productos/categoria/{categoriaId}` | Productos por categoría |
| POST | `/api/productos` | Crear nuevo producto |
| PUT | `/api/productos/{id}` | Actualizar producto |
| POST | `/api/productos/{id}/categorias` | Asignar categorías a producto |
| DELETE | `/api/productos/{id}` | Eliminar producto |

**Ejemplo JSON - Crear Producto:**
```json
{
  "nombre": "Camiseta Deportiva",
  "descripcion": "Camiseta de algodón premium",
  "precio": 29.99,
  "imagenUrl": "https://ejemplo.com/imagen.jpg",
  "stock": 100
}
```

**Ejemplo JSON - Asignar Categorías:**
```json
[1, 2, 3]
```

---

### 🏷️ CATEGORÍAS (`/api/categorias`)

| Método | Endpoint | Descripción |
|--------|----------|-------------|
| GET | `/api/categorias` | Listar todas las categorías |
| GET | `/api/categorias/{id}` | Obtener categoría por ID |
| POST | `/api/categorias` | Crear nueva categoría |
| PUT | `/api/categorias/{id}` | Actualizar categoría |
| DELETE | `/api/categorias/{id}` | Eliminar categoría |

**Ejemplo JSON - Crear Categoría:**
```json
{
  "nombre": "Ropa Deportiva"
}
```

---

### 🛒 PEDIDOS (`/api/pedidos`)

| Método | Endpoint | Descripción |
|--------|----------|-------------|
| GET | `/api/pedidos` | Listar todos los pedidos |
| GET | `/api/pedidos/{id}` | Obtener pedido por ID |
| GET | `/api/pedidos/usuario/{usuarioId}` | Pedidos de un usuario |
| GET | `/api/pedidos/estado/{estado}` | Pedidos por estado |
| POST | `/api/pedidos` | Crear nuevo pedido |
| PATCH | `/api/pedidos/{id}/estado` | Actualizar estado del pedido |
| DELETE | `/api/pedidos/{id}` | Eliminar pedido |

**Ejemplo JSON - Crear Pedido:**
```json
{
  "usuario": {
    "id": 1
  },
  "detalles": [
    {
      "producto": {
        "id": 1
      },
      "cantidad": 2
    },
    {
      "producto": {
        "id": 2
      },
      "cantidad": 1
    }
  ]
}
```

**Ejemplo JSON - Actualizar Estado:**
```json
{
  "estado": "enviado"
}
```

**Estados posibles:** `pendiente`, `pagado`, `enviado`, `entregado`, `cancelado`

---

## 🔧 Uso con React

### Ejemplo de petición con fetch:

```javascript
// GET - Obtener todos los productos
fetch('http://localhost:8080/api/productos')
  .then(response => response.json())
  .then(data => console.log(data));

// POST - Crear producto
fetch('http://localhost:8080/api/productos', {
  method: 'POST',
  headers: {
    'Content-Type': 'application/json',
  },
  body: JSON.stringify({
    nombre: 'Nuevo Producto',
    descripcion: 'Descripción del producto',
    precio: 49.99,
    stock: 50
  })
})
  .then(response => response.json())
  .then(data => console.log(data));

// PUT - Actualizar producto
fetch('http://localhost:8080/api/productos/1', {
  method: 'PUT',
  headers: {
    'Content-Type': 'application/json',
  },
  body: JSON.stringify({
    nombre: 'Producto Actualizado',
    precio: 59.99
  })
})
  .then(response => response.json())
  .then(data => console.log(data));

// DELETE - Eliminar producto
fetch('http://localhost:8080/api/productos/1', {
  method: 'DELETE'
})
  .then(response => console.log('Eliminado'));
```

### Ejemplo con Axios:

```javascript
import axios from 'axios';

const API_URL = 'http://localhost:8080/api';

// GET
const productos = await axios.get(`${API_URL}/productos`);

// POST
const nuevoProducto = await axios.post(`${API_URL}/productos`, {
  nombre: 'Nuevo Producto',
  precio: 49.99
});

// PUT
const actualizado = await axios.put(`${API_URL}/productos/1`, {
  nombre: 'Producto Actualizado'
});

// DELETE
await axios.delete(`${API_URL}/productos/1`);
```

---

## 📊 Modelo de Datos

### Relaciones:
- **Usuario** ↔ **Pedido** (1:N)
- **Pedido** ↔ **DetallePedido** (1:N)
- **Producto** ↔ **DetallePedido** (1:N)
- **Producto** ↔ **Categoría** (N:N)

---

## 🧪 Datos de Prueba

Al iniciar la aplicación, se cargan automáticamente datos de prueba:

- **Usuarios**: admin@crimewave.com, cliente@test.com
- **Categorías**: Ropa, Accesorios, Calzado
- **Productos**: Varios productos de ejemplo
- **Pedidos**: Un pedido de prueba

---

## 🔐 Seguridad

⚠️ **IMPORTANTE**: Esta API no incluye autenticación. Para producción, considera agregar:
- Spring Security
- JWT Authentication
- Encriptación de contraseñas con BCrypt

---

## 🐛 Troubleshooting

### Error de puerto 8080 ocupado
Cambia el puerto en `application.properties`:
```properties
server.port=8081
```

### Error de CORS desde React
Verifica que React corre en puerto 3000 o 5173, o actualiza `WebConfig.java`

---

## 📝 Licencia

Este proyecto es de código abierto para fines educativos.

