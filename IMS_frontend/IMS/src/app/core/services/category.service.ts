import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { CategoryRequestDto, CategoryResponseDto } from '../../shared/models/interfaces';

@Injectable({
  providedIn: 'root'
})
export class CategoryService {
  private readonly apiUrl = 'http://localhost:8082/api/v1/categories';

  constructor(private http: HttpClient) {}

  createCategory(dto: CategoryRequestDto): Observable<CategoryResponseDto> {
    return this.http.post<CategoryResponseDto>(`${this.apiUrl}/newcategory`, dto);
  }

  getAllCategories(): Observable<CategoryResponseDto[]> {
    return this.http.get<CategoryResponseDto[]>(`${this.apiUrl}/allcategories`);
  }

  getActiveCategories(): Observable<CategoryResponseDto[]> {
    return this.http.get<CategoryResponseDto[]>(`${this.apiUrl}/activecategories`);
  }

  getCategoryByDescription(description: string): Observable<CategoryResponseDto> {
    return this.http.get<CategoryResponseDto>(`${this.apiUrl}/categorydescription/${description}`);
  }

  updateCategory(id: number, dto: CategoryRequestDto): Observable<CategoryResponseDto> {
    return this.http.put<CategoryResponseDto>(`${this.apiUrl}/updatecategory/${id}`, dto);
  }

  deleteCategory(id: number): Observable<CategoryResponseDto> {
    return this.http.delete<CategoryResponseDto>(`${this.apiUrl}/deletecategory/${id}`);
  }
}
