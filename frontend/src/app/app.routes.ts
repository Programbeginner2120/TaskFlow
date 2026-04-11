import { Routes } from '@angular/router';
import { authGuard } from './guards/auth.guard';

export const routes: Routes = [
    {
        path: '',
        redirectTo: '/login',
        pathMatch: 'full'
    },
    {
        path: 'login',
        loadComponent: () => import('./pages/auth/auth.component').then(m => m.AuthComponent)
    },
    {
        path: 'verify-email',
        loadComponent: () => import('./pages/auth/verify-email/verify-email.component')
            .then(m => m.VerifyEmailComponent)
    },
    {
        path: 'reset-password',
        loadComponent: () => import('./pages/auth/reset-password/reset-password.component')
            .then(m => m.ResetPasswordComponent)
    },
    {
        path: 'landing-page',
        loadComponent: () => import('./pages/landing-page/landing-page.component')
            .then(m => m.LandingPageComponent),
        canActivate: [authGuard]
    }
];
