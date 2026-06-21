import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ApiService } from '../services/api';

@Component({
  selector: 'app-dashboard',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './dashboard.html',
  styleUrl: './dashboard.css'
})
export class DashboardComponent implements OnInit {
  sites: any[] = [];
  alerts: any[] = [];
  loading = true;

  constructor(private api: ApiService) {}

  ngOnInit() {
    this.api.getSites().subscribe({
      next: (data) => { this.sites = data; this.loading = false; },
      error: (err) => { console.error(err); this.loading = false; }
    });

    this.api.getAlerts().subscribe({
      next: (data) => { this.alerts = data; },
      error: (err) => console.error(err)
    });
  }
}
