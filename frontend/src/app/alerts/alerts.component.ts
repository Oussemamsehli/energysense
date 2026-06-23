// src/app/alerts/alerts.component.ts
import { Component, OnInit, computed, inject, signal } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { catchError, of } from 'rxjs';
import { AuthService } from '../services/auth.service';

type Severity = 'critical' | 'warning' | 'info';

interface Alert {
  id: number;
  severity: Severity;
  title: string;
  message: string;
  site: string;
  sensor: string;
  timestamp: number;     // epoch ms
  acknowledged: boolean;
}

const API = 'http://192.168.49.2:30080';

@Component({
  selector: 'app-alerts',
  standalone: true,
  templateUrl: './alerts.component.html',
  styleUrls: ['./alerts.component.css']
})
export class AlertsComponent implements OnInit {
  private http = inject(HttpClient);
  private auth = inject(AuthService);

  alerts = signal<Alert[]>([]);
  filter = signal<'all' | Severity>('all');

  counts = computed(() => {
    const a = this.alerts();
    return {
      critical: a.filter(x => x.severity === 'critical').length,
      warning: a.filter(x => x.severity === 'warning').length,
      info: a.filter(x => x.severity === 'info').length,
    };
  });

  active = computed(() => this.alerts().filter(a => !a.acknowledged));

  filtered = computed(() => {
    const f = this.filter();
    const list = f === 'all' ? this.alerts() : this.alerts().filter(a => a.severity === f);
    // Plus récentes en haut
    return [...list].sort((x, y) => y.timestamp - x.timestamp);
  });

  ngOnInit(): void {
    const token = this.auth.getToken();
    // Backend only exposes /api/sensors/{id}/alerts — use demo data until a global endpoint exists.
    this.http
      .get<Alert[]>(`${API}/api/alerts`, { headers: token ? { Authorization: `Bearer ${token}` } : {} })
      .pipe(catchError(() => of(null)))
      .subscribe(data => this.alerts.set(data && data.length ? data : this.demo()));
  }

  acknowledge(a: Alert): void {
    // Mise à jour optimiste de l'UI (signal immuable)
    this.alerts.update(list => list.map(x => x.id === a.id ? { ...x, acknowledged: true } : x));
    // 🔌 Persiste côté backend si tu as l'endpoint :
    // this.http.post(`${API}/api/alerts/${a.id}/ack`, {}).subscribe();
  }

  // Temps relatif "il y a X"
  ago(ts: number): string {
    const s = Math.floor((Date.now() - ts) / 1000);
    if (s < 60) return "à l'instant";
    const m = Math.floor(s / 60);
    if (m < 60) return `il y a ${m} min`;
    const h = Math.floor(m / 60);
    if (h < 24) return `il y a ${h} h`;
    const d = Math.floor(h / 24);
    return `il y a ${d} j`;
  }

  private demo(): Alert[] {
    const now = Date.now();
    return [
      { id: 1, severity: 'critical', title: 'Température critique', message: 'Le capteur dépasse le seuil haut depuis 8 minutes (29.4°C / seuil 26°C).', site: 'Entrepôt Sud', sensor: 'TEMP-07', timestamp: now - 4 * 60_000, acknowledged: false },
      { id: 2, severity: 'critical', title: 'Capteur hors ligne', message: 'Aucune donnée reçue du capteur depuis 12 minutes. Vérifier la connectivité MQTT.', site: 'Entrepôt Sud', sensor: 'HUM-03', timestamp: now - 12 * 60_000, acknowledged: false },
      { id: 3, severity: 'warning', title: 'Charge élevée', message: 'La charge du site dépasse 85% — risque de surconsommation aux heures de pointe.', site: 'Entrepôt Sud', sensor: 'PWR-01', timestamp: now - 34 * 60_000, acknowledged: false },
      { id: 4, severity: 'warning', title: 'Humidité en hausse', message: 'Humidité relative à 68%, tendance haussière sur la dernière heure.', site: 'Site Monastir', sensor: 'HUM-11', timestamp: now - 92 * 60_000, acknowledged: false },
      { id: 5, severity: 'info', title: 'Maintenance planifiée', message: 'Redémarrage programmé de la passerelle IoT à 02:00.', site: 'Site Tunis', sensor: 'GW-MAIN', timestamp: now - 3 * 3_600_000, acknowledged: true },
      { id: 6, severity: 'info', title: 'Seuil mis à jour', message: 'Le seuil de température a été abaissé à 24°C par l’administrateur.', site: 'Site Tunis', sensor: 'TEMP-02', timestamp: now - 6 * 3_600_000, acknowledged: true },
    ];
  }
}
