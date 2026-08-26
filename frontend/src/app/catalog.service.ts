import { HttpClient, HttpParams } from '@angular/common/http';
import { inject, Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { Product, ProductCategory } from './product.model';

@Injectable({ providedIn: 'root' })
export class CatalogService {
  private readonly http = inject(HttpClient);
  private readonly apiUrl = 'http://localhost:8080/api/products';

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
