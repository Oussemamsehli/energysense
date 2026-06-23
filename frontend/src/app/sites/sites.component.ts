// src/app/sites/sites.component.ts
import { Component, OnInit, computed, inject, signal } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Router } from '@angular/router';
import { catchError, of } from 'rxjs';

type SiteStatus = 'online' | 'warning' | 'offline';

interface Site {
  id: number;
  name: string;
  location: string;
  status: SiteStatus;
  sensors: number;
  alerts: number;
  temp: number;
  power: number;
  load: number;
  tags?: string[];
  sparkline?: string;
}

// 🔌 Clé localStorage — doit correspondre exactement à ce que ton AuthService utilise
// Vérifie avec : Object.keys(localStorage) dans la console navigateur
const TOKEN_KEY = 'token';
const API = 'http://192.168.49.2:30080';

@Component({
  selector: 'app-sites',
  standalone: true,
  templateUrl: './sites.component.html',
  styleUrls: ['./sites.component.css']
})
export class SitesComponent implements OnInit {
  private http = inject(HttpClient);
  private router = inject(Router);

  sites    = signal<Site[]>([]);
  loading  = signal(true);
  search   = signal('');
  filter   = signal<'all' | SiteStatus>('all');

  // ── Compteurs dérivés ──────────────────────────────────────────
  counts = computed(() => {
    const s = this.sites();
    return {
      all:            s.length,
      online:         s.filter(x => x.status === 'online').length,
      warning:        s.filter(x => x.status === 'warning').length,
      offline:        s.filter(x => x.status === 'offline').length,
      totalAlerts:    s.reduce((sum, x) => sum + x.alerts, 0),
      criticalAlerts: s.filter(x => x.alerts >= 3).length,
    };
  });

  // ── Stats globales pour la stats-row ──────────────────────────
  onlinePercent = computed(() =>
    Math.round((this.counts().online / (this.counts().all || 1)) * 100)
  );

  totalPower = computed(() =>
    this.sites().reduce((sum, s) => sum + s.power, 0)
  );

  // ── Filtrage live ─────────────────────────────────────────────
  filtered = computed(() => {
    const q = this.search().toLowerCase().trim();
    const f = this.filter();
    return this.sites().filter(s => {
      const okFilter = f === 'all' || s.status === f;
      const okSearch = !q
        || s.name.toLowerCase().includes(q)
        || s.location.toLowerCase().includes(q);
      return okFilter && okSearch;
    });
  });

  // ── Sparkline par défaut selon statut ────────────────────────
  defaultSparkline(status: SiteStatus): string {
    const sparklines: Record<SiteStatus, string> = {
      online:  '0,22 30,18 60,20 90,15 120,17 150,12 180,14 200,10',
      warning: '0,20 30,18 60,15 90,12 120,8 150,5 180,3 200,2',
      offline: '0,14 40,14 80,14 120,14 160,14 200,28',
    };
    return sparklines[status];
  }

  // ── Couleur de la sparkline ──────────────────────────────────
  sparklineColor(status: SiteStatus): string {
    return status === 'online' ? '#1D9E75'
         : status === 'warning' ? '#e0a64a'
         : '#6e7681';
  }

  sparklineAreaColor(status: SiteStatus): string {
    return status === 'online'  ? 'rgba(29,158,117,0.07)'
         : status === 'warning' ? 'rgba(224,166,74,0.07)'
         : 'rgba(110,118,129,0.04)';
  }

  // ── Libellé statut ───────────────────────────────────────────
  statusLabel(status: SiteStatus): string {
    return status === 'online' ? 'En ligne'
         : status === 'warning' ? 'Alerte'
         : 'Hors ligne';
  }

  // ── Chargement ───────────────────────────────────────────────
  ngOnInit(): void {
    // Essaie plusieurs clés courantes si la principale est vide
    const token = localStorage.getItem(TOKEN_KEY)
               || localStorage.getItem('jwt')
               || localStorage.getItem('access_token')
               || localStorage.getItem('authToken');

    if (!token) {
      console.warn('[Sites] Aucun token JWT trouvé — affichage démo');
      this.sites.set(this.demo());
      this.loading.set(false);
      return;
    }

    this.http
      .get<Site[]>(`${API}/api/sites`, {
        headers: { Authorization: `Bearer ${token}` },
      })
      .pipe(
        catchError(err => {
          console.error('[Sites] Erreur API :', err.status, err.message);
          return of(null);
        })
      )
      .subscribe(data => {
        this.sites.set(data && data.length ? data : this.demo());
        this.loading.set(false);
      });
  }

  open(site: Site): void {
    this.router.navigate(['/sensors'], { queryParams: { site: site.id } });
  }

  // ── Données démo ─────────────────────────────────────────────
  private demo(): Site[] {
    return [
      {
        id: 1, name: 'Site Tunis', location: 'Tunis, Zone industrielle',
        status: 'online', sensors: 3, alerts: 0, temp: 22.4, power: 184, load: 45,
        tags: ['HVAC', 'Industriel'],
        sparkline: '0,22 30,18 60,20 90,15 120,17 150,12 180,14 200,10',
      },
      {
        id: 2, name: 'Site Monastir', location: 'Monastir, Salle serveurs',
        status: 'online', sensors: 6, alerts: 0, temp: 18.1, power: 256, load: 62,
        tags: ['Datacenter', 'Refroidissement'],
        sparkline: '0,16 30,14 60,18 90,12 120,10 150,13 180,9 200,11',
      },
      {
        id: 3, name: 'Entrepôt Sud', location: 'Sfax, Stockage froid',
        status: 'warning', sensors: 15, alerts: 5, temp: 27.8, power: 412, load: 88,
        tags: ['Stockage froid', 'Temp. élevée'],
        sparkline: '0,20 30,18 60,15 90,12 120,8 150,5 180,3 200,2',
      },
      {
        id: 4, name: 'Bureau Centre', location: 'Tunis, Climatisation',
        status: 'offline', sensors: 6, alerts: 0, temp: 21.3, power: 0, load: 0,
        tags: ['Déconnecté'],
        sparkline: '0,14 40,14 80,14 120,14 160,14 200,28',
      },
    ];
  }
}