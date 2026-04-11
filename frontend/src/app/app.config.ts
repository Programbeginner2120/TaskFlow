import { ApplicationConfig, inject, provideAppInitializer, provideBrowserGlobalErrorListeners, provideZonelessChangeDetection } from '@angular/core';
import { provideRouter } from '@angular/router';

import { routes } from './app.routes';
import { provideHttpClient, withInterceptors } from '@angular/common/http';
import { UrlInterceptor } from './interceptors/url-interceptor';
import { AuthInterceptor } from './interceptors/auth-interceptor';
import { AuthService } from './services/auth.service';
import { TaskService } from './services/task.service';
import { TaskListService } from './services/task-list.service';
import { firstValueFrom, forkJoin, of, switchMap } from 'rxjs';

export const appConfig: ApplicationConfig = {
  providers: [
    provideBrowserGlobalErrorListeners(),
    provideZonelessChangeDetection(),
    provideRouter(routes),
    provideHttpClient(withInterceptors([UrlInterceptor, AuthInterceptor])),
    provideAppInitializer(() => {
      const authService = inject(AuthService);
      const taskService = inject(TaskService);
      const taskListService = inject(TaskListService);

      return firstValueFrom(
        authService.loadCurrentUser().pipe(
          switchMap(user => {
            if (!user) return of(null);
            return forkJoin([
              taskService.loadAll(),
              taskListService.loadAll(),
            ]);
          })
        )
      );
    }),
  ]
};

