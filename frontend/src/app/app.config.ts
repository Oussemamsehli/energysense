// src/app/app.config.ts
import { ApplicationConfig } from '@angular/core';
import { provideRouter }     from '@angular/router';
import { provideHttpClient, withInterceptors } from '@angular/common/http';

import { routes }         from './app.routes';
import { jwtInterceptor } from './interceptors/jwt.interceptor';

export const appConfig: ApplicationConfig = {
  providers: [
    // ← provideZoneChangeDetection() supprimé — causait NG0908 (Zone.js manquant)
    provideRouter(routes),
    provideHttpClient(
      withInterceptors([jwtInterceptor])
    ),
  ],
};