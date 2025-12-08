import {Router, Routes} from '@angular/router';
import {Component} from '@angular/core';
import {AuthService} from '../../core/services/auth/auth-service';

export const AUTH_ROUTES: Routes = [
  {
    path: 'login',
    loadComponent: () => import('./pages/login/login.component').then(m => m.LoginComponent)
  },
  {
    path: 'signup',
    loadComponent: () => import('./pages/signup/signup.component').then(m => m.SignupComponent)
  },
  {
    path: 'logout',
    loadComponent: () => LogoutComponent
  },
  {
    path: '',
    redirectTo: 'login',
    pathMatch: 'full'
  }
];

@Component({template: ''})
class LogoutComponent {
  constructor(private auth: AuthService, private router: Router) {
    auth.logout();
    router.navigate(['/auth/login']);
  }
}
