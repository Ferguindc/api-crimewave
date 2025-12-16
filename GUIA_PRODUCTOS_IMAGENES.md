# Guía de Uso: Productos con Imágenes

## 📋 Descripción General

El sistema ha sido modificado para permitir la carga de imágenes directamente al crear o actualizar productos. Ahora puedes enviar tanto los datos del producto como la imagen en una misma petición HTTP usando `multipart/form-data`.

## 🎯 Endpoints Modificados

### 1. Crear Producto con Imagen

**Endpoint:** `POST /api/productos`  
**Content-Type:** `multipart/form-data`

#### Parámetros:

| Parámetro | Tipo | Requerido | Descripción |
|-----------|------|-----------|-------------|
| nombre | String | Sí | Nombre del producto |
| descripcion | String | No | Descripción del producto |
| precio | String | Sí | Precio del producto (ej: "29.99") |
| stock | Integer | No | Cantidad en stock (default: 0) |
| categoriaIds | String | No | IDs de categorías separados por coma (ej: "1,2,3") |
| imagen | File | No | Archivo de imagen del producto |

#### Ejemplo con cURL:

```bash
curl -X POST http://localhost:8080/api/productos \
  -F "nombre=Camiseta Premium" \
  -F "descripcion=Camiseta de alta calidad" \
  -F "precio=29.99" \
  -F "stock=100" \
  -F "categoriaIds=1,2" \
  -F "imagen=@/ruta/a/imagen.jpg"
```

#### Ejemplo con JavaScript/Fetch:

```javascript
const formData = new FormData();
formData.append('nombre', 'Camiseta Premium');
formData.append('descripcion', 'Camiseta de alta calidad');
formData.append('precio', '29.99');
formData.append('stock', '100');
formData.append('categoriaIds', '1,2');
formData.append('imagen', imageFile); // imageFile es un objeto File

fetch('http://localhost:8080/api/productos', {
  method: 'POST',
  body: formData
})
.then(response => response.json())
.then(data => console.log('Producto creado:', data))
.catch(error => console.error('Error:', error));
```

#### Ejemplo con React:

```jsx
const handleSubmit = async (e) => {
  e.preventDefault();
  
  const formData = new FormData();
  formData.append('nombre', nombre);
  formData.append('descripcion', descripcion);
  formData.append('precio', precio);
  formData.append('stock', stock);
  formData.append('categoriaIds', categoriaIds.join(','));
  if (imagenFile) {
    formData.append('imagen', imagenFile);
  }

  try {
    const response = await fetch('http://localhost:8080/api/productos', {
      method: 'POST',
      body: formData
    });
    const data = await response.json();
    console.log('Producto creado:', data);
  } catch (error) {
    console.error('Error:', error);
  }
};
```

---

### 2. Actualizar Producto con Imagen

**Endpoint:** `PUT /api/productos/{id}`  
**Content-Type:** `multipart/form-data`

#### Parámetros:

| Parámetro | Tipo | Requerido | Descripción |
|-----------|------|-----------|-------------|
| nombre | String | No | Nuevo nombre del producto |
| descripcion | String | No | Nueva descripción |
| precio | String | No | Nuevo precio |
| stock | Integer | No | Nueva cantidad en stock |
| imagen | File | No | Nueva imagen (reemplaza la anterior) |

**Nota:** Solo se actualizan los campos que se envían. Si no envías un campo, mantiene su valor actual.

#### Ejemplo con cURL:

```bash
curl -X PUT http://localhost:8080/api/productos/1 \
  -F "nombre=Camiseta Premium Actualizada" \
  -F "precio=39.99" \
  -F "imagen=@/ruta/a/nueva-imagen.jpg"
```

#### Ejemplo con JavaScript/Fetch:

```javascript
const formData = new FormData();
formData.append('nombre', 'Camiseta Premium Actualizada');
formData.append('precio', '39.99');
if (nuevaImagen) {
  formData.append('imagen', nuevaImagen);
}

fetch('http://localhost:8080/api/productos/1', {
  method: 'PUT',
  body: formData
})
.then(response => response.json())
.then(data => console.log('Producto actualizado:', data))
.catch(error => console.error('Error:', error));
```

---

## 📝 Endpoints Alternativos (JSON)

Si necesitas crear o actualizar productos sin imágenes usando JSON tradicional, puedes usar estos endpoints:

### Crear Producto (JSON)
**Endpoint:** `POST /api/productos/json`  
**Content-Type:** `application/json`

```json
{
  "nombre": "Producto Test",
  "descripcion": "Descripción del producto",
  "precio": 29.99,
  "stock": 100
}
```

### Actualizar Producto (JSON)
**Endpoint:** `PUT /api/productos/{id}/json`  
**Content-Type:** `application/json`

```json
{
  "nombre": "Producto Actualizado",
  "descripcion": "Nueva descripción",
  "precio": 39.99,
  "stock": 150
}
```

---

## 🖼️ Endpoint Existente para Subir Imagen

Si prefieres subir la imagen por separado después de crear el producto:

**Endpoint:** `POST /api/productos/{id}/imagen`  
**Content-Type:** `multipart/form-data`

```bash
curl -X POST http://localhost:8080/api/productos/1/imagen \
  -F "file=@/ruta/a/imagen.jpg"
```

---

## 📥 Obtener Imagen

**Endpoint:** `GET /api/productos/imagen/{fileName}`

```bash
curl http://localhost:8080/api/productos/imagen/abc123.jpg
```

O simplemente usa la URL en un tag `<img>`:

```html
<img src="http://localhost:8080/api/productos/imagen/abc123.jpg" alt="Producto" />
```

---

## 🔍 Flujo Completo de Ejemplo

### 1. Crear producto con imagen:

```javascript
const crearProducto = async () => {
  const formData = new FormData();
  formData.append('nombre', 'Sudadera CrimeWave');
  formData.append('descripcion', 'Sudadera edición limitada');
  formData.append('precio', '49.99');
  formData.append('stock', '50');
  formData.append('categoriaIds', '1,3');
  formData.append('imagen', imagenFile);

  const response = await fetch('http://localhost:8080/api/productos', {
    method: 'POST',
    body: formData
  });
  
  return await response.json();
};
```

### 2. Obtener todos los productos:

```javascript
const obtenerProductos = async () => {
  const response = await fetch('http://localhost:8080/api/productos');
  return await response.json();
};
```

### 3. Mostrar productos con imágenes en React:

```jsx
const ProductoCard = ({ producto }) => {
  const imagenUrl = producto.imagenUrl 
    ? `http://localhost:8080/api/productos/imagen/${producto.imagenUrl}`
    : '/placeholder.jpg';

  return (
    <div className="producto-card">
      <img src={imagenUrl} alt={producto.nombre} />
      <h3>{producto.nombre}</h3>
      <p>{producto.descripcion}</p>
      <p className="precio">${producto.precio}</p>
      <p className="stock">Stock: {producto.stock}</p>
    </div>
  );
};
```

---

## ⚠️ Notas Importantes

1. **Almacenamiento:** Las imágenes se guardan en el directorio `uploads/productos/` del servidor.

2. **Nombres únicos:** Cada imagen se guarda con un UUID único para evitar conflictos.

3. **Reemplazo automático:** Al actualizar la imagen de un producto, la imagen anterior se elimina automáticamente.

4. **Validaciones:** 
   - Los archivos no pueden estar vacíos
   - El sistema valida que los campos requeridos estén presentes

5. **Categorías:** Puedes asignar múltiples categorías al crear el producto enviando los IDs separados por coma en el parámetro `categoriaIds`.

---

## 🎨 Componente React Completo de Ejemplo

```jsx
import React, { useState } from 'react';

const FormularioProducto = () => {
  const [nombre, setNombre] = useState('');
  const [descripcion, setDescripcion] = useState('');
  const [precio, setPrecio] = useState('');
  const [stock, setStock] = useState('');
  const [imagen, setImagen] = useState(null);
  const [previewUrl, setPreviewUrl] = useState('');

  const handleImagenChange = (e) => {
    const file = e.target.files[0];
    if (file) {
      setImagen(file);
      setPreviewUrl(URL.createObjectURL(file));
    }
  };

  const handleSubmit = async (e) => {
    e.preventDefault();

    const formData = new FormData();
    formData.append('nombre', nombre);
    formData.append('descripcion', descripcion);
    formData.append('precio', precio);
    formData.append('stock', stock);
    if (imagen) {
      formData.append('imagen', imagen);
    }

    try {
      const response = await fetch('http://localhost:8080/api/productos', {
        method: 'POST',
        body: formData
      });

      if (response.ok) {
        const data = await response.json();
        console.log('Producto creado:', data);
        alert('Producto creado exitosamente!');
        // Limpiar formulario
        setNombre('');
        setDescripcion('');
        setPrecio('');
        setStock('');
        setImagen(null);
        setPreviewUrl('');
      } else {
        alert('Error al crear el producto');
      }
    } catch (error) {
      console.error('Error:', error);
      alert('Error de conexión');
    }
  };

  return (
    <form onSubmit={handleSubmit}>
      <div>
        <label>Nombre:</label>
        <input
          type="text"
          value={nombre}
          onChange={(e) => setNombre(e.target.value)}
          required
        />
      </div>

      <div>
        <label>Descripción:</label>
        <textarea
          value={descripcion}
          onChange={(e) => setDescripcion(e.target.value)}
        />
      </div>

      <div>
        <label>Precio:</label>
        <input
          type="number"
          step="0.01"
          value={precio}
          onChange={(e) => setPrecio(e.target.value)}
          required
        />
      </div>

      <div>
        <label>Stock:</label>
        <input
          type="number"
          value={stock}
          onChange={(e) => setStock(e.target.value)}
        />
      </div>

      <div>
        <label>Imagen:</label>
        <input
          type="file"
          accept="image/*"
          onChange={handleImagenChange}
        />
        {previewUrl && (
          <img 
            src={previewUrl} 
            alt="Preview" 
            style={{ maxWidth: '200px', marginTop: '10px' }}
          />
        )}
      </div>

      <button type="submit">Crear Producto</button>
    </form>
  );
};

export default FormularioProducto;
```

---

## 🔧 Verificación del Sistema

Para verificar que todo funciona correctamente:

1. Inicia el servidor Spring Boot
2. Prueba crear un producto con imagen usando Postman o cURL
3. Verifica que la imagen se guardó en `uploads/productos/`
4. Accede a `GET /api/productos/{id}` y verifica que `imagenUrl` contiene el nombre del archivo
5. Accede a `GET /api/productos/imagen/{fileName}` para ver la imagen

---

¡Sistema listo para cargar imágenes directamente al crear o actualizar productos! 🎉

