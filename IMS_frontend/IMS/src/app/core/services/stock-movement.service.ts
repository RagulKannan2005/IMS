import { HttpClient, HttpParams } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { StockMovementRequestDto, StockMovementResponseDto } from '../../shared/models/interfaces';

@Injectable({
  providedIn: 'root'
})
export class StockMovementService {
  private readonly apiUrl = 'http://localhost:8083/api/v1/stock-movements';

  constructor(private http: HttpClient) {}

  createMovement(dto: StockMovementRequestDto): Observable<StockMovementResponseDto> {
    return this.http.post<StockMovementResponseDto>(this.apiUrl, dto);
  }

  getMovementById(id: number): Observable<StockMovementResponseDto> {
    return this.http.get<StockMovementResponseDto>(`${this.apiUrl}/${id}`);
  }

  getAllMovements(): Observable<StockMovementResponseDto[]> {
    return this.http.get<StockMovementResponseDto[]>(this.apiUrl);
  }

  getMovementsByProduct(productId: number): Observable<StockMovementResponseDto[]> {
    return this.http.get<StockMovementResponseDto[]>(`${this.apiUrl}/product/${productId}`);
  }

  getMovementsByWarehouse(warehouseId: number): Observable<StockMovementResponseDto[]> {
    return this.http.get<StockMovementResponseDto[]>(`${this.apiUrl}/warehouse/${warehouseId}`);
  }

  getMovementsByType(type: string): Observable<StockMovementResponseDto[]> {
    return this.http.get<StockMovementResponseDto[]>(`${this.apiUrl}/type/${type}`);
  }

  getMovementsByDateRange(start: string, end: string): Observable<StockMovementResponseDto[]> {
    const params = new HttpParams()
      .set('start', start)
      .set('end', end);
    return this.http.get<StockMovementResponseDto[]>(`${this.apiUrl}/search-date`, { params });
  }

  updateMovement(id: number, dto: StockMovementRequestDto): Observable<StockMovementResponseDto> {
    return this.http.put<StockMovementResponseDto>(`${this.apiUrl}/${id}`, dto);
  }

  deleteMovement(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${id}`);
  }
}
