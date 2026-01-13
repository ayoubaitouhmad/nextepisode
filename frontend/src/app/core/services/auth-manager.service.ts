// auth.service.ts
import {Injectable} from '@angular/core';
import {BehaviorSubject} from 'rxjs';
import {Router} from '@angular/router';


@Injectable({providedIn: 'root'})
export class AuthManagerService {
  private readonly TOKEN_KEY = 'auth_token';
  private readonly USER_KEY = 'current_user';

  private _loggedIn$ = new BehaviorSubject<boolean>(!!this.getToken());
  public loggedIn$ = this._loggedIn$.asObservable();


  constructor(private router: Router) {
  }

  isAuthenticated(): boolean {
    // you could also decode the JWT and check exp here
    return !!this.getToken();
  }

  login(token: string, user: string) {
    localStorage.setItem(this.TOKEN_KEY, token);
    localStorage.setItem(this.USER_KEY, user);
    this._loggedIn$.next(true);
  }

  logout() {
    localStorage.removeItem(this.TOKEN_KEY);
    localStorage.removeItem(this.USER_KEY);
    this._loggedIn$.next(false);

    this.router.navigate(['/auth/login']);
  }

  private isTokenExpired(token: string): boolean {
    try {
      const payload = JSON.parse(atob(token.split('.')[1]));
      const expirationTime = payload.exp * 1000; // Convert to milliseconds
      return Date.now() >= expirationTime;
    } catch (error) {
      return true;
    }
  }

  private hasToken(): boolean {
    const token = localStorage.getItem(this.TOKEN_KEY);
    return !!token && !this.isTokenExpired(token);
  }


  getToken() {
    const token = localStorage.getItem(this.TOKEN_KEY);

    // If token is expired, logout automatically
    if (token && this.isTokenExpired(token)) {
      this.logout();
      return null;
    }

    return token;
  }

  getUser() {
    return JSON.parse(localStorage.getItem("user")!);
  }

  isLoggedIn(): boolean {
    return this.hasToken();
  }
}
