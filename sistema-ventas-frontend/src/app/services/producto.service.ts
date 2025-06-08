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
}
