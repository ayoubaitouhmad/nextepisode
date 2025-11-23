import {Router, Routes} from '@angular/router';
import {Component} from '@angular/core';
import {AuthService} from '../../core/services/auth-service';

export const AUTH_ROUTES: Routes = [
  {
    path: 'profile',
    loadComponent: () => import('./pages/profile/profile.component').then(m => m.ProfileComponent)
  },

  {
    path: '',
    redirectTo: 'login',
    pathMatch: 'full'
  }
];
