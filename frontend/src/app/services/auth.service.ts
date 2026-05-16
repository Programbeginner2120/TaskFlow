import { HttpClient } from "@angular/common/http";
import { computed, inject, Injectable, signal } from "@angular/core";
import { Router } from "@angular/router";
import { Observable, tap, catchError, of, Subject, throwError } from "rxjs";
import { LoginRequest, LoginResponse, RefreshResponse, RegisterRequest, UserResponse } from "../interfaces/auth.interface";
import { isTokenExpired } from '../utils/jwt.utils';

@Injectable({
    providedIn: 'root'
})
export class AuthService {

    private readonly http = inject(HttpClient);
    private readonly router = inject(Router);

    private readonly API_URL = '/auth';
    private readonly TOKEN_KEY = 'auth_token';
    private readonly REFRESH_TOKEN_KEY = 'refresh_token';

    private readonly _currentUser = signal<UserResponse | null>(null);
    private readonly _token = signal<string | null>(localStorage.getItem(this.TOKEN_KEY));
    private readonly _refreshToken = signal<string | null>(localStorage.getItem(this.REFRESH_TOKEN_KEY));

    // Refresh-lock signals — used by AuthInterceptor to coordinate concurrent 401s
    private readonly _isRefreshing = signal(false);
    private readonly _refreshedToken = signal<string | null>(null);

    readonly currentUser = this._currentUser.asReadonly();
    readonly isRefreshing = this._isRefreshing.asReadonly();
    readonly refreshedToken = this._refreshedToken.asReadonly();

    readonly isAuthenticated = computed(() => {
        const token = this._token();
        if (!token) return false;
        return !isTokenExpired(token);
    });

    register(request: RegisterRequest): Observable<UserResponse> {
        return this.http.post<UserResponse>(`${this.API_URL}/register`, request);
    }

    login(request: LoginRequest): Observable<LoginResponse> {
        return this.http.post<LoginResponse>(`${this.API_URL}/login`, request).pipe(
            tap(response => {
                this.setToken(response.token);
                this.setRefreshToken(response.refreshToken);
            }),
            catchError(err => {
                if (err.status === 403 && err.error === 'EMAIL_NOT_VERIFIED') {
                    return throwError(() => new Error('EMAIL_NOT_VERIFIED'));
                }
                return throwError(() => err);
            })
        );
    }

    verifyEmail(token: string): Observable<void> {
        return this.http.post<void>(`${this.API_URL}/verify-email?token=${token}`, null);
    }

    resendVerification(email: string): Observable<void> {
        return this.http.post<void>(`${this.API_URL}/resend-verification`, { email });
    }

    forgotPassword(email: string): Observable<void> {
        return this.http.post<void>(`${this.API_URL}/forgot-password`, { email });
    }

    resetPassword(token: string, newPassword: string): Observable<void> {
        return this.http.post<void>(`${this.API_URL}/reset-password`, { token, newPassword });
    }

    logout(): void {
        const refreshToken = this.getRefreshToken();
        if (refreshToken) {
            this.http.post(`${this.API_URL}/logout`, { refreshToken }).subscribe();
        }
        this.clearToken();
        this.clearRefreshToken();
        this._currentUser.set(null);
        this.router.navigate(['/login']);
    }

    loadCurrentUser(): Observable<UserResponse | null> {
        if (!this.getToken()) {
            return of(null);
        }
        return this.http.get<UserResponse>(`${this.API_URL}/me`).pipe(
            tap(user => this._currentUser.set(user)),
            catchError(() => {
                this.clearToken();
                this._currentUser.set(null);
                return of(null);
            })
        );
    }

    getToken(): string | null {
        return this._token();
    }

    getRefreshToken(): string | null {
        return this._refreshToken();
    }

    refresh(): Observable<RefreshResponse> {
        const refreshToken = this.getRefreshToken();
        if (!refreshToken) {
            return throwError(() => new Error('No refresh token available'));
        }
        return this.http.post<RefreshResponse>(`${this.API_URL}/refresh`, { refreshToken }).pipe(
            tap(response => {
                this.setToken(response.token);
                this.setRefreshToken(response.refreshToken);
            })
        );
    }

    startRefresh(): void {
        this._isRefreshing.set(true);
        this._refreshedToken.set(null);
    }

    completeRefresh(token: string): void {
        this._isRefreshing.set(false);
        this._refreshedToken.set(token);
    }

    failRefresh(): void {
        this._isRefreshing.set(false);
    }

    private setToken(token: string): void {
        localStorage.setItem(this.TOKEN_KEY, token);
        this._token.set(token);
    }

    private setRefreshToken(token: string): void {
        localStorage.setItem(this.REFRESH_TOKEN_KEY, token);
        this._refreshToken.set(token);
    }

    private clearToken(): void {
        localStorage.removeItem(this.TOKEN_KEY);
        this._token.set(null);
    }

    private clearRefreshToken(): void {
        localStorage.removeItem(this.REFRESH_TOKEN_KEY);
        this._refreshToken.set(null);
    }

}
