import { Routes } from '@angular/router';
import { CatalogComponent } from './catalog.component';
import { HomeComponent } from './home.component';
import { ProductDetailComponent } from './product-detail.component';

export const routes: Routes = [
  { path: '', pathMatch: 'full', component: HomeComponent },
  { path: 'catalogo', component: CatalogComponent },
  { path: 'producto/:id', component: ProductDetailComponent },
  { path: '**', redirectTo: '' }
];
