import {Routes} from '@angular/router';
import {NoAuthGuard} from './guards/no-auth.guard';
import {authGuard} from './guards/auth.guard';

export const routes: Routes = [
  {
    path: '',
    loadComponent: () => import('./core/layouts/app-layout/app-layout.component').then(m => m.AppLayoutComponent),
    children: [
      {
        path: '',
        loadChildren: () => import('./features/home/home.routes').then(m => m.MOVIE_ROUTES),
        canActivate: [authGuard]
      },
      {
        path: 'about',
        loadChildren: () => import('./features/about/about.routes').then(m => m.ABOUT_ROUTES)
      },
      {
        path: 'auth',
        canActivate: [NoAuthGuard],
        loadChildren: () => import('./features/auth/auth.routes').then(m => m.AUTH_ROUTES),
      },
      {
        path: '',
        loadChildren: () => import('./features/user/user.routes').then(m => m.AUTH_ROUTES)
      }
    ]
  },
];
