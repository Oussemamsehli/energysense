// src/app/sensors/sensors.component.ts
import { Component, OnInit, computed, inject, signal } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { SensorCardComponent, Sensor, SensorType } from './sensor-card.component';

/* PAGE CAPTEURS — orchestre la grille de cartes. */
@Component({
  selector: 'app-sensors',
  standalone: true,
  imports: [SensorCardComponent],
  templateUrl: './sensors.component.html',
  styleUrls: ['./sensors.component.css'],
})
export class SensorsComponent implements OnInit {
  private route = inject(ActivatedRoute);

  sensors = signal<Sensor[]>([]);
  type = signal<'all' | SensorType>('all');
  spinning = signal(false);
  private siteFilter = signal<number | null>(null);

  filtered = computed(() => {
    const t = this.type();
    const site = this.siteFilter();
    return this.sensors().filter(s =>
      (t === 'all' || s.type === t) && (site === null || s.siteId === site)
    );
  });

  ngOnInit(): void {
    // Si on arrive depuis la page Sites (?site=3), on filtre sur ce site
    const site = this.route.snapshot.queryParamMap.get('site');
    this.siteFilter.set(site ? +site : null);
    this.sensors.set(this.demo());
  }

  // Le refresh force la régénération des cartes (track par id → recréation propre des graphes)
  refresh(): void {
    this.spinning.set(true);
    const snapshot = this.sensors();
    this.sensors.set([]);
    setTimeout(() => { this.sensors.set(snapshot); this.spinning.set(false); }, 350);
  }

  private demo(): Sensor[] {
    const seed = (base: number, spread: number, n = 24) =>
      Array.from({ length: n }, (_, i) => +(base + Math.sin(i / 3) * spread + (Math.random() - 0.5) * spread).toFixed(1));
    return [
      { id: 1, siteId: 1, name: 'TEMP-02', type: 'temp',     unit: '°C', value: 22.4, threshold: 26, history: seed(22, 2) },
      { id: 2, siteId: 1, name: 'HUM-04',  type: 'humidity', unit: '%',  value: 54,   threshold: 65, history: seed(52, 5) },
      { id: 3, siteId: 1, name: 'PWR-01',  type: 'power',    unit: 'kW', value: 184,  threshold: 300, history: seed(180, 30) },
      { id: 4, siteId: 2, name: 'TEMP-09', type: 'temp',     unit: '°C', value: 18.1, threshold: 24, history: seed(18, 1.5) },
      { id: 5, siteId: 3, name: 'TEMP-07', type: 'temp',     unit: '°C', value: 27.8, threshold: 26, history: seed(26, 2.5) },
      { id: 6, siteId: 3, name: 'HUM-03',  type: 'humidity', unit: '%',  value: 61,   threshold: 65, history: seed(58, 6) },
    ];
  }
}
