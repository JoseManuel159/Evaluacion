import {Component, NgIterable, OnInit} from '@angular/core';
import {MatFormField, MatFormFieldModule} from "@angular/material/form-field";
import {MatInput, MatInputModule} from "@angular/material/input";
import {FormsModule} from "@angular/forms";
import {MatIcon, MatIconModule} from "@angular/material/icon";
import {DecimalPipe, NgForOf, NgIf} from "@angular/common";
import {MatButton, MatButtonModule, MatIconButton} from "@angular/material/button";
import {Cliente} from "../../modelo/Cliente";
import {ClienteService} from "../../services/cliente.service";
import {MatDialog} from "@angular/material/dialog";
import {MatOption, MatSelect, MatSelectModule} from "@angular/material/select";
import {Categoria} from "../../modelo/Categoria";
import {Producto} from "../../modelo/Producto";
import {ProductoService} from "../../services/producto.service";
import {CategoriaService} from "../../services/categoria.service";
import {DetalleVenta} from "../../modelo/DetalleVenta";
import {VentaService} from "../../services/venta.service";
import {CanastaService} from "../../services/canasta.service";

@Component({
  selector: 'app-ventas',
  standalone: true,
  imports: [
    MatIconModule,
    MatFormField,
    MatInput,
    FormsModule,
    MatIcon,
    NgIf,
    MatIconButton,
    NgForOf,
    MatButton,
    MatSelect,
    MatOption,
    DecimalPipe,
    MatFormFieldModule,
    MatFormFieldModule,
    MatInputModule,
    MatSelectModule,
    MatButtonModule,
    MatIconModule,
  ],
  templateUrl: './ventas.component.html',
  styleUrl: './ventas.component.css'
})
// reemplaza tu VentasComponent por este
export class VentasComponent implements OnInit {
  dniCliente: string = '';
  clienteSeleccionado: Cliente | null = null;
  productos: Producto[] = [];
  productosFiltrados: Producto[] = [];
  categorias: Categoria[] = [];
  codigoBusqueda: string = '';
  categoriaSeleccionada: number | null = null;
  canasta: DetalleVenta[] = [];
  formapagoId: number = 1; // Por defecto efectivo


  constructor(
    private clienteService: ClienteService,
    private dialog: MatDialog,
    private productoService: ProductoService,
    private categoriaService: CategoriaService,
    private canastaService: CanastaService,
    private ventaService: VentaService
  ) {}

  ngOnInit(): void {
    this.cargarProductos();
    this.cargarCategorias();
    this.actualizarCanasta();
  }

  actualizarCanasta(): void {
    this.canasta = this.canastaService.getCanasta();
  }

  eliminarItem(item: DetalleVenta): void {
    this.canastaService.eliminarProducto(item.productoId);
    this.actualizarCanasta();
  }

  get totalPagar(): number {
    return this.canastaService.getTotal();
  }

  generarVenta(): void {
    if (!this.clienteSeleccionado) {
      alert("Debe seleccionar un cliente para continuar.");
      return;
    }

    if (this.canasta.length === 0) {
      alert("No hay productos en la canasta.");
      return;
    }

    const detalle = this.canasta.map(item => ({
      productoId: item.productoId,
      cantidad: item.cantidad
    }));

    const venta = {
      clienteId: this.clienteSeleccionado.id,
      detalle: detalle,
      formapagoId: this.formapagoId
    };

    this.ventaService.registrarVenta(venta).subscribe({
      next: () => {
        alert('Venta registrada con éxito');
        this.canastaService.vaciarCanasta();
        this.actualizarCanasta();
        this.clienteSeleccionado = null;
        this.dniCliente = '';
      },
      error: (err) => {
        console.error(err);
        alert('Error al registrar venta');
      }
    });
  }

  buscarCliente() {
    if (!this.dniCliente) return;

    this.clienteService.obtenerPorDni(this.dniCliente).subscribe({
      next: (cliente) => {
        this.clienteSeleccionado = cliente;
      },
      error: () => {
        this.clienteSeleccionado = null;
        alert("Cliente no encontrado. Puedes agregarlo.");
      }
    });
  }

  abrirDialogoAgregarCliente() {
    // Aquí puedes abrir un diálogo con un formulario para agregar un nuevo cliente.
  }

  cargarProductos(): void {
    this.productoService.listar().subscribe(productos => {
      this.productos = productos;
      this.productosFiltrados = productos;
    });
  }

  cargarCategorias(): void {
    this.categoriaService.listar().subscribe(categorias => {
      this.categorias = categorias;
    });
  }

  buscarPorCodigo(): void {
    if (!this.codigoBusqueda) return;

    this.productoService.buscarPorCodigo(this.codigoBusqueda).subscribe({
      next: producto => {
        this.productosFiltrados = [producto];
      },
      error: () => {
        this.productosFiltrados = [];
      }
    });
  }

  filtrarPorCategoria(): void {
    if (!this.categoriaSeleccionada) {
      this.productosFiltrados = [...this.productos];
    } else {
      this.productosFiltrados = this.productos.filter(p => p.categoria.id === this.categoriaSeleccionada);
    }
  }

  agregarACanasta(producto: Producto): void {
    if (producto.id == null) {
      console.error('El producto no tiene ID. No se puede agregar a la canasta.');
      return;
    }

    const yaExiste = this.canasta.find(item => item.productoId === producto.id);

    if (yaExiste) {
      yaExiste.cantidad += 1;
    } else {
      this.canastaService.agregarProducto({
        productoId: producto.id,
        cantidad: 1,
        producto: producto
      });
    }

    this.actualizarCanasta();
  }

}
