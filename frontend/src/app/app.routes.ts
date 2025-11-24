import {Routes} from '@angular/router';
import {AuthGuard} from './guards/auth.guard';
import {NoAuthGuard} from './guards/no-auth.guard';

export const routes: Routes = [
  {
    path: '',
    loadComponent: () => import('./core/layouts/app-layout/app-layout.component').then(m => m.AppLayoutComponent),
    children: [
      {
        path: '',

        loadChildren: () => import('./features/movies/movies.routes').then(m => m.MOVIE_ROUTES)
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
        canActivate: [AuthGuard],
        loadChildren: () => import('./features/user/user.routes').then(m => m.AUTH_ROUTES)
      }
    ]
  },
];
