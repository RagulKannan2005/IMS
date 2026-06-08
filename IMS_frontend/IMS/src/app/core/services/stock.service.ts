import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { StockRequestDto, StockResponseDto, StockTransferRequestDto } from '../../shared/models/interfaces';

@Injectable({
  providedIn: 'root'
})
export class StockService {
  private readonly apiUrl = 'http://localhost:8082/api/v1/stock';

  constructor(private http: HttpClient) {}

  addStock(dto: StockRequestDto): Observable<StockResponseDto> {
    return this.http.post<StockResponseDto>(`${this.apiUrl}/addstock`, dto);
  }

  getAllStocks(): Observable<StockResponseDto[]> {
    return this.http.get<StockResponseDto[]>(`${this.apiUrl}/getallstock`);
  }

  updateStock(id: number, dto: StockRequestDto): Observable<StockResponseDto> {
    return this.http.put<StockResponseDto>(`${this.apiUrl}/updatestock/${id}`, dto);
  }

  deleteStock(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/deletestock/${id}`);
  }

  transferStock(dto: StockTransferRequestDto): Observable<StockResponseDto[]> {
    return this.http.post<StockResponseDto[]>(`${this.apiUrl}/transferstock`, dto);
  }

  getCurrentStock(productId: number, warehouseId: number): Observable<StockResponseDto> {
    return this.http.get<StockResponseDto>(`${this.apiUrl}/getcurrentstock/${productId}/${warehouseId}`);
  }
}
