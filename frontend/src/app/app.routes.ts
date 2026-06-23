// src/app/app.routes.ts
import { Routes } from '@angular/router';
import { LayoutComponent }    from './layout/layout.component';
import { DashboardComponent } from './dashboard/dashboard';
import { SitesComponent }     from './sites/sites.component';
import { SensorsComponent }   from './sensors/sensors.component';
import { AlertsComponent }    from './alerts/alerts.component';
import { LoginComponent }     from './login/login.component';
import { authGuard }          from './guards/auth.guard';

export const routes: Routes = [
  // Page login — sans layout (pas de sidebar)
  { path: 'login', component: LoginComponent },

  // Toutes les pages protégées partagent le LayoutComponent (sidebar + router-outlet)
  {
    path: '',
    component: LayoutComponent,
    canActivate: [authGuard],
    children: [
      { path: '',          redirectTo: 'dashboard', pathMatch: 'full' },
      { path: 'dashboard', component: DashboardComponent },
      { path: 'sites',     component: SitesComponent },
      { path: 'sensors',   component: SensorsComponent },   // ← manquait !
      { path: 'alerts',    component: AlertsComponent },
    ]
  },

  // Fallback
  { path: '**', redirectTo: 'dashboard' }
];