import { HttpClient, HttpHeaders } from '@angular/common/http';
import { inject, Injectable } from '@angular/core';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root',
})
export class StockService {
  private http = inject(HttpClient);
  private apiurl = 'http://localhost:8083/api/v1/stock';

  private getAuthHeaders(): HttpHeaders {
    const token = localStorage.getItem('token') || '';
    return new HttpHeaders({
      Authorization: `Bearer ${token}`,
      'Content-Type': 'application/json',
    });
  }

  getAllStocks(): Observable<any> {
    return this.http.get(`${this.apiurl}/getallstock`, {
      headers: this.getAuthHeaders(),
    });
  }

  addStock(stock: any): Observable<any> {
    return this.http.post(`${this.apiurl}/addstock`, stock, {
      headers: this.getAuthHeaders(),
    });
  }

  updateStock(id: number, stock: any): Observable<any> {
    return this.http.put(`${this.apiurl}/updatestock/${id}`, stock, {
      headers: this.getAuthHeaders(),
    });
  }

  deleteStock(id: number): Observable<any> {
    return this.http.delete(`${this.apiurl}/deletestock/${id}`, {
      headers: this.getAuthHeaders(),
    });
  }

  transferStock(transferDto: any): Observable<any> {
    return this.http.post(`${this.apiurl}/transferstock`, transferDto, {
      headers: this.getAuthHeaders(),
    });
  }
}
