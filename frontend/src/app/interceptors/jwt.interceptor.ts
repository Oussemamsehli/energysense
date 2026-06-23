// src/app/interceptors/jwt.interceptor.ts
import { HttpInterceptorFn, HttpRequest, HttpHandlerFn } from '@angular/common/http';

const API_BASE = 'http://192.168.49.2:30080';

// Clés localStorage à essayer dans l'ordre
const TOKEN_KEYS = ['token', 'jwt', 'access_token', 'authToken'];

function getToken(): string | null {
  for (const key of TOKEN_KEYS) {
    const t = localStorage.getItem(key);
    if (t) return t;
  }
  return null;
}

/**
 * Intercepteur fonctionnel Angular 18 (standalone).
 *
 * Comment ça fonctionne (pour l'entretien) :
 * - Intercepte TOUTES les requêtes HTTP sortantes
 * - Si la requête cible notre API backend → ajoute "Authorization: Bearer <token>"
 * - Sinon (CDN, assets, etc.) → laisse passer sans modification
 * - Résultat : aucun composant n'a besoin de gérer le token manuellement
 */
export const jwtInterceptor: HttpInterceptorFn = (
  req: HttpRequest<unknown>,
  next: HttpHandlerFn
) => {
  // N'intercepte que les appels vers notre backend
  if (!req.url.startsWith(API_BASE)) {
    return next(req);
  }

  const token = getToken();

  // Pas de token → laisse passer (le backend répondra 401/403)
  if (!token) {
    return next(req);
  }

  // Clone la requête avec le header Authorization ajouté
  // Important : on clone car les HttpRequest sont immuables
  const authReq = req.clone({
    setHeaders: {
      Authorization: `Bearer ${token}`,
    },
  });

  return next(authReq);
};