import { Component } from '@angular/core';
import { Routes } from '@angular/router';
import { ProductDetailComponent } from './product-detail.component';

@Component({
  standalone: true,
  template: ''
})
class HomeRouteComponent {}

export const routes: Routes = [
  { path: '', pathMatch: 'full', component: HomeRouteComponent },
  { path: 'producto/:id', component: ProductDetailComponent },
  { path: '**', redirectTo: '' }
];
