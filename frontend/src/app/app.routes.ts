import { Routes } from '@angular/router';
import { CatalogComponent } from './features/catalog/pages/catalog/catalog.component';
import { ProductDetailComponent } from './features/catalog/pages/product-detail/product-detail.component';
import { AdminProductsComponent } from './features/admin/products/pages/product-list/admin-products.component';
import { CreateProductComponent } from './features/admin/products/pages/product-form/create-product.component';
import { HomeComponent } from './features/home/pages/home/home.component';

export const routes: Routes = [
  { path: '', pathMatch: 'full', component: HomeComponent },
  { path: 'catalogo', component: CatalogComponent },
  { path: 'producto/:id', component: ProductDetailComponent },
  { path: 'admin/productos', component: AdminProductsComponent },
  { path: 'admin/productos/nuevo', component: CreateProductComponent },
  { path: 'admin/productos/:id/editar', component: CreateProductComponent },
  { path: '**', redirectTo: '' }
];
