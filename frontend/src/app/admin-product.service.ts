import { HttpClient } from '@angular/common/http';
import { inject, Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { CreateProductRequest, Product, UpdateProductRequest } from './product.model';

@Injectable({ providedIn: 'root' })
export class AdminProductService {
  private readonly http = inject(HttpClient);
  private readonly apiUrl = 'http://localhost:8080/api/admin/products';

  list(): Observable<Product[]> {
    return this.http.get<Product[]>(this.apiUrl);
  }

  create(request: CreateProductRequest): Observable<Product> {
    return this.http.post<Product>(this.apiUrl, request);
  }

  byId(id: string): Observable<Product> {
    return this.http.get<Product>(`${this.apiUrl}/${id}`);
  }

  update(id: string, request: UpdateProductRequest): Observable<Product> {
    return this.http.put<Product>(`${this.apiUrl}/${id}`, request);
  }

  activate(id: string): Observable<Product> {
    return this.http.patch<Product>(`${this.apiUrl}/${id}/activate`, {});
  }

  deactivate(id: string): Observable<Product> {
    return this.http.patch<Product>(`${this.apiUrl}/${id}/deactivate`, {});
  }

  delete(id: string): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${id}`);
  }
}
