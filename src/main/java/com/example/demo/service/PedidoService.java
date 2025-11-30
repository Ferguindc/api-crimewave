package com.example.demo.service;

import com.example.demo.model.Pedido;
import com.example.demo.model.DetallePedido;
import com.example.demo.model.Usuario;
import com.example.demo.model.Producto;
import com.example.demo.repository.PedidoRepository;
import com.example.demo.repository.UsuarioRepository;
import com.example.demo.repository.ProductoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
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
        return pedidoRepository.findByEstado(estado);
    }

    @Transactional
    public Pedido createPedido(Pedido pedido) {
        // Verificar que el usuario existe
        Usuario usuario = usuarioRepository.findById(pedido.getUsuario().getId())
            .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        pedido.setUsuario(usuario);
        pedido.setFecha(LocalDateTime.now());
        pedido.setEstado("pendiente");

        // Calcular el total del pedido
        BigDecimal total = BigDecimal.ZERO;
        for (DetallePedido detalle : pedido.getDetalles()) {
            // Verificar que el producto existe
            Producto producto = productoRepository.findById(detalle.getProducto().getId())
                .orElseThrow(() -> new RuntimeException("Producto no encontrado con id: " + detalle.getProducto().getId()));

            detalle.setProducto(producto);
            detalle.setPedido(pedido);

            // Si no se proporciona precio unitario, usar el precio del producto
            if (detalle.getPrecioUnit() == null) {
                detalle.setPrecioUnit(producto.getPrecio());
            }

            BigDecimal subtotal = detalle.getPrecioUnit().multiply(new BigDecimal(detalle.getCantidad()));
            total = total.add(subtotal);
        }

        pedido.setTotal(total);

        return pedidoRepository.save(pedido);
    }

    public Pedido updateEstadoPedido(Long id, String nuevoEstado) {
        Pedido pedido = pedidoRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Pedido no encontrado con id: " + id));

        pedido.setEstado(nuevoEstado);
        return pedidoRepository.save(pedido);
    }

    public void deletePedido(Long id) {
        Pedido pedido = pedidoRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Pedido no encontrado con id: " + id));
        pedidoRepository.delete(pedido);
    }
}

