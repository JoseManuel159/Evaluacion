package com.example.mscatalogo.mapper;

import com.example.mscatalogo.dto.ProductoDTO;
import com.example.mscatalogo.entity.Producto;

public class ProductoMapper {
    public static ProductoDTO toDTO(Producto producto) {
        ProductoDTO dto = new ProductoDTO();
        dto.setId(producto.getId());
        dto.setCodigo(producto.getCodigo());
        dto.setNombre(producto.getNombre());
        dto.setDescripcion(producto.getDescripcion());
        dto.setCantidad(producto.getCantidad());
        dto.setPrecioVenta(producto.getPrecioVenta());
        dto.setCostoCompra(producto.getCostoCompra());
        dto.setEstado(producto.isEstado());
        dto.setImagen(producto.getImagen());
        dto.setUrlImagen(producto.getUrlImagen()); // ✅ esto es clave
        if (producto.getCategoria() != null) {
            dto.setCategoriaId(producto.getCategoria().getId());
            dto.setCategoriaNombre(producto.getCategoria().getNombre());
        }
        return dto;
    }
}
