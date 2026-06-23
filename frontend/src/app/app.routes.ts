import { Routes } from '@angular/router';
import { DashboardComponent } from './dashboard/dashboard';
import { SitesComponent } from './sites/sites';
import { AlertsComponent } from './alerts/alerts';
import { LoginComponent } from './login/login';
import { authGuard } from './guards/auth.guard';

export const routes: Routes = [
  { path: '', redirectTo: 'dashboard', pathMatch: 'full' },
  { path: 'login', component: LoginComponent },
  {
    path: 'dashboard',
    component: DashboardComponent,
    canActivate: [authGuard]
  },
  {
    path: 'sites',
    component: SitesComponent,
    canActivate: [authGuard]
  },
  {
    path: 'alerts',
    component: AlertsComponent,
    canActivate: [authGuard]
  },
  { path: '**', redirectTo: 'dashboard' }
];
