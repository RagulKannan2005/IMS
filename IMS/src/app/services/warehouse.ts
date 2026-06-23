import { HttpClient, HttpHeaders } from '@angular/common/http';
import { inject, Injectable } from '@angular/core';
import { Observable, forkJoin } from 'rxjs';
import { map } from 'rxjs/operators';

@Injectable({
  providedIn: 'root',
})
export class WarehouseService {
  private http = inject(HttpClient);
  private apiurl = 'http://localhost:8083/api/v1/warehouses';

  private getAuthHeaders(): HttpHeaders {
    const token = localStorage.getItem('token') || '';
    return new HttpHeaders({
      Authorization: `Bearer ${token}`,
      'Content-Type': 'application/json',
    });
  }

  // Combine active and inactive warehouses to get all
  getAllWarehouses(): Observable<any[]> {
    const active$ = this.http.get<any[]>(`${this.apiurl}/isactive/active`, { headers: this.getAuthHeaders() });
    const inactive$ = this.http.get<any[]>(`${this.apiurl}/isactive/inactive`, { headers: this.getAuthHeaders() });
    
    return forkJoin([active$, inactive$]).pipe(
      map(([active, inactive]) => {
        // Return combined array. If any is null/undefined, handle it.
        return [...(active || []), ...(inactive || [])];
      })
    );
  }

  addWarehouse(warehouse: any): Observable<any> {
    return this.http.post(`${this.apiurl}/addwarehouse`, warehouse, { headers: this.getAuthHeaders() });
  }

  updateWarehouse(id: number, warehouse: any): Observable<any> {
    return this.http.put(`${this.apiurl}/updatewarehouse/${id}`, warehouse, { headers: this.getAuthHeaders() });
  }

  deleteWarehouse(id: number): Observable<any> {
    return this.http.delete(`${this.apiurl}/deletewarehouse/${id}`, { headers: this.getAuthHeaders() });
  }
}

