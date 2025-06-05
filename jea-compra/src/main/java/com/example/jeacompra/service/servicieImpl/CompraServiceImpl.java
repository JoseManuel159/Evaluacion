package com.example.jeacompra.service.servicieImpl;

import com.example.jeacompra.dto.FormaPago;
import com.example.jeacompra.dto.Producto;
import com.example.jeacompra.entity.Compra;
import com.example.jeacompra.entity.CompraDetalle;
import com.example.jeacompra.feign.FormaPagoFeign;
import com.example.jeacompra.feign.ProductoFeign;
import com.example.jeacompra.repository.CompraRepository;
import com.example.jeacompra.service.CompraService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CompraServiceImpl implements CompraService {

    @Autowired
    private CompraRepository compraRepository;

    @Autowired
    private ProductoFeign productoFeign;

    @Autowired
    private FormaPagoFeign formaPagoFeign;


    @Override
    public Compra createCompra(Compra compra) {
        Compra nuevaCompra = compraRepository.save(compra);

        for (CompraDetalle detalle : nuevaCompra.getDetalle()) {
            Long productoId = detalle.getProductoId();
            Double cantidadComprada = detalle.getCantidad();

            ResponseEntity<Producto> response = productoFeign.listarProducto(productoId);
            if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
                Producto producto = response.getBody();

                if (!producto.isEstado()) {
                    throw new RuntimeException("El producto con ID " + productoId + " está inactivo y no puede usarse en compras.");
                }

                // Calcular nueva cantidad y llamar solo al endpoint de cantidad
                int nuevaCantidad = producto.getCantidad() + cantidadComprada.intValue();
                productoFeign.actualizarCantidad(productoId, nuevaCantidad);
            } else {
                throw new RuntimeException("Producto con ID " + productoId + " no encontrado al registrar compra.");
            }
        }

        return nuevaCompra;
    }

    @Override
    public List<Compra> getAllCompra() {
        return compraRepository.findAll();
    }

    @Override
    public Optional<Compra> getCompraById(Long id) {
        Compra compra = compraRepository.findById(id).orElse(null);

        if (compra == null) return Optional.empty();


        List<CompraDetalle> detalles = compra.getDetalle().stream().map(detalle -> {
            Producto producto = productoFeign.listarProducto(detalle.getProductoId()).getBody();
            detalle.setProducto(producto);
            return detalle;
        }).collect(Collectors.toList());

        FormaPago formaPago = formaPagoFeign.obtenerFormaPago(compra.getFormapagoId()).getBody();
        compra.setFormaPago(formaPago);

        compra.setDetalle(detalles);
        return Optional.of(compra);
    }

    @Override
    public Compra updateCompra(Long id, Compra compra) {
        return compraRepository.save(compra);
    }

    @Override
    public void deleteCompra(Long id) {
        compraRepository.deleteById(id);
    }
}
