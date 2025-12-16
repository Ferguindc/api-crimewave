package com.example.demo.service;

import com.example.demo.model.Pedido;
import com.example.demo.model.DetallePedido;
import com.example.demo.model.Producto;
import com.example.demo.repository.PedidoRepository;
import com.example.demo.repository.UsuarioRepository;
import com.example.demo.repository.ProductoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class PedidoService {

    @Autowired
    private PedidoRepository pedidoRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private ProductoRepository productoRepository;

    public List<Pedido> getAllPedidos() {
        return pedidoRepository.findAll();
    }

    public Optional<Pedido> getPedidoById(Long id) {
        return pedidoRepository.findById(id);
    }

    public List<Pedido> getPedidosByUsuario(Long usuarioId) {
        return pedidoRepository.findByUsuarioId(usuarioId);
    }

    public List<Pedido> getPedidosByEstado(String estado) {
        return pedidoRepository.findByEstadoPedido(estado);
    }

    public List<Pedido> getPedidosByEstadoPago(String estadoPago) {
        return pedidoRepository.findByEstadoPago(estadoPago);
    }

    public List<Pedido> getPedidosByEmail(String email) {
        return pedidoRepository.findByEmailCliente(email);
    }

    @Transactional
    public Pedido createPedido(Pedido pedido) {
        // Establecer estados por defecto
        if (pedido.getEstadoPedido() == null) {
            pedido.setEstadoPedido("pendiente");
        }
        if (pedido.getEstadoPago() == null) {
            pedido.setEstadoPago("pendiente");
        }

        // Calcular el total del pedido
        double total = 0.0;
        for (DetallePedido detalle : pedido.getDetalles()) {
            // Si se proporciona productoId, obtener información del producto
            if (detalle.getProductoId() != null) {
                Producto producto = productoRepository.findById(detalle.getProductoId())
                    .orElseThrow(() -> new RuntimeException("Producto no encontrado con id: " + detalle.getProductoId()));

                // Copiar información del producto al detalle
                if (detalle.getNombreProducto() == null) {
                    detalle.setNombreProducto(producto.getNombre());
                }
                if (detalle.getPrecioUnitario() == null) {
                    detalle.setPrecioUnitario(producto.getPrecio().doubleValue());
                }
                if (detalle.getImagenUrl() == null) {
                    detalle.setImagenUrl(producto.getImagenUrl());
                }
            }

            // Establecer la relación con el pedido
            detalle.setPedido(pedido);

            // Calcular subtotal
            if (detalle.getCantidad() != null && detalle.getPrecioUnitario() != null) {
                detalle.setSubtotal(detalle.getCantidad() * detalle.getPrecioUnitario());
                total += detalle.getSubtotal();
            }
        }

        pedido.setTotal(total);

        return pedidoRepository.save(pedido);
    }

    public Pedido updateEstadoPedido(Long id, String nuevoEstado) {
        Pedido pedido = pedidoRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Pedido no encontrado con id: " + id));

        pedido.setEstadoPedido(nuevoEstado);
        return pedidoRepository.save(pedido);
    }

    public Pedido updateEstadoPago(Long id, String nuevoEstadoPago) {
        Pedido pedido = pedidoRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Pedido no encontrado con id: " + id));

        pedido.setEstadoPago(nuevoEstadoPago);
        return pedidoRepository.save(pedido);
    }

    public void deletePedido(Long id) {
        Pedido pedido = pedidoRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Pedido no encontrado con id: " + id));
        pedidoRepository.delete(pedido);
    }
}

