import { Routes } from '@angular/router';

export const routes: Routes = [
  { path: '', redirectTo: '/overview', pathMatch: 'full' },
  {
    path: 'overview',
    loadComponent: () => import('./components/product-overview/product-overview.component').then(m => m.ProductOverviewComponent)
  }
];