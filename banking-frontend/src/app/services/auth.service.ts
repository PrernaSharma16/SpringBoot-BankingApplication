import { HttpClient } from "@angular/common/http";
import { Injectable } from "@angular/core";
import { Observable } from "rxjs";

export interface AuthRequest{
    username: string;
    password: string;
}

@Injectable({
    providedIn: 'root'
})
export class AuthService{
    private apiUrl = 'http://localhost:8080/auth/login';

    constructor(private http: HttpClient) {}

    login(data: AuthRequest): Observable<string> {
        return this.http.post(this.apiUrl, data, {responseType: 'text'});
    }
}