// src/app/login/login.component.ts
import { Component, inject, signal } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { HttpClient } from '@angular/common/http';
import { Router } from '@angular/router';
import { AuthService } from '../services/auth.service';

const API = 'http://192.168.49.2:30080';

@Component({
  selector: 'app-login',
  standalone: true,
  imports: [FormsModule],
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent {
  private http = inject(HttpClient);
  private router = inject(Router);
  private auth = inject(AuthService);

  email = '';
  password = '';
  show = signal(false);
  loading = signal(false);
  error = signal<string | null>(null);
  focus = signal<'email' | 'pwd' | null>(null);

  login(): void {
    if (!this.email || !this.password) {
      this.fail('Renseigne ton email et ton mot de passe.');
      return;
    }
    this.loading.set(true);
    this.error.set(null);

    // 🔌 Adapte le payload (email/username) à ton AuthController et la clé du token à ta réponse
    this.http
      .post<any>(`${API}/auth/login`, { email: this.email, password: this.password })
      .subscribe({
        next: res => {
          const token = res?.token ?? res?.accessToken ?? res?.jwt;
          if (!token) { this.fail('Réponse inattendue du serveur.'); return; }
          this.auth.setToken(token);
          this.router.navigate(['/dashboard']);
        },
        error: err => {
          this.fail(err?.status === 401
            ? 'Identifiants incorrects.'
            : 'Connexion au serveur impossible.');
        },
      });
  }

  private fail(msg: string): void {
    this.loading.set(false);
    this.error.set(msg);
    this.shake.set(true);
    setTimeout(() => this.shake.set(false), 450);
  }

  shake = signal(false);
}
