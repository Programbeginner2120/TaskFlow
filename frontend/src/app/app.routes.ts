import { Routes } from '@angular/router';

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
];
