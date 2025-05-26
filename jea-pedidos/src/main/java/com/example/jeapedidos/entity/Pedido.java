package com.example.jeapedidos.entity;

import com.example.jeapedidos.dto.Cliente;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Entity
public class Pedido {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String codigo;
    private String descripcion;
    private String estado;

    @Column(name = "cliente_id")
    private Long clienteId;

    @Transient
    private Cliente cliente;

    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "pedido_id")
    private List<PedidoDetalle> detalle;

    private LocalDateTime fechaPedido;
    private LocalDate fechaEntrega;

    public Pedido() {
        this.fechaPedido = LocalDateTime.now();
        this.fechaEntrega = this.fechaPedido.toLocalDate().plusWeeks(1); // Entrega 1 semana después
        this.estado = "PENDIENTE";
    }

    public Pedido(Integer id, String codigo, String descripcion, String estado, Long clienteId, Cliente cliente, List<PedidoDetalle> detalle, LocalDateTime fechaPedido, LocalDate fechaEntrega) {
        this.id = id;
        this.codigo = codigo;
        this.descripcion = descripcion;
        this.estado = estado;
        this.clienteId = clienteId;
        this.cliente = cliente;
        this.detalle = detalle;
        this.fechaPedido = fechaPedido;
        this.fechaEntrega = fechaEntrega;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
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

    public List<PedidoDetalle> getDetalle() {
        return detalle;
    }

    public void setDetalle(List<PedidoDetalle> detalle) {
        this.detalle = detalle;
    }

    public LocalDateTime getFechaPedido() {
        return fechaPedido;
    }

    public void setFechaPedido(LocalDateTime fechaPedido) {
        this.fechaPedido = fechaPedido;
    }

    public LocalDate getFechaEntrega() {
        return fechaEntrega;
    }

    public void setFechaEntrega(LocalDate fechaEntrega) {
        this.fechaEntrega = fechaEntrega;
    }
}