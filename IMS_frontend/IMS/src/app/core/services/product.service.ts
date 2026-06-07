import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { ProductRequestDto, ProductResponseDto, StockAdjustmentRequest } from '../../shared/models/interfaces';

@Injectable({
  providedIn: 'root'
})
export class ProductService {
  private readonly apiUrl = 'http://localhost:8083/api/v1/products';

  constructor(private http: HttpClient) {}

  addProduct(dto: ProductRequestDto): Observable<ProductResponseDto> {
    return this.http.post<ProductResponseDto>(`${this.apiUrl}/addproduct`, dto);
  }

  getAllProducts(): Observable<ProductResponseDto[]> {
    return this.http.get<ProductResponseDto[]>(`${this.apiUrl}/allproducts`);
  }

  getProductById(id: number): Observable<ProductResponseDto> {
    return this.http.get<ProductResponseDto>(`${this.apiUrl}/${id}`);
  }

  getActiveProducts(): Observable<ProductResponseDto[]> {
    return this.http.get<ProductResponseDto[]>(`${this.apiUrl}/activeproducts`);
  }

  getProductByName(name: string): Observable<ProductResponseDto> {
    return this.http.get<ProductResponseDto>(`${this.apiUrl}/productname/${name}`);
  }

  getProductBySku(sku: string): Observable<ProductResponseDto> {
    return this.http.get<ProductResponseDto>(`${this.apiUrl}/productsku/${sku}`);
  }

  getProductsByCategory(categoryName: string): Observable<ProductResponseDto[]> {
    return this.http.get<ProductResponseDto[]>(`${this.apiUrl}/productcategory/${categoryName}`);
  }

  updateProduct(id: number, dto: ProductRequestDto): Observable<ProductResponseDto> {
    return this.http.put<ProductResponseDto>(`${this.apiUrl}/updateproduct/${id}`, dto);
  }

  deleteProduct(id: number): Observable<ProductResponseDto> {
    return this.http.delete<ProductResponseDto>(`${this.apiUrl}/deleteproduct/${id}`);
  }

  adjustStock(id: number, request: StockAdjustmentRequest): Observable<ProductResponseDto> {
    return this.http.post<ProductResponseDto>(`${this.apiUrl}/${id}/adjust-stock`, request);
  }

  getProductsBySupplier(supplierId: number): Observable<ProductResponseDto[]> {
    return this.http.get<ProductResponseDto[]>(`${this.apiUrl}/supplier/${supplierId}`);
  }
}
