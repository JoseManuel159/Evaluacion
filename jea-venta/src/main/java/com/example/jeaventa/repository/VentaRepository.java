package com.example.jeaventa.repository;

import com.example.jeaventa.entity.Venta;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface VentaRepository extends JpaRepository<Venta, Integer> {

    List<Venta> findByFechaVentaBetween(LocalDateTime inicio, LocalDateTime fin);

    List<Venta> findBySerie(String serie);

    // Buscar por número exacto
    List<Venta> findByNumero(String numero);

    // Buscar por serie y número
    Optional<Venta> findBySerieAndNumero(String serie, String numero);

}
