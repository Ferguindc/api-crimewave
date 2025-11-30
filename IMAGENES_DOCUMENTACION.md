# Documentación de Imágenes de Productos

## Implementación Completada

Se ha agregado soporte completo para imágenes de productos en el backend. Los archivos se almacenan localmente en el servidor en la carpeta `uploads/productos/`.

## Nuevos Endpoints

### 1. Subir imagen a un producto
```
POST /api/productos/{id}/imagen
Content-Type: multipart/form-data

Parámetros:
- file: archivo de imagen (JPG, PNG, GIF, WEBP)

Respuesta exitosa:
{
  "message": "Imagen subida correctamente: uuid-filename.jpg"
}
```

### 2. Obtener imagen de un producto
```
GET /api/productos/imagen/{fileName}

Ejemplo:
GET /api/productos/imagen/550e8400-e29b-41d4-a716-446655440000.jpg

Respuesta:
Imagen en formato binario con el Content-Type apropiado
```

## Cómo usar desde el Frontend

### Ejemplo con React y Axios:

#### Subir imagen al crear/editar producto:
```javascript
const subirImagenProducto = async (productoId, archivo) => {
  const formData = new FormData();
  formData.append('file', archivo);

  try {
    const response = await axios.post(
      `http://localhost:8082/api/productos/${productoId}/imagen`,
      formData,
      {
        headers: {
          'Content-Type': 'multipart/form-data',
        },
      }
    );
    console.log('Imagen subida:', response.data);
  } catch (error) {
    console.error('Error al subir imagen:', error);
  }
};
```

#### Mostrar imagen en el frontend:
```javascript
const ProductoCard = ({ producto }) => {
  // Si el producto tiene imagenUrl, construir la URL completa
  const imagenUrl = producto.imagenUrl 
    ? `http://localhost:8082/api/productos/imagen/${producto.imagenUrl}`
    : '/ruta/a/imagen-por-defecto.jpg';

  return (
    <div className="producto-card">
      <img src={imagenUrl} alt={producto.nombre} />
      <h3>{producto.nombre}</h3>
      <p>{producto.descripcion}</p>
      <p>${producto.precio}</p>
    </div>
  );
};
```

#### Componente de formulario para crear producto con imagen:
```javascript
const FormularioProducto = () => {
  const [producto, setProducto] = useState({
    nombre: '',
    descripcion: '',
    precio: 0,
    stock: 0
  });
  const [imagenFile, setImagenFile] = useState(null);

  const handleSubmit = async (e) => {
    e.preventDefault();
    
    try {
      // 1. Crear el producto
      const responseProducto = await axios.post(
        'http://localhost:8082/api/productos',
        producto
      );
      
      const productoId = responseProducto.data.id;
      
      // 2. Subir la imagen si existe
      if (imagenFile) {
        const formData = new FormData();
        formData.append('file', imagenFile);
        
        await axios.post(
          `http://localhost:8082/api/productos/${productoId}/imagen`,
          formData,
          {
            headers: { 'Content-Type': 'multipart/form-data' }
          }
        );
      }
      
      alert('Producto creado con éxito!');
    } catch (error) {
      console.error('Error:', error);
    }
  };

  return (
    <form onSubmit={handleSubmit}>
      <input
        type="text"
        placeholder="Nombre"
        value={producto.nombre}
        onChange={(e) => setProducto({...producto, nombre: e.target.value})}
      />
      <input
        type="text"
        placeholder="Descripción"
        value={producto.descripcion}
        onChange={(e) => setProducto({...producto, descripcion: e.target.value})}
      />
      <input
        type="number"
        placeholder="Precio"
        value={producto.precio}
        onChange={(e) => setProducto({...producto, precio: parseFloat(e.target.value)})}
      />
      <input
        type="number"
        placeholder="Stock"
        value={producto.stock}
        onChange={(e) => setProducto({...producto, stock: parseInt(e.target.value)})}
      />
      <input
        type="file"
        accept="image/*"
        onChange={(e) => setImagenFile(e.target.files[0])}
      />
      <button type="submit">Crear Producto</button>
    </form>
  );
};
```

## Configuración

### Límites de tamaño de archivo:
- Tamaño máximo por archivo: 10MB
- Tamaño máximo de request: 10MB

Puedes modificar estos límites en `application.properties`:
```properties
spring.servlet.multipart.max-file-size=10MB
spring.servlet.multipart.max-request-size=10MB
```

## Notas Importantes

1. **Directorio de almacenamiento**: Las imágenes se guardan en `uploads/productos/` en el servidor.

2. **Nombres de archivo**: Se usa UUID para evitar conflictos de nombres.

3. **Formatos soportados**: JPG, JPEG, PNG, GIF, WEBP.

4. **Compatibilidad con el frontend existente**: 
   - Los endpoints existentes NO han cambiado
   - El modelo Producto ya tenía el campo `imagenUrl`
   - Solo se agregaron 2 endpoints nuevos para gestión de imágenes

5. **Al eliminar un producto**: La imagen anterior se elimina automáticamente al subir una nueva.

## Flujo recomendado

1. Crear el producto primero (POST /api/productos)
2. Obtener el ID del producto creado
3. Subir la imagen usando el ID (POST /api/productos/{id}/imagen)
4. Al listar productos, el campo `imagenUrl` contendrá el nombre del archivo
5. Construir la URL completa en el frontend: `http://localhost:8082/api/productos/imagen/{imagenUrl}`

## Probando con Postman/Thunder Client

### Subir imagen:
1. POST a `http://localhost:8082/api/productos/1/imagen`
2. En Body, seleccionar "form-data"
3. Agregar key "file" de tipo "File"
4. Seleccionar tu imagen
5. Enviar

### Ver imagen:
1. GET a `http://localhost:8082/api/productos/imagen/nombre-del-archivo.jpg`
2. Deberías ver la imagen directamente en el navegador

