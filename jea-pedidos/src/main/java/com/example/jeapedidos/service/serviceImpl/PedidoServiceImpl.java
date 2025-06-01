package com.example.jeapedidos.service.serviceImpl;

import com.example.jeapedidos.dto.Categoria;
import com.example.jeapedidos.dto.Cliente;
import com.example.jeapedidos.dto.FormaPago;
import com.example.jeapedidos.dto.Producto;
import com.example.jeapedidos.entity.Pedido;
import com.example.jeapedidos.entity.PedidoDetalle;
import com.example.jeapedidos.feing.CategoriaFeign;
import com.example.jeapedidos.feing.ClienteFeign;
import com.example.jeapedidos.feing.FormaPagoFeign;
import com.example.jeapedidos.feing.ProductoFeign;
import com.example.jeapedidos.repository.PedidoRepository;
import com.example.jeapedidos.service.PedidoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class PedidoServiceImpl implements PedidoService {

    @Autowired
    private PedidoRepository pedidoRepository;

    @Autowired
    private ClienteFeign clienteFeign;

    @Autowired
    private ProductoFeign productoFeign;

    @Autowired
    private CategoriaFeign categoriaFeign;

    @Autowired
    private FormaPagoFeign formaPagoFeign;


    @Override
    public Pedido createPedido(Pedido pedido) {
        // Asegurar fechaEntrega 1 semana después de fechaPedido
        if (pedido.getFechaPedido() == null) {
            pedido.setFechaPedido(LocalDateTime.now());
        }
        pedido.setFechaEntrega(pedido.getFechaPedido().toLocalDate().plusWeeks(1));
        return pedidoRepository.save(pedido);
    }

    @Override
    public List<Pedido> getAllPedidos() {
        return pedidoRepository.findAll();
    }

    @Override
    public Optional<Pedido> getPedidoById(Integer id) {
        Pedido pedido = pedidoRepository.findById(id).orElse(null);
        if (pedido == null) return Optional.empty();

        Cliente cliente = clienteFeign.listarcliente(pedido.getClienteId()).getBody();
        pedido.setCliente(cliente);

        List<PedidoDetalle> detalles = pedido.getDetalle().stream().map(detalle -> {
            Producto producto = productoFeign.listarProducto(detalle.getProductoId()).getBody();
            detalle.setProducto(producto);
            return detalle;
        }).collect(Collectors.toList());

        FormaPago formaPago = formaPagoFeign.obtenerFormaPago(pedido.getFormapagoId()).getBody();
        pedido.setFormaPago(formaPago);

        pedido.setDetalle(detalles);
        return Optional.of(pedido);
    }

    @Override
    public Pedido updatePedido(Integer id, Pedido pedido) {
        return pedidoRepository.save(pedido);
    }

    @Override
    public void deletePedido(Integer id) {
        pedidoRepository.deleteById(id);
    }
}
