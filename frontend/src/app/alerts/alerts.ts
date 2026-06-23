import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule, Router } from '@angular/router';
import { ApiService } from '../services/api';
import { AuthService } from '../services/auth.service';

@Component({
  selector: 'app-alerts',
  standalone: true,
  imports: [CommonModule, RouterModule],
  templateUrl: './alerts.html',
  styleUrl: './alerts.css'
})
export class AlertsComponent implements OnInit {
  alerts: any[] = [];
  loading = true;

  constructor(private api: ApiService, private auth: AuthService, private router: Router) {}

  ngOnInit() { this.loadAlerts(); }

  loadAlerts() {
    this.loading = true;
    this.api.getAlerts().subscribe({
      next: (data) => { this.alerts = data; this.loading = false; },
      error: () => { this.loading = false; }
    });
  }

  logout() { this.auth.logout(); this.router.navigate(['/login']); }
}
