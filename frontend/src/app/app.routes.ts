import { Routes } from '@angular/router';
import { CatalogComponent } from './catalog.component';
import { HomeComponent } from './home.component';
import { ProductDetailComponent } from './product-detail.component';
import { AdminProductsComponent } from './admin-products.component';
import { CreateProductComponent } from './create-product.component';

export const routes: Routes = [
  { path: '', pathMatch: 'full', component: HomeComponent },
  { path: 'catalogo', component: CatalogComponent },
  { path: 'producto/:id', component: ProductDetailComponent },
  { path: 'admin/productos', component: AdminProductsComponent },
  { path: 'admin/productos/nuevo', component: CreateProductComponent },
  { path: 'admin/productos/:id/editar', component: CreateProductComponent },
  { path: '**', redirectTo: '' }
];
