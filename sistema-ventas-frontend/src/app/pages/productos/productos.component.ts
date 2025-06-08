import {Component, OnInit} from '@angular/core';
import {Producto} from "../../modelo/Producto";
import {ProductoService} from "../../services/producto.service";
import {
  MatCell,
  MatCellDef,
  MatColumnDef,
  MatHeaderCell,
  MatHeaderCellDef,
  MatHeaderRow, MatHeaderRowDef, MatRow, MatRowDef,
  MatTable
} from "@angular/material/table";
import {DecimalPipe, NgClass, NgIf} from "@angular/common";

@Component({
  selector: 'app-productos',
  standalone: true,
  imports: [
    MatTable,
    NgIf,
    MatColumnDef,
    MatHeaderCell,
    MatCell,
    MatCellDef,
    MatHeaderCellDef,
    MatHeaderRow,
    MatHeaderRowDef,
    MatRow,
    MatRowDef,
    DecimalPipe,
    NgClass
  ],
  templateUrl: './productos.component.html',
  styleUrl: './productos.component.css'
})
export class ProductosComponent implements OnInit {
  productos: Producto[] = [];
  columnas: string[] = ['id', 'codigo', 'nombre', 'cantidad', 'precioVenta', 'estado', 'categoria'];

  constructor(private productoService: ProductoService) {}

  ngOnInit(): void {
    this.productoService.listar().subscribe({
      next: data => this.productos = data,
      error: err => console.error('Error al obtener productos', err)
    });
  }
}
