# ✅ Actualización de Entidades: Pedido y DetallePedido

## 📋 Cambios Realizados

Se han actualizado las entidades **Pedido** y **DetallePedido** para incluir todos los campos necesarios para visualizar correctamente los pedidos en la página.

---

## 🎯 Entidad Pedido - Campos Agregados

### Información del Cliente
```java
private Long usuarioId;
private String nombreCliente;
private String emailCliente;
private String telefonoCliente;
```

### Información de Envío
```java
private String direccionEnvio;
private String ciudadEnvio;
private String comunaEnvio;
private String notasPedido;
```

### Información de Pago
```java
private String metodoPago;      // tarjeta_credito, paypal, transferencia, etc.
private String estadoPago;       // pendiente, pagado, fallido, reembolsado
```

### Estado y Total
```java
private String estadoPedido;     // pendiente, confirmado, enviado, entregado, cancelado
private Double total;
private LocalDateTime fechaPedido;
```

---

## 🎯 Entidad DetallePedido - Campos Agregados

### Información del Producto
```java
private Long productoId;
private String nombreProducto;
private Integer cantidad;
private String talla;            // XS, S, M, L, XL, XXL, etc.
```

### Precios e Imagen
```java
private Double precioUnitario;
private Double subtotal;
private String imagenUrl;
```

### Cálculo Automático
```java
@PrePersist
@PreUpdate
protected void calcularSubtotal() {
    if (cantidad != null && precioUnitario != null) {
        subtotal = cantidad * precioUnitario;
    }
}
```

---

## 🚀 Ejemplo de Uso - Crear Pedido

### Desde el Frontend (JSON)

```json
{
  "usuarioId": 1,
  "nombreCliente": "Juan Pérez",
  "emailCliente": "juan@example.com",
  "telefonoCliente": "555-1234",
  "direccionEnvio": "Calle Principal 123, Apto 4B",
  "ciudadEnvio": "Santiago",
  "comunaEnvio": "Providencia",
  "notasPedido": "Entregar en horario de oficina",
  "metodoPago": "tarjeta_credito",
  "estadoPago": "pendiente",
  "estadoPedido": "pendiente",
  "detalles": [
    {
      "productoId": 1,
      "nombreProducto": "Camiseta CrimeWave",
      "cantidad": 2,
      "talla": "M",
      "precioUnitario": 29.99,
      "imagenUrl": "abc123.jpg"
    },
    {
      "productoId": 2,
      "nombreProducto": "Gorra CrimeWave",
      "cantidad": 1,
      "talla": "Única",
      "precioUnitario": 19.99,
      "imagenUrl": "def456.jpg"
    }
  ]
}
```

### Con JavaScript/Fetch

```javascript
const crearPedido = async (datosPedido) => {
  const response = await fetch('http://localhost:8080/api/pedidos', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json'
    },
    body: JSON.stringify(datosPedido)
  });

  if (response.ok) {
    const pedido = await response.json();
    console.log('Pedido creado:', pedido);
    return pedido;
  } else {
    throw new Error('Error al crear pedido');
  }
};
```

### Ejemplo Completo en React

```jsx
const CrearPedido = ({ carritoItems, usuario }) => {
  const [datosEnvio, setDatosEnvio] = useState({
    direccion: '',
    ciudad: '',
    comuna: '',
    telefono: '',
    notas: ''
  });

  const handleSubmit = async (e) => {
    e.preventDefault();

    const pedido = {
      usuarioId: usuario.id,
      nombreCliente: usuario.nombre,
      emailCliente: usuario.email,
      telefonoCliente: datosEnvio.telefono,
      direccionEnvio: datosEnvio.direccion,
      ciudadEnvio: datosEnvio.ciudad,
      comunaEnvio: datosEnvio.comuna,
      notasPedido: datosEnvio.notas,
      metodoPago: 'tarjeta_credito',
      estadoPago: 'pendiente',
      estadoPedido: 'pendiente',
      detalles: carritoItems.map(item => ({
        productoId: item.producto.id,
        nombreProducto: item.producto.nombre,
        cantidad: item.cantidad,
        talla: item.talla,
        precioUnitario: item.producto.precio,
        imagenUrl: item.producto.imagenUrl
      }))
    };

    try {
      const response = await fetch('http://localhost:8080/api/pedidos', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(pedido)
      });

      if (response.ok) {
        const pedidoCreado = await response.json();
        alert(`¡Pedido #${pedidoCreado.id} creado exitosamente!`);
        // Redirigir a página de confirmación
      }
    } catch (error) {
      console.error('Error:', error);
      alert('Error al crear el pedido');
    }
  };

  return (
    <form onSubmit={handleSubmit}>
      <h2>Datos de Envío</h2>
      
      <input
        type="text"
        placeholder="Dirección completa"
        value={datosEnvio.direccion}
        onChange={(e) => setDatosEnvio({...datosEnvio, direccion: e.target.value})}
        required
      />
      
      <input
        type="text"
        placeholder="Ciudad"
        value={datosEnvio.ciudad}
        onChange={(e) => setDatosEnvio({...datosEnvio, ciudad: e.target.value})}
        required
      />
      
      <input
        type="text"
        placeholder="Comuna"
        value={datosEnvio.comuna}
        onChange={(e) => setDatosEnvio({...datosEnvio, comuna: e.target.value})}
        required
      />
      
      <input
        type="tel"
        placeholder="Teléfono"
        value={datosEnvio.telefono}
        onChange={(e) => setDatosEnvio({...datosEnvio, telefono: e.target.value})}
        required
      />
      
      <textarea
        placeholder="Notas adicionales (opcional)"
        value={datosEnvio.notas}
        onChange={(e) => setDatosEnvio({...datosEnvio, notas: e.target.value})}
      />
      
      <button type="submit">Crear Pedido</button>
    </form>
  );
};
```

---

## 📊 Visualizar Pedido

### Componente React para Mostrar Pedido

```jsx
const DetallesPedido = ({ pedido }) => {
  return (
    <div className="pedido-container">
      <div className="pedido-header">
        <h2>Pedido #{pedido.id}</h2>
        <span className={`estado ${pedido.estadoPedido}`}>
          {pedido.estadoPedido}
        </span>
      </div>

      <div className="pedido-info">
        <div className="info-seccion">
          <h3>Cliente</h3>
          <p><strong>Nombre:</strong> {pedido.nombreCliente}</p>
          <p><strong>Email:</strong> {pedido.emailCliente}</p>
          <p><strong>Teléfono:</strong> {pedido.telefonoCliente}</p>
        </div>

        <div className="info-seccion">
          <h3>Envío</h3>
          <p><strong>Dirección:</strong> {pedido.direccionEnvio}</p>
          <p><strong>Ciudad:</strong> {pedido.ciudadEnvio}</p>
          <p><strong>Comuna:</strong> {pedido.comunaEnvio}</p>
          {pedido.notasPedido && (
            <p><strong>Notas:</strong> {pedido.notasPedido}</p>
          )}
        </div>

        <div className="info-seccion">
          <h3>Pago</h3>
          <p><strong>Método:</strong> {pedido.metodoPago}</p>
          <p><strong>Estado:</strong> 
            <span className={`estado-pago ${pedido.estadoPago}`}>
              {pedido.estadoPago}
            </span>
          </p>
        </div>
      </div>

      <div className="pedido-productos">
        <h3>Productos</h3>
        <table>
          <thead>
            <tr>
              <th>Imagen</th>
              <th>Producto</th>
              <th>Talla</th>
              <th>Cantidad</th>
              <th>Precio Unit.</th>
              <th>Subtotal</th>
            </tr>
          </thead>
          <tbody>
            {pedido.detalles.map(detalle => (
              <tr key={detalle.id}>
                <td>
                  {detalle.imagenUrl && (
                    <img 
                      src={`http://localhost:8080/api/productos/imagen/${detalle.imagenUrl}`}
                      alt={detalle.nombreProducto}
                      style={{ width: '50px', height: '50px', objectFit: 'cover' }}
                    />
                  )}
                </td>
                <td>{detalle.nombreProducto}</td>
                <td>{detalle.talla}</td>
                <td>{detalle.cantidad}</td>
                <td>${detalle.precioUnitario.toFixed(2)}</td>
                <td>${detalle.subtotal.toFixed(2)}</td>
              </tr>
            ))}
          </tbody>
          <tfoot>
            <tr>
              <td colSpan="5" style={{ textAlign: 'right' }}>
                <strong>TOTAL:</strong>
              </td>
              <td><strong>${pedido.total.toFixed(2)}</strong></td>
            </tr>
          </tfoot>
        </table>
      </div>

      <div className="pedido-footer">
        <p><strong>Fecha:</strong> {new Date(pedido.fechaPedido).toLocaleString('es-CL')}</p>
      </div>
    </div>
  );
};
```

---

## 🔧 Nuevos Endpoints

### 1. Actualizar Estado de Pago
```
PATCH /api/pedidos/{id}/estado-pago
Body: { "estadoPago": "pagado" }
```

### 2. Buscar Pedidos por Email
```
GET /api/pedidos/email/{email}
```

### 3. Buscar Pedidos por Estado de Pago
```
GET /api/pedidos/estado-pago/{estadoPago}
```

---

## 📝 Estados Disponibles

### Estados de Pedido
- `pendiente` - Pedido creado, esperando procesamiento
- `confirmado` - Pedido confirmado
- `enviado` - Pedido en camino
- `entregado` - Pedido entregado al cliente
- `cancelado` - Pedido cancelado

### Estados de Pago
- `pendiente` - Pago no realizado
- `pagado` - Pago exitoso
- `fallido` - Pago rechazado
- `reembolsado` - Pago devuelto

### Métodos de Pago
- `tarjeta_credito`
- `tarjeta_debito`
- `paypal`
- `transferencia`
- `webpay`
- `mercadopago`

---

## ✅ Verificación

### Compilación
```bash
.\mvnw.cmd clean compile
```
**Resultado:** ✅ BUILD SUCCESS

### Estructura de Tablas
Al iniciar la aplicación, Hibernate creará automáticamente las tablas:
- `pedidos` - Con todos los campos de cliente, envío y pago
- `detalle_pedido` - Con información completa del producto

---

## 🎉 Beneficios

1. ✅ **Información completa del cliente** en cada pedido
2. ✅ **Datos de envío detallados** para logística
3. ✅ **Tallas** en los detalles del pedido
4. ✅ **Imágenes** guardadas en el detalle (por si el producto cambia)
5. ✅ **Nombres de productos** guardados (por si se elimina el producto)
6. ✅ **Estados separados** para pedido y pago
7. ✅ **Cálculo automático** de subtotales
8. ✅ **Trazabilidad completa** del pedido

---

## 📦 Archivos Modificados

- ✅ `model/Pedido.java` - Campos agregados
- ✅ `model/DetallePedido.java` - Campos agregados
- ✅ `service/PedidoService.java` - Lógica actualizada
- ✅ `controller/PedidoController.java` - Endpoints nuevos
- ✅ `repository/PedidoRepository.java` - Métodos de búsqueda
- ✅ `config/DataLoader.java` - Datos de prueba actualizados

---

**Estado:** ✅ COMPLETADO Y FUNCIONANDO

**Fecha:** 15/12/2025

**Compilación:** ✅ BUILD SUCCESS

