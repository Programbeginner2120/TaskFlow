import { HttpHandlerFn, HttpInterceptorFn, HttpRequest, HttpErrorResponse } from "@angular/common/http";
import { inject } from "@angular/core";
import { toObservable } from "@angular/core/rxjs-interop";
import { catchError, filter, switchMap, take, throwError } from "rxjs";
import { AuthService } from "../services/auth.service";

export const AuthInterceptor: HttpInterceptorFn = (req: HttpRequest<any>, next: HttpHandlerFn) => {
    const authService = inject(AuthService);

    const token = authService.getToken();

    const authReq = token && !req.url.includes('/auth/refresh')
        ? req.clone({ setHeaders: { Authorization: `Bearer ${token}` } })
        : req;

    return next(authReq).pipe(
        catchError((error: HttpErrorResponse) => {
            if (error.status !== 401 || req.url.includes('/auth/refresh')) {
                return throwError(() => error);
            }

            if (authService.isRefreshing()) {
                return toObservable(authService.refreshedToken).pipe(
                    filter(t => t !== null),
                    take(1),
                    switchMap(newToken =>
                        next(req.clone({ setHeaders: { Authorization: `Bearer ${newToken}` } }))
                    )
                );
            }

            authService.startRefresh();

            return authService.refresh().pipe(
                switchMap(response => {
                    authService.completeRefresh(response.token);
                    return next(req.clone({ setHeaders: { Authorization: `Bearer ${response.token}` } }));
                }),
                catchError(refreshError => {
                    authService.failRefresh();
                    authService.logout();
                    return throwError(() => refreshError);
                })
            );
        })
    );
};
