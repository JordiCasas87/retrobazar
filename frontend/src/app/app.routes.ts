import { Routes } from '@angular/router';
import { HomeComponent } from './home.component';
import { ProductDetailComponent } from './product-detail.component';

export const routes: Routes = [
  { path: '', pathMatch: 'full', component: HomeComponent },
  { path: 'producto/:id', component: ProductDetailComponent },
  { path: '**', redirectTo: '' }
];
