import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { RouterModule, Router } from '@angular/router';
import { ApiService } from '../services/api';
import { AuthService } from '../services/auth.service';

@Component({
  selector: 'app-sites',
  standalone: true,
  imports: [CommonModule, FormsModule, RouterModule],
  templateUrl: './sites.html',
  styleUrl: './sites.css'
})
export class SitesComponent implements OnInit {
  sites: any[] = [];
  loading = true;
  showForm = false;
  newName = '';
  newLocation = '';
  saving = false;

  constructor(private api: ApiService, private auth: AuthService, private router: Router) {}

  ngOnInit() { this.loadSites(); }

  loadSites() {
    this.loading = true;
    this.api.getSites().subscribe({
      next: (data) => { this.sites = data; this.loading = false; },
      error: () => { this.loading = false; }
    });
  }

  createSite() {
    if (!this.newName || !this.newLocation) return;
    this.saving = true;
    this.api.createSite(this.newName, this.newLocation).subscribe({
      next: () => {
        this.showForm = false;
        this.newName = '';
        this.newLocation = '';
        this.saving = false;
        this.loadSites();
      },
      error: () => { this.saving = false; }
    });
  }

  logout() { this.auth.logout(); this.router.navigate(['/login']); }
}
