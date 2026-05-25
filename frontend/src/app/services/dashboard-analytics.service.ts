import { HttpClient } from '@angular/common/http';
import { inject, Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { DashboardAnalyticsApiResponse, DashboardAnalyticsRequest } from '../interfaces/dashboard.interface';

@Injectable({ providedIn: 'root' })
export class DashboardAnalyticsService {

    private readonly http = inject(HttpClient);
    private readonly API = '/dashboard-analytics';

    getAnalytics(request: DashboardAnalyticsRequest): Observable<DashboardAnalyticsApiResponse> {
        return this.http.post<DashboardAnalyticsApiResponse>(this.API, request);
    }
}
