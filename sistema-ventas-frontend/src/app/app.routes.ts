import { Routes } from '@angular/router';
import {LoginComponent} from "./pages/login-component/login-component.component";
import {CategoriasComponent} from "./pages/categorias/categorias.component";

export const routes: Routes = [
  { path: 'login', component: LoginComponent },
  { path: 'categorias', component: CategoriasComponent },
  { path: '', redirectTo: '/login', pathMatch: 'full' }
];
