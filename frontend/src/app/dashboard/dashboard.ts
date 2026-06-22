import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { ApiService } from '../services/api';

@Component({
  selector: 'app-dashboard',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './dashboard.html',
  styleUrl: './dashboard.css'
})
export class DashboardComponent {
  sites: any[] = [];
  alerts: any[] = [];
  loading = false;
  loggedIn = false;
  email = '';
  password = '';
  error = '';

  constructor(private api: ApiService) {}

  login() {
    this.error = '';
    this.api.login(this.email, this.password).subscribe({
      next: (data) => {
        this.api.setToken(data.token);
        this.loggedIn = true;
        this.loadData();
      },
      error: () => { this.error = 'Email ou mot de passe incorrect'; }
    });
  }

  loadData() {
    this.loading = true;
    this.api.getSites().subscribe({
      next: (data) => { this.sites = data; this.loading = false; },
      error: () => { this.loading = false; }
    });
    this.api.getAlerts().subscribe({
      next: (data) => { this.alerts = data; },
      error: () => {}
    });
  }
}
