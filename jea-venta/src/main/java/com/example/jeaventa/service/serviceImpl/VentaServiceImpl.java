package com.example.jeaventa.service.serviceImpl;

import com.example.jeaventa.dto.Cliente;
import com.example.jeaventa.dto.Producto;
import com.example.jeaventa.entity.Venta;
import com.example.jeaventa.entity.VentaDetalle;
import com.example.jeaventa.feign.ClienteFeign;
import com.example.jeaventa.feign.ProductoFeign;
import com.example.jeaventa.repository.VentaRepository;
import com.example.jeaventa.service.VentaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class VentaServiceImpl implements VentaService {

    @Autowired
    private VentaRepository ventaRepository;

    @Autowired
    private ClienteFeign clienteFeign;

    @Autowired
    private ProductoFeign productoFeign;

    @Override
    public Venta createVenta(Venta venta) {
        return ventaRepository.save(venta);
    }

    @Override
    public List<Venta> getAllVentas() {
        return ventaRepository.findAll();
    }

    @Override
    public Optional<Venta> getVentaById(Integer id) {
        Venta venta = ventaRepository.findById(id).orElse(null);

        if (venta == null) return Optional.empty();

        Cliente cliente = clienteFeign.listarcliente(venta.getClienteId()).getBody();
        venta.setCliente(cliente);

        List<VentaDetalle> detalles = venta.getDetalle().stream().map(detalle -> {
            Producto producto = productoFeign.listarProducto(detalle.getProductoId()).getBody();
            detalle.setProducto(producto);
            return detalle;
        }).collect(Collectors.toList());

        venta.setDetalle(detalles);
        return Optional.of(venta);
    }

    @Override
    public Venta updateVenta(Integer id, Venta venta) {
        return ventaRepository.save(venta);
    }

    @Override
    public void deleteVenta(Integer id) {
        ventaRepository.deleteById(id);
    }
}
