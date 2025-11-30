# Guía de Integración Frontend - React

## Componente para Mostrar Productos con Imágenes

```jsx
// components/ProductoCard.jsx
import React from 'react';
import './ProductoCard.css';

const ProductoCard = ({ producto }) => {
  // Construir la URL de la imagen
  const getImageUrl = () => {
    if (!producto.imagenUrl) {
      return 'https://via.placeholder.com/300?text=Sin+Imagen';
    }
    
    // Si ya es una URL completa (http/https), usarla directamente
    if (producto.imagenUrl.startsWith('http')) {
      return producto.imagenUrl;
    }
    
    // Si es un nombre de archivo, construir la URL del backend
    return `http://localhost:8082/api/productos/imagen/${producto.imagenUrl}`;
  };

  return (
    <div className="producto-card">
      <div className="producto-imagen">
        <img 
          src={getImageUrl()} 
          alt={producto.nombre}
          onError={(e) => {
            e.target.src = 'https://via.placeholder.com/300?text=Error';
          }}
        />
      </div>
      <div className="producto-info">
        <h3>{producto.nombre}</h3>
        <p className="descripcion">{producto.descripcion}</p>
        <p className="precio">${producto.precio}</p>
        <p className="stock">Stock: {producto.stock}</p>
        <button>Agregar al Carrito</button>
      </div>
    </div>
  );
};

export default ProductoCard;
```

## Componente para Crear/Editar Productos con Imagen

```jsx
// components/FormularioProducto.jsx
import React, { useState } from 'react';
import axios from 'axios';

const FormularioProducto = ({ productoInicial = null, onSuccess }) => {
  const [producto, setProducto] = useState(productoInicial || {
    nombre: '',
    descripcion: '',
    precio: 0,
    stock: 0
  });
  const [imagenFile, setImagenFile] = useState(null);
  const [previewUrl, setPreviewUrl] = useState(null);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState(null);

  // Preview de la imagen seleccionada
  const handleImageChange = (e) => {
    const file = e.target.files[0];
    if (file) {
      setImagenFile(file);
      const reader = new FileReader();
      reader.onloadend = () => {
        setPreviewUrl(reader.result);
      };
      reader.readAsDataURL(file);
    }
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    setLoading(true);
    setError(null);

    try {
      let productoId;

      if (productoInicial && productoInicial.id) {
        // Actualizar producto existente
        const response = await axios.put(
          `http://localhost:8082/api/productos/${productoInicial.id}`,
          producto
        );
        productoId = response.data.id;
      } else {
        // Crear nuevo producto
        const response = await axios.post(
          'http://localhost:8082/api/productos',
          producto
        );
        productoId = response.data.id;
      }

      // Subir imagen si fue seleccionada
      if (imagenFile) {
        const formData = new FormData();
        formData.append('file', imagenFile);

        await axios.post(
          `http://localhost:8082/api/productos/${productoId}/imagen`,
          formData,
          {
            headers: {
              'Content-Type': 'multipart/form-data',
            },
          }
        );
      }

      alert('Producto guardado con éxito!');
      if (onSuccess) onSuccess();
      
      // Limpiar formulario
      setProducto({ nombre: '', descripcion: '', precio: 0, stock: 0 });
      setImagenFile(null);
      setPreviewUrl(null);
    } catch (err) {
      console.error('Error:', err);
      setError(err.response?.data?.message || 'Error al guardar el producto');
    } finally {
      setLoading(false);
    }
  };

  return (
    <form onSubmit={handleSubmit} className="formulario-producto">
      <h2>{productoInicial ? 'Editar Producto' : 'Nuevo Producto'}</h2>
      
      {error && <div className="error-message">{error}</div>}

      <div className="form-group">
        <label>Nombre:</label>
        <input
          type="text"
          required
          value={producto.nombre}
          onChange={(e) => setProducto({...producto, nombre: e.target.value})}
        />
      </div>

      <div className="form-group">
        <label>Descripción:</label>
        <textarea
          value={producto.descripcion}
          onChange={(e) => setProducto({...producto, descripcion: e.target.value})}
        />
      </div>

      <div className="form-group">
        <label>Precio:</label>
        <input
          type="number"
          step="0.01"
          required
          value={producto.precio}
          onChange={(e) => setProducto({...producto, precio: parseFloat(e.target.value)})}
        />
      </div>

      <div className="form-group">
        <label>Stock:</label>
        <input
          type="number"
          required
          value={producto.stock}
          onChange={(e) => setProducto({...producto, stock: parseInt(e.target.value)})}
        />
      </div>

      <div className="form-group">
        <label>Imagen:</label>
        <input
          type="file"
          accept="image/*"
          onChange={handleImageChange}
        />
        {previewUrl && (
          <div className="image-preview">
            <img src={previewUrl} alt="Preview" style={{maxWidth: '200px'}} />
          </div>
        )}
      </div>

      <button type="submit" disabled={loading}>
        {loading ? 'Guardando...' : 'Guardar Producto'}
      </button>
    </form>
  );
};

export default FormularioProducto;
```

## Componente de Lista de Productos

```jsx
// components/ListaProductos.jsx
import React, { useState, useEffect } from 'react';
import axios from 'axios';
import ProductoCard from './ProductoCard';

const ListaProductos = () => {
  const [productos, setProductos] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);

  useEffect(() => {
    cargarProductos();
  }, []);

  const cargarProductos = async () => {
    try {
      setLoading(true);
      const response = await axios.get('http://localhost:8082/api/productos');
      setProductos(response.data);
    } catch (err) {
      console.error('Error al cargar productos:', err);
      setError('Error al cargar productos');
    } finally {
      setLoading(false);
    }
  };

  if (loading) return <div>Cargando productos...</div>;
  if (error) return <div className="error">{error}</div>;

  return (
    <div className="lista-productos">
      <h1>Nuestros Productos</h1>
      <div className="productos-grid">
        {productos.map(producto => (
          <ProductoCard key={producto.id} producto={producto} />
        ))}
      </div>
    </div>
  );
};

export default ListaProductos;
```

## CSS Básico

```css
/* ProductoCard.css */
.producto-card {
  border: 1px solid #ddd;
  border-radius: 8px;
  overflow: hidden;
  transition: transform 0.2s;
  background: white;
}

.producto-card:hover {
  transform: translateY(-5px);
  box-shadow: 0 4px 12px rgba(0,0,0,0.15);
}

.producto-imagen {
  width: 100%;
  height: 250px;
  overflow: hidden;
  background: #f5f5f5;
}

.producto-imagen img {
  width: 100%;
  height: 100%;
  object-fit: cover;
}

.producto-info {
  padding: 15px;
}

.producto-info h3 {
  margin: 0 0 10px 0;
  font-size: 1.2em;
}

.descripcion {
  color: #666;
  font-size: 0.9em;
  margin-bottom: 10px;
}

.precio {
  font-size: 1.5em;
  color: #2c3e50;
  font-weight: bold;
  margin: 10px 0;
}

.stock {
  color: #7f8c8d;
  font-size: 0.9em;
}

button {
  width: 100%;
  padding: 10px;
  background: #3498db;
  color: white;
  border: none;
  border-radius: 4px;
  cursor: pointer;
  margin-top: 10px;
}

button:hover {
  background: #2980b9;
}

.productos-grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(280px, 1fr));
  gap: 20px;
  padding: 20px;
}
```

## Configuración de Axios (Opcional)

```javascript
// config/api.js
import axios from 'axios';

const api = axios.create({
  baseURL: 'http://localhost:8082/api',
  headers: {
    'Content-Type': 'application/json',
  },
});

// Interceptor para agregar token si usas autenticación
api.interceptors.request.use(
  (config) => {
    const token = localStorage.getItem('token');
    if (token) {
      config.headers.Authorization = `Bearer ${token}`;
    }
    return config;
  },
  (error) => {
    return Promise.reject(error);
  }
);

export default api;
```

## Uso en tu App

```jsx
// App.jsx
import React from 'react';
import { BrowserRouter, Routes, Route } from 'react-router-dom';
import ListaProductos from './components/ListaProductos';
import FormularioProducto from './components/FormularioProducto';

function App() {
  return (
    <BrowserRouter>
      <Routes>
        <Route path="/" element={<ListaProductos />} />
        <Route path="/admin/productos/nuevo" element={<FormularioProducto />} />
      </Routes>
    </BrowserRouter>
  );
}

export default App;
```

## Notas Importantes

1. **CORS**: Asegúrate de que tu backend tenga CORS configurado (ya lo tienes en WebConfig.java)

2. **URLs de Imagen**: El código maneja tanto URLs externas (http/https) como archivos locales del servidor

3. **Manejo de Errores**: Incluye `onError` en las imágenes para mostrar un placeholder si falla la carga

4. **Preview de Imagen**: Muestra una vista previa antes de subir la imagen

5. **Validación**: Valida el tipo de archivo en el frontend para mejor UX

## Testing con datos de prueba

Si quieres probar rápidamente, los productos que se cargan automáticamente en el DataLoader ya tienen URLs de placeholder. Simplemente:

1. Inicia el backend
2. Ve a tu frontend
3. Los productos se cargarán con imágenes de placeholder de https://via.placeholder.com

Luego puedes reemplazarlas subiendo imágenes reales usando el formulario.

