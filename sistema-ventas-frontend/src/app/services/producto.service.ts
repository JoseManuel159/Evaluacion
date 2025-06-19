import { Injectable } from '@angular/core';
import {HttpClient, HttpHeaders} from "@angular/common/http";
import {Observable} from "rxjs";
import {Producto} from "../modelo/Producto";

@Injectable({
  providedIn: 'root'
})
export class ProductoService {
  private apiUrl = 'http://localhost:8085/producto'; // Ajusta el puerto si es necesario

  constructor(private http: HttpClient) {}

  private getHeaders(): HttpHeaders {
    const token = localStorage.getItem('token');
    return new HttpHeaders({
      'Authorization': `Bearer ${token}`
    });
  }

  listar(): Observable<Producto[]> {
    return this.http.get<Producto[]>(this.apiUrl, {
      headers: this.getHeaders()
    });
  }

  obtenerPorId(id: number): Observable<Producto> {
    return this.http.get<Producto>(`${this.apiUrl}/${id}`, {
      headers: this.getHeaders()
    });
  }

  crear(producto: Producto): Observable<Producto> {
    return this.http.post<Producto>(this.apiUrl, producto, {
      headers: this.getHeaders()
    });
  }

  actualizar(id: number, producto: Producto): Observable<Producto> {
    return this.http.put<Producto>(`${this.apiUrl}/${id}`, producto, {
      headers: this.getHeaders()
    });
  }

  eliminar(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${id}`, {
      headers: this.getHeaders()
    });
  }

  desactivar(id: number): Observable<void> {
    return this.http.put<void>(`${this.apiUrl}/${id}/desactivar`, {}, {
      headers: this.getHeaders()
    });
  }

  actualizarCantidad(id: number, nuevaCantidad: number): Observable<Producto> {
    return this.http.put<Producto>(`${this.apiUrl}/${id}/cantidad`, nuevaCantidad, {
      headers: this.getHeaders()
    });
  }

  crearConImagen(producto: Producto, imagen?: File): Observable<Producto> {
    const formData = new FormData();
    formData.append('codigo', producto.codigo);
    formData.append('nombre', producto.nombre);
    formData.append('cantidad', producto.cantidad?.toString() || '0');
    formData.append('precioVenta', producto.precioVenta?.toString() || '0');
    formData.append('costoCompra', producto.costoCompra?.toString() || '0');
    formData.append('categoriaId', producto.categoria?.id?.toString() || '0');
    if (producto.descripcion) formData.append('descripcion', producto.descripcion);
    if (imagen) formData.append('imagen', imagen);

    return this.http.post<Producto>(`${this.apiUrl}/crear-con-imagen`, formData, {
      headers: this.getHeaders()
    });
  }

  actualizarConImagen(id: number, producto: Producto, nuevaImagen?: File): Observable<Producto> {
    const formData = new FormData();
    formData.append('codigo', producto.codigo);
    formData.append('nombre', producto.nombre);
    formData.append('cantidad', producto.cantidad?.toString() || '0');
    formData.append('precioVenta', producto.precioVenta?.toString() || '0');
    formData.append('costoCompra', producto.costoCompra?.toString() || '0');
    formData.append('estado', producto.estado?.toString() || 'false');
    formData.append('categoriaId', producto.categoria?.id?.toString() || '0');
    if (producto.descripcion) formData.append('descripcion', producto.descripcion);
    if (nuevaImagen) formData.append('imagen', nuevaImagen);

    return this.http.put<Producto>(`${this.apiUrl}/${id}/actualizar-con-imagen`, formData, {
      headers: this.getHeaders()
    });
  }



  buscarPorCodigo(codigo: string): Observable<Producto> {
    return this.http.get<Producto>(`${this.apiUrl}/buscar/codigo/${codigo}`, {
      headers: this.getHeaders()
    });
  }

}
