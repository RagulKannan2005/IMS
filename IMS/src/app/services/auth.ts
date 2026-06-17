import { HttpClient } from '@angular/common/http';
import { inject, Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root',
})
export class Auth {
  private http = inject(HttpClient);

  private apiUrl = 'http://localhost:8083/api/v1/auth';

  register(user: any){
    return this.http.post(this.apiUrl + '/register',user);
  }
  login(user:any){
    return this.http.post(this.apiUrl + '/login',user);
  }
}
