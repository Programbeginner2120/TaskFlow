import { ApplicationConfig, inject, provideAppInitializer, provideBrowserGlobalErrorListeners, provideZonelessChangeDetection } from '@angular/core';
import { provideRouter } from '@angular/router';

import { routes } from './app.routes';
import { provideHttpClient, withInterceptors } from '@angular/common/http';
import { UrlInterceptor } from './interceptors/url-interceptor';
import { AuthInterceptor } from './interceptors/auth-interceptor';
import { AuthService } from './services/auth.service';
import { TaskStateService } from './services/task-state.service';
import { TaskListStateService } from './services/task-list-state.service';
import { firstValueFrom, forkJoin, of, switchMap } from 'rxjs';
import { TaskListTemplateStateService } from './services/task-list-template-state.service';
import { provideEchartsCore } from 'ngx-echarts';
import * as echarts from 'echarts/core';
import { LineChart, BarChart, GaugeChart } from 'echarts/charts';
import {
  GridComponent,
  TooltipComponent,
  LegendComponent,
  TitleComponent,
} from 'echarts/components';
import { CanvasRenderer } from 'echarts/renderers';

echarts.use([
  LineChart,
  BarChart,
  GaugeChart,
  GridComponent,
  TooltipComponent,
  LegendComponent,
  TitleComponent,
  CanvasRenderer
]);

export const appConfig: ApplicationConfig = {
  providers: [
    provideBrowserGlobalErrorListeners(),
    provideZonelessChangeDetection(),
    provideRouter(routes),
    provideHttpClient(withInterceptors([UrlInterceptor, AuthInterceptor])),
    provideEchartsCore({ echarts }),
    provideAppInitializer(() => {
      const authService = inject(AuthService);
      const taskStateService = inject(TaskStateService);
      const taskListStateService = inject(TaskListStateService);
      const taskListTemplateStateService = inject(TaskListTemplateStateService);

      return firstValueFrom(
        authService.loadCurrentUser().pipe(
          switchMap(user => {
            if (!user) return of(null);
            return forkJoin([
              taskStateService.loadAll(),
              taskListStateService.loadAll(),
              taskListTemplateStateService.loadAll()
            ]);
          })
        )
      );
    }),
  ]
};

