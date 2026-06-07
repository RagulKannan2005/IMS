import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { UserRequestDto, UserResponseDto } from '../../shared/models/interfaces';

@Injectable({
  providedIn: 'root'
})
export class UserService {
  private readonly apiUrl = 'http://localhost:8083/api/v1/users';

  constructor(private http: HttpClient) {}

  getCurrentUser(): Observable<UserResponseDto> {
    return this.http.get<UserResponseDto>(`${this.apiUrl}/me`);
  }

  createUser(dto: UserRequestDto): Observable<UserResponseDto> {
    return this.http.post<UserResponseDto>(`${this.apiUrl}/newuser`, dto);
  }

  getAllUsers(): Observable<UserResponseDto[]> {
    return this.http.get<UserResponseDto[]>(`${this.apiUrl}/allusers`);
  }

  updateUser(id: number, dto: UserRequestDto): Observable<UserResponseDto> {
    return this.http.put<UserResponseDto>(`${this.apiUrl}/updateuser/${id}`, dto);
  }

  deleteUser(id: number): Observable<UserResponseDto> {
    return this.http.delete<UserResponseDto>(`${this.apiUrl}/deleteuser/${id}`);
  }
}
