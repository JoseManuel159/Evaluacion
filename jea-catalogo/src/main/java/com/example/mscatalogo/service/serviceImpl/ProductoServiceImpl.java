package com.example.mscatalogo.service.serviceImpl;

import com.example.mscatalogo.entity.Producto;
import com.example.mscatalogo.repository.ProductoRepository;
import com.example.mscatalogo.service.ProductoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProductoServiceImpl implements ProductoService {

    @Autowired
    private ProductoRepository productoRepository;

    @Override
    public Producto guardar(Producto producto) {
        boolean existeCodigo = productoRepository.existsByCodigo(producto.getCodigo());
        boolean existeNombre = productoRepository.existsByNombre(producto.getNombre());

        if (existeCodigo || existeNombre) {
            throw new RuntimeException("Ya existe un producto con ese código o nombre");
        }

        return productoRepository.save(producto);
    }

    @Override
    public List<Producto> listar() {
        return productoRepository.findAll();
    }

    @Override
    public Optional<Producto> obtenerPorId(Long id) {
        return productoRepository.findById(id);
    }

    @Override
    public Producto actualizar(Long id, Producto productoActualizado) {
        Optional<Producto> productoExistente = productoRepository.findById(id);

        if (productoExistente.isEmpty()) {
            throw new RuntimeException("Producto no encontrado");
        }

        Producto producto = productoExistente.get();
        producto.setCodigo(productoActualizado.getCodigo());
        producto.setNombre(productoActualizado.getNombre());
        producto.setDescripcion(productoActualizado.getDescripcion());
        producto.setCantidad(productoActualizado.getCantidad());
        producto.setPrecioVenta(productoActualizado.getPrecioVenta());
        producto.setCostoCompra(productoActualizado.getCostoCompra());
        producto.setCategoria(productoActualizado.getCategoria());
        producto.setEstado(productoActualizado.isEstado());

        return productoRepository.save(producto);
    }

    @Override
    public void eliminar(Long id) {
        productoRepository.deleteById(id);
    }

    @Override
    public void desactivar(Long id) {
        Optional<Producto> producto = productoRepository.findById(id);

        if (producto.isPresent()) {
            Producto prod = producto.get();
            prod.setEstado(false);
            productoRepository.save(prod);
        } else {
            throw new RuntimeException("Producto no encontrado");
        }
    }
}
