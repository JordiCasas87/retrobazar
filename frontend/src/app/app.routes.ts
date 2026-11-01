import { Routes } from '@angular/router';
import { CatalogComponent } from './features/catalog/pages/catalog/catalog.component';
import { ProductDetailComponent } from './features/catalog/pages/product-detail/product-detail.component';
import { AdminProductsComponent } from './features/admin/products/pages/product-list/admin-products.component';
import { CreateProductComponent } from './features/admin/products/pages/product-form/create-product.component';
import { HomeComponent } from './features/home/pages/home/home.component';
import { AccountComponent } from './features/auth/pages/account/account.component';
import { adminGuard } from './core/auth/admin.guard';

export const routes: Routes = [
  { path: '', pathMatch: 'full', component: HomeComponent },
  { path: 'catalogo', component: CatalogComponent },
  { path: 'producto/:id', component: ProductDetailComponent },
  { path: 'cuenta', component: AccountComponent },
  { path: 'admin/productos', component: AdminProductsComponent, canActivate: [adminGuard] },
  { path: 'admin/productos/nuevo', component: CreateProductComponent, canActivate: [adminGuard] },
  { path: 'admin/productos/:id/editar', component: CreateProductComponent, canActivate: [adminGuard] },
  { path: '**', redirectTo: '' }
];
