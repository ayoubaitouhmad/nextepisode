import {Routes} from '@angular/router';

export const MOVIE_ROUTES: Routes = [
  {
    path: '',
    loadComponent: () => import('./pages/home/home.component').then(m => m.HomeComponent)
  },

  {
    path: 'movie/:id',
    loadComponent: () => import('./pages/details/content-details.component').then(m => m.ContentDetailsComponent)
  },
  {
    path: 'tv/:id',
    loadComponent: () => import('./pages/details/content-details.component').then(m => m.ContentDetailsComponent)
  },
  {
    path: 'browse/discover',
    loadComponent: () => import('./pages/browse/browse.component').then(m => m.BrowseComponent)
  },
];
