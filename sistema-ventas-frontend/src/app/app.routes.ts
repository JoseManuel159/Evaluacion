import { Routes } from '@angular/router';
import {LoginComponent} from "./pages/login-component/login-component.component";
import {CategoriasComponent} from "./pages/categorias/categorias.component";
import {HomeLayoutComponent} from "./pages/home-layout.component";
import {AuthGuard} from "./guards/auth.guard";

export const routes: Routes = [
  {
    path: '',
    component: HomeLayoutComponent,
    canActivate: [AuthGuard], // 🔒 Protege el layout principal
    children: [
      { path: 'categorias', component: CategoriasComponent },
      { path: '', redirectTo: 'categorias', pathMatch: 'full' }
    ]
  },
  { path: 'login', component: LoginComponent },
  { path: '**', redirectTo: '' }
];
