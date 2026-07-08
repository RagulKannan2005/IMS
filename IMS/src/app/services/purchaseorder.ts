import { HttpClient,HttpHandler,HttpHeaders } from "@angular/common/http";
import { Inject,inject,Injectable } from "@angular/core";
import { Observable } from "rxjs";


@Injectable({
    providedIn:'root'
})

export class PurchaseOrderService{

    private http = inject(HttpClient)
    private baseUrl ='http://localhost:8083/api/v1/purrchaseorder'
    
    private getAuthHeaders(): HttpHeaders{
        const token = localStorage.getItem('token') || '';
        return new HttpHeaders({
            'Authorization':`Bearer ${token}`,
            'Content-Type':'application/json'
        })
    }

    getPurchaseOrdersBySupplier(supplierId: number): Observable<any> {
        return this.http.get(`${this.baseUrl}/findbysupplierid?supplierId=${supplierId}`, { headers: this.getAuthHeaders() });
    }

    addPurchaseOrder(poData: any): Observable<any> {
        return this.http.post(`${this.baseUrl}/addpurchaseorder`, poData, { headers: this.getAuthHeaders() });
    }

    getAllPurchaseOrders(): Observable<any> {
        return this.http.get(`${this.baseUrl}/findallpurchaseorders`, { headers: this.getAuthHeaders() });
    }
    
    updateOrderStatus(id: number, status: string): Observable<any> {
        return this.http.post(`${this.baseUrl}/${id}/status?status=${status}`, {}, { headers: this.getAuthHeaders() });
    }

    receivePurchaseOrder(payload: any): Observable<any> {
        return this.http.post(`${this.baseUrl}/receivepurchaseorder`, payload, { headers: this.getAuthHeaders() });
    }

    
}