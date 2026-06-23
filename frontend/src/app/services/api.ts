import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable, of } from 'rxjs';
import { AuthService } from './auth.service';

@Injectable({ providedIn: 'root' })
export class ApiService {
  private baseUrl = 'http://192.168.49.2:30080';

  constructor(private http: HttpClient, private auth: AuthService) {}

  private headers(): HttpHeaders {
    return new HttpHeaders({
      'Authorization': `Bearer ${this.auth.getToken()}`,
      'Content-Type': 'application/json'
    });
  }

  login(email: string, password: string): Observable<any> {
    return this.http.post<any>(`${this.baseUrl}/auth/login`, { email, password });
  }

  register(email: string, password: string, name: string): Observable<any> {
    return this.http.post<any>(`${this.baseUrl}/auth/register`, { email, password, name });
  }

  getSites(): Observable<any[]> {
    return this.http.get<any[]>(`${this.baseUrl}/api/sites`, { headers: this.headers() });
  }

  createSite(name: string, location: string): Observable<any> {
    return this.http.post<any>(`${this.baseUrl}/api/sites`, { name, location }, { headers: this.headers() });
  }

  getAlerts(): Observable<any[]> {
    return of([]);
  }

  getSensors(siteId: string): Observable<any[]> {
    return this.http.get<any[]>(`${this.baseUrl}/api/sites/${siteId}/sensors`, { headers: this.headers() });
  }
}
