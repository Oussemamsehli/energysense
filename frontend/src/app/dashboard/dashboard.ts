// src/app/dashboard/dashboard.ts
import {
  Component, OnInit, AfterViewInit, OnDestroy,
  ElementRef, ViewChild, computed, signal, inject,
} from '@angular/core';
import { RouterModule } from '@angular/router';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { catchError, of } from 'rxjs';
import { Chart, registerables } from 'chart.js';

Chart.register(...registerables);

type SiteStatus = 'online' | 'warning' | 'offline';

interface SiteRow {
  id: number;
  name: string;
  location: string;
  status: SiteStatus;
  sensors: number;
  load: number;
  power: number;
}

interface AlertRow {
  id: number;
  severity: 'critical' | 'warning' | 'info';
  title: string;
  acknowledged: boolean;
}

const API = 'http://192.168.49.2:30080';

@Component({
  selector: 'app-dashboard',
  standalone: true,
  imports: [RouterModule],
  templateUrl: './dashboard.html',
  styleUrl: './dashboard.css',
})
export class DashboardComponent implements OnInit, AfterViewInit, OnDestroy {
  @ViewChild('consumptionChart') chartRef!: ElementRef<HTMLCanvasElement>;

  private http        = inject(HttpClient);
  private chart?:       Chart;
  private clockTimer?:  ReturnType<typeof setInterval>;

  loading     = true;
  currentTime = new Date().toLocaleTimeString('fr-FR');

  private _sites  = signal<SiteRow[]>([]);
  private _alerts = signal<AlertRow[]>([]);

  sitesData = computed(() => ({
    list:    this._sites(),
    total:   this._sites().length,
    sensors: this._sites().reduce((s, x) => s + x.sensors, 0),
  }));

  alertsData = computed(() =>
    this._alerts().filter(a => !a.acknowledged)
  );

  totalPower = computed(() =>
    this._sites().reduce((s, x) => s + x.power, 0)
  );

  ngOnInit(): void {
    this.loadData();
    this.clockTimer = setInterval(
      () => this.currentTime = new Date().toLocaleTimeString('fr-FR'), 1000
    );
  }

  ngAfterViewInit(): void { this.initChart(); }

  ngOnDestroy(): void {
    if (this.clockTimer) clearInterval(this.clockTimer);
    this.chart?.destroy();
  }

  // FIX erreur 2769 : HttpHeaders au lieu d'un objet littéral avec undefined
  // TypeScript refuse { Authorization?: undefined } comme Record<string, string|string[]>
  // HttpHeaders gère l'absence de token proprement sans undefined dans le type
  loadData(): void {
    const token = localStorage.getItem('token')
               || localStorage.getItem('jwt')
               || localStorage.getItem('access_token');

    const headers = token
      ? new HttpHeaders({ Authorization: `Bearer ${token}` })
      : new HttpHeaders();

    this.loading = true;

    // FIX erreur 2345 : Array.isArray() comme guard de type
    // Sans ça, TypeScript peut inférer ArrayBuffer pour certaines surcharges de get()
    this.http.get<SiteRow[]>(`${API}/api/sites`, { headers })
      .pipe(catchError(() => of(null)))
      .subscribe(data => {
        this._sites.set(Array.isArray(data) && data.length ? data : this.demoSites());
        this.loading = false;
      });

    this.http.get<AlertRow[]>(`${API}/api/alerts`, { headers })
      .pipe(catchError(() => of(null)))
      .subscribe(data => {
        this._alerts.set(Array.isArray(data) && data.length ? data : this.demoAlerts());
      });
  }

  private initChart(): void {
    if (!this.chartRef?.nativeElement) return;
    const ctx = this.chartRef.nativeElement.getContext('2d')!;

    const grad = ctx.createLinearGradient(0, 0, 0, 180);
    grad.addColorStop(0, 'rgba(29,158,117,.22)');
    grad.addColorStop(1, 'rgba(29,158,117,0)');

    this.chart = new Chart(ctx, {
      type: 'line',
      data: {
        labels: ['00:00','02:00','04:00','06:00','08:00','10:00',
                 '12:00','14:00','16:00','18:00','20:00','22:00'],
        datasets: [{
          data: [42, 38, 35, 40, 68, 95, 112, 98, 105, 88, 72, 55],
          borderColor: '#1D9E75',
          backgroundColor: grad,
          fill: true,
          tension: 0.4,
          pointRadius: 3,
          pointBackgroundColor: '#1D9E75',
          pointBorderColor: '#111217',
          pointBorderWidth: 1.5,
          borderWidth: 2,
        }],
      },
      options: {
        responsive: true,
        maintainAspectRatio: false,
        animation: { duration: 600 },
        plugins: {
          legend: { display: false },
          tooltip: {
            backgroundColor: '#1e2128',
            borderColor: '#2a2d35',
            borderWidth: 1,
            titleColor: '#f0f1f3',
            bodyColor: '#969ba3',
            padding: 10,
            callbacks: { label: ctx => ` ${ctx.parsed.y} kWh` },
          },
        },
        scales: {
          x: {
            grid: { color: 'rgba(255,255,255,.04)' },
            ticks: { color: '#6e7681', font: { size: 10 }, maxRotation: 0 },
            border: { color: '#2a2d35' },
          },
          y: {
            grid: { color: 'rgba(255,255,255,.04)' },
            ticks: { color: '#6e7681', font: { size: 10 } },
            border: { color: '#2a2d35' },
          },
        },
      },
    });
  }

  private demoSites(): SiteRow[] {
    return [
      { id: 1, name: 'Site Tunis',    location: 'Tunis, Zone industrielle', status: 'online',  sensors: 3,  load: 45, power: 184 },
      { id: 2, name: 'Site Monastir', location: 'Monastir, Salle serveurs', status: 'online',  sensors: 6,  load: 62, power: 256 },
      { id: 3, name: 'Entrepôt Sud',  location: 'Sfax, Stockage froid',     status: 'warning', sensors: 15, load: 88, power: 412 },
      { id: 4, name: 'Bureau Centre', location: 'Tunis, Climatisation',     status: 'offline', sensors: 6,  load: 0,  power: 0   },
    ];
  }

  private demoAlerts(): AlertRow[] {
    return [
      { id: 1, severity: 'critical', title: 'Température critique', acknowledged: false },
      { id: 2, severity: 'critical', title: 'Capteur hors ligne',   acknowledged: false },
      { id: 3, severity: 'warning',  title: 'Charge élevée',        acknowledged: false },
      { id: 4, severity: 'warning',  title: 'Humidité en hausse',   acknowledged: false },
    ];
  }
}