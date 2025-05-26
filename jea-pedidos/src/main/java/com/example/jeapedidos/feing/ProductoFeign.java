package com.example.jeapedidos.feing;

import com.example.jeapedidos.dto.Producto;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "jea-catalogo-service", path = "/producto")
public interface ProductoFeign {

    @GetMapping("/{id}")
    @CircuitBreaker(name = "orderByIdCB", fallbackMethod = "fallbackProductById")
    public ResponseEntity<Producto> listarProducto(@PathVariable Long id);


    default ResponseEntity<Producto> fallbackProductById(Long id, Exception e) {
        Producto productDto = new Producto();
        productDto.setNombre("Servicio no disponible de producto");
        return ResponseEntity.ok(productDto);
    }
}
