import { HttpClient,HttpHandler,HttpHeaders } from "@angular/common/http";
import { Inject,inject,Injectable } from "@angular/core";
import { Observable } from "rxjs";


@Injectable({
    providedIn:'root'
})

export class PurchaseOrderService{

    private http = inject(HttpClient)
    private baseUrl ='http://localhost:808/api/auth/purchaseorders'
    
    private getAuthHeaders(): HttpHeaders{
        const token = localStorage.getItem('token') || '';
        return new HttpHeaders({
            'Authorization':`Bearer ${token}`,
            'Content-Type':'application/json'
        })
    }

    


    
}