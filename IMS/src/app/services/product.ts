import { HttpClient, HttpHeaders } from '@angular/common/http';
import { inject, Injectable } from '@angular/core';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root',
})
export class ProductService {
  private http = inject(HttpClient);
  private apiUrl = 'http://localhost:8083/api/v1/products';

  
  private getAuthHeaders(): HttpHeaders {
    const token = localStorage.getItem('token') || '';
    
    return new HttpHeaders({
      'Authorization': `Bearer ${token}`
    });
  }

  getAllProducts(): Observable<any> {
    return this.http.get(`${this.apiUrl}/allproducts`, { headers: this.getAuthHeaders() });
  }

  getActiveProducts(): Observable<any> {
    return this.http.get(`${this.apiUrl}/activeproducts`, { headers: this.getAuthHeaders() });
  }

  getProductById(id: number): Observable<any> {
    return this.http.get(`${this.apiUrl}/${id}`, { headers: this.getAuthHeaders() });
  }

  addProduct(productData: any): Observable<any> {
    return this.http.post(`${this.apiUrl}/addproduct`, productData, { headers: this.getAuthHeaders() });
  }

  updateProduct(id: number, productData: any): Observable<any> {
    return this.http.put(`${this.apiUrl}/updateproduct/${id}`, productData, { headers: this.getAuthHeaders() });
  }

  deleteProduct(id: number): Observable<any> {
    return this.http.delete(`${this.apiUrl}/deleteproduct/${id}`, { headers: this.getAuthHeaders() });
  }

  getProductCountByUser(userId: number): Observable<any> {
    return this.http.get(`${this.apiUrl}/user/${userId}/count`, { headers: this.getAuthHeaders() });
  }
}
