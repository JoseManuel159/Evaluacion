import { Injectable } from '@angular/core';
import {DetalleVenta} from "../modelo/DetalleVenta";

@Injectable({
  providedIn: 'root'
})
export class CanastaService {
  private canasta: DetalleVenta[] = [];

  getCanasta(): DetalleVenta[] {
    return this.canasta;
  }

  agregarProducto(item: DetalleVenta): void {
    const existe = this.canasta.find(i => i.productoId === item.productoId);
    if (existe) {
      existe.cantidad += item.cantidad;
    } else {
      this.canasta.push(item);
    }
  }

  eliminarProducto(productoId: number): void {
    this.canasta = this.canasta.filter(i => i.productoId !== productoId);
  }

  vaciarCanasta(): void {
    this.canasta = [];
  }

  getTotal(): number {
    return this.canasta.reduce((total, item) => total + (item.producto?.precioVenta || 0) * item.cantidad, 0);
  }
}
