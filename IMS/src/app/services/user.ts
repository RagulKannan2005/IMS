import { HttpClient, HttpHeaders } from '@angular/common/http';
import { inject, Injectable } from '@angular/core';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root',
})
export class UserService {
  private http = inject(HttpClient);
  private authApiUrl = 'http://localhost:8083/api/v1/auth';
  private usersApiUrl = 'http://localhost:8083/api/v1/users';

  private getAuthHeaders(): HttpHeaders {
    const token = localStorage.getItem('token') || '';
    return new HttpHeaders({
      Authorization: `Bearer ${token}`,
      'Content-Type': 'application/json',
    });
  }

  register(user: any): Observable<any> {
    return this.http.post(`${this.authApiUrl}/register`, user);
  }

  // Used by Admin to create a manager or staff member
  adminCreateUser(user: any): Observable<any> {
    return this.http.post(`${this.usersApiUrl}/newuser`, user, {
      headers: this.getAuthHeaders(),
    });
  }

  login(user: any): Observable<any> {
    return this.http.post(`${this.authApiUrl}/login`, user);
  }

  getAllUsers(): Observable<any[]> {
    return this.http.get<any[]>(`${this.usersApiUrl}/allusers`, {
      headers: this.getAuthHeaders(),
    });
  }

  deleteUser(id: number): Observable<any> {
    return this.http.delete(`${this.usersApiUrl}/deleteuser/${id}`, {
      headers: this.getAuthHeaders(),
    });
  }

  getManagersByAdminId(adminId: number): Observable<any[]> {
    return this.http.get<any[]>(`${this.usersApiUrl}/admin/${adminId}/managers`, {
      headers: this.getAuthHeaders(),
    });
  }

  updateUser(id: number, userData: any): Observable<any> {
    return this.http.put(`${this.usersApiUrl}/updateuser/${id}`, userData, {
      headers: this.getAuthHeaders(),
    });
  }
}