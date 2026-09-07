import { HttpClient, HttpParams } from '@angular/common/http';
import { inject, Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { API_BASE_URL } from '../../../core/config/api.config';
import { Product, ProductCategory } from '../models/product.model';

@Injectable({ providedIn: 'root' })
export class CatalogService {
  private readonly http = inject(HttpClient);
  private readonly apiUrl = `${API_BASE_URL}/products`;

  list(): Observable<Product[]> {
    return this.http.get<Product[]>(this.apiUrl);
  }

  byCategory(category: ProductCategory): Observable<Product[]> {
    return this.http.get<Product[]>(`${this.apiUrl}/category/${category}`);
  }

  search(text: string): Observable<Product[]> {
    const params = new HttpParams().set('text', text);
    return this.http.get<Product[]>(`${this.apiUrl}/search`, { params });
  }

  byId(id: string): Observable<Product> {
    return this.http.get<Product>(`${this.apiUrl}/${id}`);
  }
}
