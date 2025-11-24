// no-auth.guard.ts
import {Injectable} from '@angular/core';
import {CanActivate, Router} from '@angular/router';
import {AuthService} from '../core/services/auth-service';


@Injectable({providedIn: 'root'})
export class NoAuthGuard implements CanActivate {
  constructor(private auth: AuthService, private router: Router) {
  }

  canActivate(): boolean {
    if (!this.auth.isAuthenticated()) {
      return true;
    }
    // already logged in → go to home/dashboard
    this.router.navigate(['/']);
    return false;
  }
}
