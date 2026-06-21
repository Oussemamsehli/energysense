import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class ApiService {
  private baseUrl = 'http://192.168.49.2:30080';
  private token = '';

  constructor(private http: HttpClient) {}

  setToken(token: string) {
    this.token = token;
  }

  private headers(): HttpHeaders {
    return new HttpHeaders({
      'Authorization': `Bearer ${this.token}`,
      'Content-Type': 'application/json'
    });
  }

  getSites(): Observable<any[]> {
    return this.http.get<any[]>(`${this.baseUrl}/api/sites`, { headers: this.headers() });
  }

  getAlerts(): Observable<any[]> {
    return this.http.get<any[]>(`${this.baseUrl}/api/alerts`, { headers: this.headers() });
  }

  getSensors(siteId: string): Observable<any[]> {
    return this.http.get<any[]>(`${this.baseUrl}/api/sites/${siteId}/sensors`, { headers: this.headers() });
  }

  login(username: string, password: string): Observable<any> {
    return this.http.post<any>(`${this.baseUrl}/api/auth/login`, { username, password });
  }
}
