import { HttpClient, HttpParams } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { 
  PurchaseOrderRequestDto, 
  PurchaseOrderResponseDto, 
  PurchaseOrderItemRequestDto, 
  PurchaseOrderItemResponseDto 
} from '../../shared/models/interfaces';

@Injectable({
  providedIn: 'root'
})
export class PurchaseOrderService {
  private readonly poApiUrl = 'http://localhost:8082/api/v1/purchase-orders';
  private readonly itemApiUrl = 'http://localhost:8082/api/v1/purchaseorderitems';

  constructor(private http: HttpClient) {}

  /* ========================================================================
     PURCHASE ORDER ENDPOINTS
     ======================================================================== */

  addPurchaseOrder(dto: PurchaseOrderRequestDto): Observable<PurchaseOrderResponseDto> {
    return this.http.post<PurchaseOrderResponseDto>(`${this.poApiUrl}/addpurchaseorder`, dto);
  }

  updatePurchaseOrder(id: number, dto: PurchaseOrderRequestDto): Observable<PurchaseOrderResponseDto> {
    return this.http.post<PurchaseOrderResponseDto>(`${this.poApiUrl}/updatepurchaseorder/${id}`, dto);
  }

  deletePurchaseOrder(id: number): Observable<void> {
    return this.http.post<void>(`${this.poApiUrl}/deletepurchaseorder/${id}`, {});
  }

  findByPurchaseOrderNumber(poNumber: string): Observable<PurchaseOrderResponseDto> {
    const params = new HttpParams().set('poNumber', poNumber);
    return this.http.post<PurchaseOrderResponseDto>(`${this.poApiUrl}/findbypurchaseordernumber`, {}, { params });
  }

  getAllPurchaseOrders(): Observable<PurchaseOrderResponseDto[]> {
    return this.http.post<PurchaseOrderResponseDto[]>(`${this.poApiUrl}/findallpurchaseorders`, {});
  }

  findBySupplierId(supplierId: number): Observable<PurchaseOrderResponseDto[]> {
    const params = new HttpParams().set('supplierId', supplierId);
    return this.http.post<PurchaseOrderResponseDto[]>(`${this.poApiUrl}/findbysupplierid`, {}, { params });
  }

  findByStatus(status: string): Observable<PurchaseOrderResponseDto[]> {
    const params = new HttpParams().set('status', status);
    return this.http.post<PurchaseOrderResponseDto[]>(`${this.poApiUrl}/findbystatus`, {}, { params });
  }

  findByCreatedBy(userId: number): Observable<PurchaseOrderResponseDto[]> {
    const params = new HttpParams().set('userId', userId);
    return this.http.post<PurchaseOrderResponseDto[]>(`${this.poApiUrl}/findbycreatedby`, {}, { params });
  }

  findByOrderDateRange(from: string, to: string): Observable<PurchaseOrderResponseDto[]> {
    const params = new HttpParams().set('from', from).set('to', to);
    return this.http.post<PurchaseOrderResponseDto[]>(`${this.poApiUrl}/findbyorderdatetrange`, {}, { params });
  }

  receivePurchaseOrder(id: number): Observable<PurchaseOrderResponseDto> {
    const params = new HttpParams().set('id', id);
    return this.http.post<PurchaseOrderResponseDto>(`${this.poApiUrl}/receivepurchaseorder`, {}, { params });
  }

  updateStatus(id: number, status: string): Observable<PurchaseOrderResponseDto> {
    const params = new HttpParams().set('status', status);
    return this.http.post<PurchaseOrderResponseDto>(`${this.poApiUrl}/${id}/status`, {}, { params });
  }

  /* ========================================================================
     PURCHASE ORDER ITEM ENDPOINTS
     ======================================================================== */

  addPurchaseOrderItem(dto: PurchaseOrderItemRequestDto): Observable<PurchaseOrderItemResponseDto> {
    return this.http.post<PurchaseOrderItemResponseDto>(this.itemApiUrl, dto);
  }

  updatePurchaseOrderItem(id: number, dto: PurchaseOrderItemRequestDto): Observable<PurchaseOrderItemResponseDto> {
    return this.http.put<PurchaseOrderItemResponseDto>(`${this.itemApiUrl}/${id}`, dto);
  }

  deletePurchaseOrderItem(id: number): Observable<void> {
    return this.http.delete<void>(`${this.itemApiUrl}/${id}`);
  }

  getPurchaseOrderItemById(id: number): Observable<PurchaseOrderItemResponseDto> {
    return this.http.get<PurchaseOrderItemResponseDto>(`${this.itemApiUrl}/${id}`);
  }

  getAllPurchaseOrderItems(): Observable<PurchaseOrderItemResponseDto[]> {
    return this.http.get<PurchaseOrderItemResponseDto[]>(this.itemApiUrl);
  }

  getItemsByPurchaseOrder(purchaseOrderId: number): Observable<PurchaseOrderItemResponseDto[]> {
    return this.http.get<PurchaseOrderItemResponseDto[]>(`${this.itemApiUrl}/purchaseorder/${purchaseOrderId}`);
  }

  getItemsByProduct(productId: number): Observable<PurchaseOrderItemResponseDto[]> {
    return this.http.get<PurchaseOrderItemResponseDto[]>(`${this.itemApiUrl}/product/${productId}`);
  }
}
