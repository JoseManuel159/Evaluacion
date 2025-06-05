import { Component } from '@angular/core';
import { RouterOutlet } from '@angular/router';
import {CategoriasComponent} from "./pages/categorias/categorias.component";

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [RouterOutlet, CategoriasComponent],
  templateUrl: './app.component.html',
  styleUrl: './app.component.css'
})
export class AppComponent {
  title = 'sistema-ventas-frontend';
}
