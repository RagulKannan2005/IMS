import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { 
  SupplierRequestDto, 
  SupplierResponseDto, 
  ProductResponseDto, 
  PurchaseOrderResponseDto 
} from '../../shared/models/interfaces';

@Injectable({
  providedIn: 'root'
})
export class SupplierService {
  private readonly apiUrl = 'http://localhost:8083/api/v1/suppliers';

  constructor(private http: HttpClient) {}

  addSupplier(dto: SupplierRequestDto): Observable<SupplierResponseDto> {
    return this.http.post<SupplierResponseDto>(`${this.apiUrl}/addsupplier`, dto);
  }

  getAllSuppliers(): Observable<SupplierResponseDto[]> {
    return this.http.get<SupplierResponseDto[]>(`${this.apiUrl}/getallsuppliers`);
  }

  updateSupplier(id: number, dto: SupplierRequestDto): Observable<SupplierResponseDto> {
    return this.http.put<SupplierResponseDto>(`${this.apiUrl}/updatesupplier/${id}`, dto);
  }

  deleteSupplier(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/deletesupplier/${id}`);
  }

  getSupplierById(id: number): Observable<SupplierResponseDto> {
    return this.http.get<SupplierResponseDto>(`${this.apiUrl}/getsupplierbyid/${id}`);
  }

  getSuppliersByProduct(productName: string): Observable<SupplierResponseDto[]> {
    return this.http.get<SupplierResponseDto[]>(`${this.apiUrl}/getsuppliersbyproduct/${productName}`);
  }

  getSuppliersByStatus(status: boolean): Observable<SupplierResponseDto[]> {
    return this.http.get<SupplierResponseDto[]>(`${this.apiUrl}/getsuppliersbystatus/${status}`);
  }

  getSuppliersByName(name: string): Observable<SupplierResponseDto[]> {
    return this.http.get<SupplierResponseDto[]>(`${this.apiUrl}/getsuppliersbyname/${name}`);
  }

  getMyProducts(): Observable<ProductResponseDto[]> {
    return this.http.get<ProductResponseDto[]>(`${this.apiUrl}/my-products`);
  }

  getMyOrders(): Observable<PurchaseOrderResponseDto[]> {
    return this.http.get<PurchaseOrderResponseDto[]>(`${this.apiUrl}/my-orders`);
  }

  getSuppliersByCategory(categoryName: string): Observable<SupplierResponseDto[]> {
    return this.http.get<SupplierResponseDto[]>(`${this.apiUrl}/category/${categoryName}`);
  }
}
