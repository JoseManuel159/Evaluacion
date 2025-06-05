package com.example.jeaventa.service.serviceImpl;

import com.example.jeaventa.dto.Cliente;
import com.example.jeaventa.dto.FormaPago;
import com.example.jeaventa.dto.Producto;
import com.example.jeaventa.entity.Venta;
import com.example.jeaventa.entity.VentaDetalle;
import com.example.jeaventa.feign.ClienteFeign;
import com.example.jeaventa.feign.FormaPagoFeign;
import com.example.jeaventa.feign.ProductoFeign;
import com.example.jeaventa.repository.VentaRepository;
import com.example.jeaventa.service.VentaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

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

    @Autowired
    private FormaPagoFeign formaPagoFeign;

    @Override
    public Venta createVenta(Venta venta) {
        Venta nuevaVenta = ventaRepository.save(venta);

        for (VentaDetalle detalle : nuevaVenta.getDetalle()) {
            Long productoId = detalle.getProductoId();
            Double cantidadVendida = detalle.getCantidad();

            ResponseEntity<Producto> response = productoFeign.listarProducto(productoId);

            if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
                Producto producto = response.getBody();

                // ✅ Verificar que el producto esté activo
                if (!producto.isEstado()) {
                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Producto con ID " + productoId + " no está activo.");
                }

                // ✅ Verificar que haya stock suficiente
                if (producto.getCantidad() < cantidadVendida.intValue()) {
                    throw new RuntimeException("Stock insuficiente para el producto con ID " + productoId + ".");
                }

                // ✅ Restar la cantidad vendida del inventario
                int nuevaCantidad = producto.getCantidad() - cantidadVendida.intValue();
                productoFeign.actualizarCantidad(productoId, nuevaCantidad);
            } else {
                throw new RuntimeException("Producto con ID " + productoId + " no encontrado al registrar venta.");
            }
        }

        return nuevaVenta;
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

        FormaPago formaPago = formaPagoFeign.obtenerFormaPago(venta.getFormapagoId()).getBody();
        venta.setFormaPago(formaPago);

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
