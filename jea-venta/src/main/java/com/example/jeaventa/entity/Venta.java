package com.example.jeaventa.entity;

import com.example.jeaventa.dto.Cliente;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.List;

@Entity
public class Venta {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String serie;
    private String numero;
    private String descripcion;

    @Column(name = "cliente_id")
    private Long clienteId;

    @Transient
    private Cliente cliente;

    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "venta_id")
    private List<VentaDetalle> detalle;

    private LocalDateTime fechaVenta;

    public Venta() {
        this.fechaVenta = LocalDateTime.now();
    }

    public Venta(Integer id, String serie, String numero, String descripcion, Long clienteId, Cliente cliente, List<VentaDetalle> detalle, LocalDateTime fechaVenta) {
        this.id = id;
        this.serie = serie;
        this.numero = numero;
        this.descripcion = descripcion;
        this.clienteId = clienteId;
        this.cliente = cliente;
        this.detalle = detalle;
        this.fechaVenta = fechaVenta;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getSerie() {
        return serie;
    }

    public void setSerie(String serie) {
        this.serie = serie;
    }

    public String getNumero() {
        return numero;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Long getClienteId() {
        return clienteId;
    }

    public void setClienteId(Long clienteId) {
        this.clienteId = clienteId;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    public List<VentaDetalle> getDetalle() {
        return detalle;
    }

    public void setDetalle(List<VentaDetalle> detalle) {
        this.detalle = detalle;
    }

    public LocalDateTime getFechaVenta() {
        return fechaVenta;
    }

    public void setFechaVenta(LocalDateTime fechaVenta) {
        this.fechaVenta = fechaVenta;
    }
}
