import { Component } from '@angular/core';
import {FormsModule} from "@angular/forms";
import {MatDialogRef} from "@angular/material/dialog";
import {MatButton, MatButtonModule, MatIconButton} from "@angular/material/button";
import {MatIcon, MatIconModule} from "@angular/material/icon";
import {ProductoService} from "../../../services/producto.service";
import {Producto} from "../../../modelo/Producto";
import {MatFormField, MatFormFieldModule} from "@angular/material/form-field";
import {MatInput, MatInputModule} from "@angular/material/input";
import {CommonModule, NgIf} from "@angular/common";
import {MaterialModule} from "../../../material/material.module";
import {MatOption, MatSelect} from "@angular/material/select";
import {Categoria} from "../../../modelo/Categoria";
import {CategoriaService} from "../../../services/categoria.service";

@Component({
  selector: 'app-formproducto',
  standalone: true,
  imports: [
    FormsModule,
    MatIconButton,
    MatIcon,
    MatFormField,
    MatInput,
    NgIf,
    MatButton,
    MaterialModule,
    CommonModule,
    FormsModule,
    NgIf,
    MatFormFieldModule,
    MatInputModule,
    MatIconModule,
    MatButtonModule,
    MatSelect,
    MatOption
  ],
  templateUrl: './formproducto.component.html',
  styleUrl: './formproducto.component.css'
})
export class FormproductoComponent {
  producto: Producto = {
    codigo: '',
    nombre: '',
    descripcion: '',
    costoCompra: 0,
    precioVenta: 0,
    cantidad: 0,
    categoria: { id: 0, nombre: '' }  // ✅ obligatorio
  };

  imagenSeleccionada?: File;

  categorias: Categoria[] = [];

  ngOnInit() {
    this.cargarCategorias();
  }

  cargarCategorias() {
    this.categoriaService.listar().subscribe({
      next: data => {
        this.categorias = data;
      },
      error: err => {
        console.error('Error al cargar categorías', err);
      }
    });
  }

  constructor(
    private dialogRef: MatDialogRef<FormproductoComponent>,
    private productoService: ProductoService,
    private categoriaService: CategoriaService
  ) {}

  cerrar() {
    this.dialogRef.close();
  }

  guardarProducto() {
    // Ajustamos campos automáticos antes de enviar
    const nuevoProducto: Producto = {
      ...this.producto,
      estado: true
      // no enviamos fechas ni imagen (las genera el backend)
    };

    this.productoService.crearConImagen(nuevoProducto, this.imagenSeleccionada).subscribe({
      next: () => this.dialogRef.close(true),
      error: err => console.error('Error al guardar producto:', err)
    });
  }

  onFileSelected(event: Event) {
    const input = event.target as HTMLInputElement;
    if (input.files?.length) {
      this.imagenSeleccionada = input.files[0];
    }
  }

}
