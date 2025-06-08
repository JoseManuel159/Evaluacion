import { Injectable } from '@angular/core';
import {HttpClient, HttpHeaders} from "@angular/common/http";
import {Cliente} from "../modelo/Cliente";
import {Observable} from "rxjs";

@Injectable({
  providedIn: 'root'
})
export class ClienteService {
  private apiUrl = 'http://localhost:8085/cliente';

  constructor(private http: HttpClient) {}

  private getHeaders(): HttpHeaders {
    const token = localStorage.getItem('token');
    return new HttpHeaders({
      'Authorization': `Bearer ${token}`
    });
  }

  listar(): Observable<Cliente[]> {
    return this.http.get<Cliente[]>(this.apiUrl, {
      headers: this.getHeaders()
    });
  }
}
