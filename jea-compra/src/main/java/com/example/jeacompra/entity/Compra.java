package com.example.jeacompra.entity;

import com.example.jeacompra.dto.FormaPago;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.List;

@Entity
public class Compra {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String serie;
    private String numero;
    private String descripcion;

    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "compra_id")
    private List<CompraDetalle> detalle;

    private LocalDateTime fechaCompra;

    private Double baseImponible;
    private Double igv;
    private Double total;

    @Column(name = "formapago_id")
    private Long formapagoId;

    @Transient
    private FormaPago formaPago;

    public Compra() {
    }

    public Compra(Long id, String serie, String numero, String descripcion, List<CompraDetalle> detalle, LocalDateTime fechaCompra, Double baseImponible, Double igv, Double total, Long formapagoId, FormaPago formaPago) {
        this.id = id;
        this.serie = serie;
        this.numero = numero;
        this.descripcion = descripcion;
        this.detalle = detalle;
        this.fechaCompra = fechaCompra;
        this.baseImponible = baseImponible;
        this.igv = igv;
        this.total = total;
        this.formapagoId = formapagoId;
        this.formaPago = formaPago;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
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

    public List<CompraDetalle> getDetalle() {
        return detalle;
    }

    public void setDetalle(List<CompraDetalle> detalle) {
        this.detalle = detalle;
    }

    public LocalDateTime getFechaCompra() {
        return fechaCompra;
    }

    public void setFechaCompra(LocalDateTime fechaCompra) {
        this.fechaCompra = fechaCompra;
    }

    public Double getBaseImponible() {
        return baseImponible;
    }

    public void setBaseImponible(Double baseImponible) {
        this.baseImponible = baseImponible;
    }

    public Double getIgv() {
        return igv;
    }

    public void setIgv(Double igv) {
        this.igv = igv;
    }

    public Double getTotal() {
        return total;
    }

    public void setTotal(Double total) {
        this.total = total;
    }

    public Long getFormapagoId() {
        return formapagoId;
    }

    public void setFormapagoId(Long formapagoId) {
        this.formapagoId = formapagoId;
    }

    public FormaPago getFormaPago() {
        return formaPago;
    }

    public void setFormaPago(FormaPago formaPago) {
        this.formaPago = formaPago;
    }
}
