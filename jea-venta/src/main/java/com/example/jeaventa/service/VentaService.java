package com.example.jeaventa.service;

import com.example.jeaventa.entity.Venta;

import java.util.List;
import java.util.Optional;

public interface VentaService {
    Venta createVenta(Venta venta);

    List<Venta> getAllVentas();

    Optional<Venta> getVentaById(Integer id);

    Venta updateVenta(Integer id, Venta venta);

    void deleteVenta(Integer id);
}
