package com.example.jeacompra.controller;

import com.example.jeacompra.entity.Compra;
import com.example.jeacompra.service.CompraService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/compra")
public class CompraController {

    @Autowired
    private CompraService compraService;

    @PostMapping
    public ResponseEntity<Compra> crearCompra(@RequestBody Compra compra) {
        Compra nuevaCompra = compraService.createCompra(compra);
        return ResponseEntity.ok(nuevaCompra);
    }

    @GetMapping
    public List<Compra> listarCompras() {
        return compraService.getAllCompra();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Compra> obtenerCompra(@PathVariable Long id) {
        return compraService.getCompraById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<Compra> actualizarCompra(@PathVariable Long id, @RequestBody Compra compra) {
        Compra actualizada = compraService.updateCompra(id, compra);
        return ResponseEntity.ok(actualizada);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminarCompra(@PathVariable Long id) {
        compraService.deleteCompra(id);
        return ResponseEntity.noContent().build();
    }
}
