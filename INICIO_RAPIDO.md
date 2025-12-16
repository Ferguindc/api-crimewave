# 🚀 Inicio Rápido - Productos con Imágenes

## ✅ ¿Qué se ha modificado?

El sistema de productos ahora acepta imágenes directamente al crear o actualizar productos usando `multipart/form-data`.

## 📦 Archivos Modificados

- ✅ `ProductoController.java` - Endpoints modificados y nuevos

## 📄 Archivos Creados

1. **GUIA_PRODUCTOS_IMAGENES.md** - Documentación completa
2. **RESUMEN_CAMBIOS.md** - Resumen de cambios técnicos
3. **ejemplos-react-productos.jsx** - Componentes React listos para usar
4. **test-productos-imagenes.ps1** - Script de prueba PowerShell
5. **Postman_Collection_Productos_Imagenes.json** - Colección Postman
6. **INICIO_RAPIDO.md** - Este archivo

## 🎯 Endpoints Principales

### Crear Producto con Imagen
```
POST /api/productos
Content-Type: multipart/form-data

Parámetros:
- nombre (requerido)
- precio (requerido)
- descripcion (opcional)
- stock (opcional)
- categoriaIds (opcional, separados por coma)
- imagen (opcional, archivo)
```

### Actualizar Producto con Imagen
```
PUT /api/productos/{id}
Content-Type: multipart/form-data

Parámetros:
- nombre (opcional)
- precio (opcional)
- descripcion (opcional)
- stock (opcional)
- imagen (opcional, archivo)
```

## 🧪 Prueba Rápida

### 1. Iniciar el servidor
```powershell
cd C:\Users\alvar\Desktop\TiendaCrimeWave\Proyecto-Full-Stack-II-React\api-crimewave
.\mvnw.cmd spring-boot:run
```

### 2. Probar con Postman

1. Importa la colección: `Postman_Collection_Productos_Imagenes.json`
2. Abre "1. Crear Producto con Imagen"
3. Selecciona una imagen en el parámetro "imagen"
4. Haz clic en "Send"

### 3. Probar con PowerShell
```powershell
.\test-productos-imagenes.ps1
```

### 4. Probar con cURL
```bash
curl -X POST http://localhost:8080/api/productos \
  -F "nombre=Producto Test" \
  -F "precio=29.99" \
  -F "imagen=@C:\ruta\a\imagen.jpg"
```

## 📱 Integración con React

### Ejemplo Básico
```jsx
import { CrearProductoForm } from './ejemplos-react-productos';

function App() {
  return (
    <div>
      <h1>Mi Tienda</h1>
      <CrearProductoForm />
    </div>
  );
}
```

### Usando el Hook Personalizado
```jsx
import { useProductos } from './ejemplos-react-productos';

function MiComponente() {
  const { productos, loading, crearProducto } = useProductos();

  const handleSubmit = async (e) => {
    e.preventDefault();
    const formData = new FormData(e.target);
    await crearProducto(formData);
  };

  return (
    <form onSubmit={handleSubmit}>
      <input name="nombre" required />
      <input name="precio" type="number" required />
      <input name="imagen" type="file" />
      <button type="submit">Crear</button>
    </form>
  );
}
```

## 📚 Documentación

- **Guía completa:** `GUIA_PRODUCTOS_IMAGENES.md`
- **Detalles técnicos:** `RESUMEN_CAMBIOS.md`
- **Componentes React:** `ejemplos-react-productos.jsx`

## 🔍 Verificación

Para verificar que todo funciona:

1. ✅ El proyecto compila sin errores: `.\mvnw.cmd compile`
2. ✅ El servidor inicia correctamente: `.\mvnw.cmd spring-boot:run`
3. ✅ Puedes crear un producto: `POST /api/productos`
4. ✅ La imagen se guarda en: `uploads/productos/`
5. ✅ Puedes ver la imagen: `GET /api/productos/imagen/{fileName}`

## ⚡ Características Clave

- ✅ **Un solo paso:** Crea producto e imagen juntos
- ✅ **Actualización parcial:** Solo actualiza los campos que envías
- ✅ **Eliminación automática:** Al actualizar imagen, borra la anterior
- ✅ **Retrocompatible:** Los endpoints JSON siguen funcionando
- ✅ **Validaciones:** Nombre y precio requeridos
- ✅ **Categorías múltiples:** Asigna varias categorías al crear

## 🎨 URLs de Imagen

Cuando obtienes un producto, el campo `imagenUrl` contiene el nombre del archivo.
Para mostrar la imagen:

```javascript
const imagenUrl = producto.imagenUrl 
  ? `http://localhost:8080/api/productos/imagen/${producto.imagenUrl}`
  : '/placeholder.jpg';
```

```html
<img src={imagenUrl} alt={producto.nombre} />
```

## 🛠️ Solución de Problemas

### Error: "Archivo vacío"
- Asegúrate de seleccionar un archivo válido
- Verifica que el campo se llame "imagen" (no "file")

### Error: "Producto no encontrado"
- Verifica que el ID del producto sea correcto
- Asegúrate de que el producto existe

### Error CORS (en desarrollo)
- El backend ya tiene CORS configurado en `WebConfig.java`
- Permite origen: `http://localhost:5173`

### Las imágenes no se muestran
- Verifica que el servidor esté corriendo
- Verifica la URL: `http://localhost:8080/api/productos/imagen/{fileName}`
- Comprueba que el archivo existe en `uploads/productos/`

## 📞 Endpoints de Consulta

```bash
# Listar todos
GET http://localhost:8080/api/productos

# Obtener uno
GET http://localhost:8080/api/productos/1

# Buscar por nombre
GET http://localhost:8080/api/productos/buscar?nombre=camiseta

# Por categoría
GET http://localhost:8080/api/productos/categoria/1

# Ver imagen
GET http://localhost:8080/api/productos/imagen/{fileName}
```

## 🎉 ¡Listo para usar!

Todo está configurado y funcionando. Los cambios son retrocompatibles, así que tu código existente seguirá funcionando sin modificaciones.

**¿Necesitas ayuda?** Consulta los archivos de documentación o los ejemplos de código.

---

**Fecha de modificación:** 15/12/2025  
**Compilación:** ✅ BUILD SUCCESS  
**Estado:** ✅ Producción Ready

