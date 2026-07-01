import { HttpClient, HttpHandler, HttpHeaders } from '@angular/common/http';
import { inject, Inject, Injectable } from '@angular/core';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root',
})
export class categoryservice {
  private http = inject(HttpClient);
  private apiurl = 'http://localhost:8083/api/v1/category';

  private getAuthHeaders(): HttpHeaders {
    const token = localStorage.getItem('token') || '';

    return new HttpHeaders({
      Authorization: `Bearer ${token}`,
      'Content-Type': 'application/json',
    });
  }

  getAllCategories(): Observable<any> {
    return this.http.get(`${this.apiurl}/allcategories`, {
      headers: this.getAuthHeaders(),
    });
  }

  getDefaultCategories(): Observable<any> {
    return this.http.get(`${this.apiurl}/defaultcategories`, {
      headers: this.getAuthHeaders(),
    });
  }

  addcategory(category: any): Observable<any> {
    return this.http.post(`${this.apiurl}/newcategory`, category, { headers: this.getAuthHeaders() });
  }
  getCategoryByName(name: string): Observable<any> {
    return this.http.get(`${this.apiurl}/categoryname/${name}`, { headers: this.getAuthHeaders() });
  }
}
