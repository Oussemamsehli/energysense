// src/app/sensors/sensor-card.component.ts
import {
  AfterViewInit, Component, ElementRef, Input, OnDestroy,
  ViewChild, computed, inject, signal,
} from '@angular/core';
import { HttpClient } from '@angular/common/http';
import Chart from 'chart.js/auto';

export type SensorType = 'temp' | 'humidity' | 'power' | 'pressure';

export interface Sensor {
  id: number;
  siteId: number;
  name: string;
  type: SensorType;
  unit: string;
  value: number;
  threshold: number;
  history: number[];
}

const API = 'http://192.168.49.2:30080';

/* CARTE CAPTEUR — composant autonome.
   Chaque carte possède son canvas, son graphe Chart.js et son timer. */
@Component({
  selector: 'app-sensor-card',
  standalone: true,
  templateUrl: './sensor-card.component.html',
  styleUrls: ['./sensor-card.component.css'],
})
export class SensorCardComponent implements AfterViewInit, OnDestroy {
  @Input({ required: true }) sensor!: Sensor;
  @ViewChild('cv') canvas!: ElementRef<HTMLCanvasElement>;

  private http = inject(HttpClient);
  private chart?: Chart;
  private timer?: ReturnType<typeof setInterval>;

  // État réactif de la dernière valeur
  current = signal(0);
  previous = signal(0);

  display = computed(() => this.current().toFixed(this.sensor.type === 'power' ? 0 : 1));
  trend = computed(() => +(this.current() - this.previous()).toFixed(1));
  absTrend = computed(() => Math.abs(this.trend()).toFixed(1));
  over = computed(() => this.current() > this.sensor.threshold);

  ngAfterViewInit(): void {
    this.current.set(this.sensor.value);
    this.previous.set(this.sensor.value);
    this.buildChart();
    // Rafraîchissement temps réel toutes les 2,5 s
    this.timer = setInterval(() => this.tick(), 2500);
  }

  ngOnDestroy(): void {
    if (this.timer) clearInterval(this.timer);
    this.chart?.destroy();
  }

  private buildChart(): void {
    const ctx = this.canvas.nativeElement.getContext('2d')!;
    const accent = this.sensor.type === 'power' ? '#e0a64a' : this.sensor.type === 'humidity' ? '#4a9ee0' : '#1D9E75';

    // Dégradé vertical sous la courbe
    const grad = ctx.createLinearGradient(0, 0, 0, 96);
    grad.addColorStop(0, this.hexA(accent, .28));
    grad.addColorStop(1, this.hexA(accent, 0));

    this.chart = new Chart(ctx, {
      type: 'line',
      data: {
        labels: this.sensor.history.map((_, i) => i),
        datasets: [
          {
            data: [...this.sensor.history],
            borderColor: accent,
            backgroundColor: grad,
            borderWidth: 2,
            fill: true,
            tension: 0.4,
            pointRadius: 0,
          },
          {
            // Ligne de seuil (rouge pointillé)
            data: this.sensor.history.map(() => this.sensor.threshold),
            borderColor: 'rgba(226,80,74,.55)',
            borderWidth: 1,
            borderDash: [5, 4],
            pointRadius: 0,
            fill: false,
          },
        ],
      },
      options: {
        responsive: true,
        maintainAspectRatio: false,
        animation: { duration: 0 },           // flux live = pas d'animation à chaque tick
        plugins: { legend: { display: false }, tooltip: { enabled: false } },
        scales: {
          x: { display: false },
          y: {
            display: false,
            suggestedMin: Math.min(...this.sensor.history) * 0.85,
            suggestedMax: Math.max(this.sensor.threshold, ...this.sensor.history) * 1.15,
          },
        },
      },
    });
  }

  private tick(): void {
    const token = localStorage.getItem('token');
    // 🔌 Vraie source : dernières lectures du capteur
    this.http
      .get<{ value: number }[]>(`${API}/api/sensors/${this.sensor.id}/readings?limit=1`, {
        headers: token ? { Authorization: `Bearer ${token}` } : {},
      })
      .subscribe({
        next: r => this.push(r?.length ? r[r.length - 1].value : this.simulate()),
        error: () => this.push(this.simulate()),   // API down → simulation (la démo reste vivante)
      });
  }

  private push(value: number): void {
    const v = +value.toFixed(2);
    this.previous.set(this.current());
    this.current.set(v);

    const ds = this.chart!.data.datasets[0].data as number[];
    const thr = this.chart!.data.datasets[1].data as number[];
    ds.push(v); thr.push(this.sensor.threshold);
    if (ds.length > 24) { ds.shift(); thr.shift(); (this.chart!.data.labels as number[]).push(0); }
    this.chart!.update();
  }

  // Marche aléatoire bornée autour de la valeur courante
  private simulate(): number {
    const drift = (Math.random() - 0.48) * (this.sensor.threshold * 0.06);
    const next = this.current() + drift;
    const floor = this.sensor.type === 'power' ? 0 : this.sensor.threshold * 0.4;
    const ceil = this.sensor.threshold * 1.25;
    return Math.max(floor, Math.min(ceil, next));
  }

  private hexA(hex: string, a: number): string {
    const n = parseInt(hex.slice(1), 16);
    return `rgba(${n >> 16 & 255},${n >> 8 & 255},${n & 255},${a})`;
  }

  label(): string {
    return { temp: 'Température', humidity: 'Humidité', power: 'Puissance', pressure: 'Pression' }[this.sensor.type];
  }
}
