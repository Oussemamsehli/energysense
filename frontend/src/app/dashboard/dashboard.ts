import { Component, OnInit, AfterViewInit, ElementRef, ViewChild } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule, Router } from '@angular/router';
import { ApiService } from '../services/api';
import { AuthService } from '../services/auth.service';
import { Chart, registerables } from 'chart.js';

Chart.register(...registerables);

@Component({
  selector: 'app-dashboard',
  standalone: true,
  imports: [CommonModule, RouterModule],
  templateUrl: './dashboard.html',
  styleUrl: './dashboard.css'
})
export class DashboardComponent implements OnInit, AfterViewInit {
  @ViewChild('consumptionChart') chartRef!: ElementRef;
  sites: any[] = [];
  alerts: any[] = [];
  loading = true;
  chart: any;
  currentTime = new Date().toLocaleTimeString('fr-FR');

  constructor(private api: ApiService, private auth: AuthService, private router: Router) {}

  ngOnInit() {
    this.loadData();
    setInterval(() => this.currentTime = new Date().toLocaleTimeString('fr-FR'), 1000);
  }

  ngAfterViewInit() { this.initChart(); }

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

  initChart() {
    if (!this.chartRef) return;
    const ctx = this.chartRef.nativeElement.getContext('2d');
    const labels = ['00:00','02:00','04:00','06:00','08:00','10:00','12:00','14:00','16:00','18:00','20:00','22:00'];
    const data = [42,38,35,40,68,95,112,98,105,88,72,55];
    this.chart = new Chart(ctx, {
      type: 'line',
      data: {
        labels,
        datasets: [{
          label: 'Consommation (kWh)',
          data,
          borderColor: '#1D9E75',
          backgroundColor: 'rgba(29,158,117,0.1)',
          fill: true,
          tension: 0.4,
          pointRadius: 3,
          pointBackgroundColor: '#1D9E75'
        }]
      },
      options: {
        responsive: true,
        maintainAspectRatio: false,
        plugins: { legend: { display: false } },
        scales: {
          x: { grid: { color: 'rgba(255,255,255,0.05)' }, ticks: { color: '#888', font: { size: 11 } } },
          y: { grid: { color: 'rgba(255,255,255,0.05)' }, ticks: { color: '#888', font: { size: 11 } } }
        }
      }
    });
  }

  logout() { this.auth.logout(); this.router.navigate(['/login']); }
}
