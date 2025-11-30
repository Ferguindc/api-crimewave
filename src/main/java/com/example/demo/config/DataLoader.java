package com.example.demo.config;

import com.example.demo.model.*;
import com.example.demo.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;

@Component
public class DataLoader implements CommandLineRunner {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private CategoriaRepository categoriaRepository;

    @Autowired
    private ProductoRepository productoRepository;

    @Autowired
    private PedidoRepository pedidoRepository;

    @Override
    public void run(String... args) throws Exception {
        // Cargar datos de prueba solo si no existen
        if (usuarioRepository.count() == 0) {
            cargarDatosPrueba();
        }
    }

    private void cargarDatosPrueba() {
        // Crear usuarios
        Usuario admin = new Usuario();
        admin.setNombre("Admin");
        admin.setEmail("admin@crimewave.com");
        admin.setPasswordHash("$2a$10$slYQmyNdGzTn7ZLBXBChFOC9f6kFjAqPhccnP6DxlWXx2lPk1C3G6"); // password: admin123
        admin.setRol("admin");
        usuarioRepository.save(admin);

        Usuario cliente = new Usuario();
        cliente.setNombre("Cliente Test");
        cliente.setEmail("cliente@test.com");
        cliente.setPasswordHash("$2a$10$slYQmyNdGzTn7ZLBXBChFOC9f6kFjAqPhccnP6DxlWXx2lPk1C3G6"); // password: admin123
        cliente.setRol("cliente");
        usuarioRepository.save(cliente);

        // Crear categorías
        Categoria ropa = new Categoria();
        ropa.setNombre("Ropa");
        categoriaRepository.save(ropa);

        Categoria accesorios = new Categoria();
        accesorios.setNombre("Accesorios");
        categoriaRepository.save(accesorios);

        Categoria calzado = new Categoria();
        calzado.setNombre("Calzado");
        categoriaRepository.save(calzado);

        // Crear productos
        Producto camiseta = new Producto();
        camiseta.setNombre("Camiseta Básica");
        camiseta.setDescripcion("Camiseta de algodón 100%");
        camiseta.setPrecio(new BigDecimal("19.99"));
        camiseta.setStock(50);
        camiseta.setImagenUrl("https://via.placeholder.com/300");
        camiseta.getCategorias().add(ropa);
        productoRepository.save(camiseta);

        Producto gorra = new Producto();
        gorra.setNombre("Gorra Deportiva");
        gorra.setDescripcion("Gorra ajustable con logo");
        gorra.setPrecio(new BigDecimal("15.99"));
        gorra.setStock(30);
        gorra.setImagenUrl("https://via.placeholder.com/300");
        gorra.getCategorias().add(accesorios);
        productoRepository.save(gorra);

        Producto zapatillas = new Producto();
        zapatillas.setNombre("Zapatillas Running");
        zapatillas.setDescripcion("Zapatillas deportivas para correr");
        zapatillas.setPrecio(new BigDecimal("89.99"));
        zapatillas.setStock(20);
        zapatillas.setImagenUrl("https://via.placeholder.com/300");
        zapatillas.getCategorias().add(calzado);
        productoRepository.save(zapatillas);

        // Crear un pedido de prueba
        Pedido pedido = new Pedido();
        pedido.setUsuario(cliente);
        pedido.setFecha(LocalDateTime.now());
        pedido.setEstado("pendiente");

        DetallePedido detalle1 = new DetallePedido();
        detalle1.setPedido(pedido);
        detalle1.setProducto(camiseta);
        detalle1.setCantidad(2);
        detalle1.setPrecioUnit(camiseta.getPrecio());

        DetallePedido detalle2 = new DetallePedido();
        detalle2.setPedido(pedido);
        detalle2.setProducto(gorra);
        detalle2.setCantidad(1);
        detalle2.setPrecioUnit(gorra.getPrecio());

        pedido.setDetalles(Arrays.asList(detalle1, detalle2));

        // Calcular total
        BigDecimal total = camiseta.getPrecio().multiply(new BigDecimal(2))
                .add(gorra.getPrecio());
        pedido.setTotal(total);

        pedidoRepository.save(pedido);

        System.out.println("✅ Datos de prueba cargados correctamente");
    }
}

