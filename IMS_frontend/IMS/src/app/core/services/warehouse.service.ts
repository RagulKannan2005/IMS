import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { WarehouseRequestDto, WarehouseResponseDto } from '../../shared/models/interfaces';

@Injectable({
  providedIn: 'root'
})
export class WarehouseService {
  private readonly apiUrl = 'http://localhost:8083/api/v1/warehouses';

  constructor(private http: HttpClient) {}

  addWarehouse(dto: WarehouseRequestDto): Observable<WarehouseResponseDto> {
    return this.http.post<WarehouseResponseDto>(`${this.apiUrl}/addwarehouse`, dto);
  }

  getWarehouseByCode(code: string): Observable<WarehouseResponseDto> {
    return this.http.get<WarehouseResponseDto>(`${this.apiUrl}/warehouse_code/${code}`);
  }

  getWarehousesByManager(name: string): Observable<WarehouseResponseDto[]> {
    return this.http.get<WarehouseResponseDto[]>(`${this.apiUrl}/managername/${name}`);
  }

  getWarehousesByActiveStatus(status: string): Observable<WarehouseResponseDto[]> {
    return this.http.get<WarehouseResponseDto[]>(`${this.apiUrl}/isactive/${status}`);
  }

  getWarehousesByName(name: string): Observable<WarehouseResponseDto[]> {
    return this.http.get<WarehouseResponseDto[]>(`${this.apiUrl}/name/${name}`);
  }

  updateWarehouse(id: number, dto: WarehouseRequestDto): Observable<WarehouseResponseDto> {
    return this.http.put<WarehouseResponseDto>(`${this.apiUrl}/updatewarehouse/${id}`, dto);
  }

  deleteWarehouse(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/deletewarehouse/${id}`);
  }
}
