import { HttpClient, HttpHeaders } from '@angular/common/http';
import { inject, Injectable } from '@angular/core';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root',
})
export class SupplierService {
  private http = inject(HttpClient);
  private apiUrl = 'http://localhost:8083/api/v1/supplier';

  private getAuthHeaders(): HttpHeaders {
    const token = localStorage.getItem('token') || '';
    
    return new HttpHeaders({
      'Authorization': `Bearer ${token}`
    });
  }

  getDashboardStats(): Observable<any> {
    return this.http.get(`${this.apiUrl}/dashboard-stats`, { headers: this.getAuthHeaders() });
  }

  getAllSuppliers(): Observable<any> {
    return this.http.get(`${this.apiUrl}/getallsuppliers`, { headers: this.getAuthHeaders() });
  }

  updateSupplier(id: number, supplierData: any): Observable<any> {
    return this.http.put(`${this.apiUrl}/updatesupplier/${id}`, supplierData, { headers: this.getAuthHeaders() });
  }

  deleteSupplier(id: number): Observable<any> {
    return this.http.delete(`${this.apiUrl}/deletesupplier/${id}`, { headers: this.getAuthHeaders() });
  }
}
