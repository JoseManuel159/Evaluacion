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
        return compraRepository.save(compra);
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
