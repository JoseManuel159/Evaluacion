import { Component } from '@angular/core';
import {AuthService} from "../../services/auth-service.service";
import { Router } from '@angular/router';
import {FormsModule} from "@angular/forms";

@Component({
  selector: 'app-login-component',
  standalone: true,
  imports: [
    FormsModule
  ],
  templateUrl: './login-component.component.html',
  styleUrl: './login-component.component.css'
})
export class LoginComponent {
  userName: string = '';
  password: string = '';

  constructor(private authService: AuthService, private router: Router) {}

  login(): void {
    this.authService.login({ userName: this.userName, password: this.password })
      .subscribe({
        next: (res) => {
          localStorage.setItem('token', res.token);

          // Opcional: extraer el id del token
          const payload = JSON.parse(atob(res.token.split('.')[1]));
          localStorage.setItem('userId', payload.id);

          this.router.navigate(['/categorias']); // o tu página principal
        },
        error: () => {
          alert('Credenciales inválidas');
        }
      });
  }
}
