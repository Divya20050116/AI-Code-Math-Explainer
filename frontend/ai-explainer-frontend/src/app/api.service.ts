import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({ providedIn: 'root' })
export class ApiService {
  private apiUrl = 'http://localhost:8080/api/explain'; // Make sure Spring Boot is running

  constructor(private http: HttpClient) {}

  explain(input: string, type: string): Observable<string> {
  return this.http.post<string>(this.apiUrl, { input, type }, { responseType: 'text' as 'json' });
}

}
