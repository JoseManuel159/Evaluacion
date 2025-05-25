package com.example.mscatalogo.service;


import com.example.mscatalogo.entity.Producto;

import java.util.List;
import java.util.Optional;

public interface ProductoService {

    Producto guardar(Producto producto);

    List<Producto> listar();

    Optional<Producto> obtenerPorId(Long id);

    Producto actualizar(Long id, Producto productoActualizado);

    void eliminar(Long id);

    void desactivar(Long id);
}