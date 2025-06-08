import { Routes } from '@angular/router';
import {LoginComponent} from "./pages/login-component/login-component.component";
import {CategoriasComponent} from "./pages/categorias/categorias.component";
import {HomeLayoutComponent} from "./pages/home-layout.component";
import {AuthGuard} from "./guards/auth.guard";
import {ClientesComponent} from "./pages/clientes/clientes.component";
import {ProductosComponent} from "./pages/productos/productos.component";

export const routes: Routes = [
  {
    path: '',
    component: HomeLayoutComponent,
    canActivate: [AuthGuard], // 🔒 Protege el layout principal
    children: [
      { path: 'categorias', component: CategoriasComponent },
      { path: 'cliente', component: ClientesComponent },
      { path: 'producto', component: ProductosComponent },
      { path: '', redirectTo: 'categorias', pathMatch: 'full' }
    ]
  },
  { path: 'login', component: LoginComponent },
  { path: '**', redirectTo: '' }
];
