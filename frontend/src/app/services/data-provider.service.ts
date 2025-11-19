import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { DataProviderProduct, ImportResult } from '../models/product.model';

@Injectable({
  providedIn: 'root'
})
export class DataProviderService {
  private apiUrl = 'http://localhost:8080/api/dataprovider';
  private productsApiUrl = 'http://localhost:8080/api/dataprovider-products';

  constructor(private http: HttpClient) {}

  getAllDataProviderProducts(): Observable<DataProviderProduct[]> {
    return this.http.get<DataProviderProduct[]>(this.productsApiUrl);
  }

  importJsonProducts(): Observable<ImportResult> {
    return this.http.post<ImportResult>(`${this.apiUrl}/import/json`, {});
  }
}