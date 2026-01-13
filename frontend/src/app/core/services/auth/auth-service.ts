// auth.service.ts
import {inject, Injectable} from '@angular/core';
import {HttpClient, HttpHeaders} from '@angular/common/http';
import {Observable} from 'rxjs';
import {tap} from 'rxjs/operators';
import {User} from '../../models/user/user.model';
import {LoginRequest, LoginResponse, SignupRequest} from '../../models/auth/auth.model';
import {Service} from './service';
import {AuthManagerService} from '../auth-manager.service';


@Injectable({providedIn: 'root'})
export class AuthService extends Service {
  private authManager: AuthManagerService = inject(AuthManagerService);

  protected apiUrl: string;

  constructor(http: HttpClient) {
    super(http);
    this.apiUrl = `${this.baseServiceApiUrl}`;
  }

  /** Create a new user */
  register(req: SignupRequest): Observable<User> {
    return this.http.post<User>(
      `${this.apiUrl}/register`,
      req,
      {
        headers: new HttpHeaders({'Content-Type': 'application/json'})
      }
    );
  }

  /** Log in and get a JWT + user info */
  login(req: LoginRequest): Observable<LoginResponse> {
    return this.http.post<LoginResponse>(
      `${this.apiUrl}/login`,
      req,
      {headers: new HttpHeaders({'Content-Type': 'application/json'})}
    ).pipe(
      tap(res => {
        this.authManager.login(res.token, JSON.stringify(res.user))

      })
    );
  }

  logout() {
    this.authManager.logout();
  }


  isAuthenticated() {
    return this.authManager.isAuthenticated();
  }

  getToken() {
    return this.authManager.getToken();
  }

  getUser() {
    return this.authManager.getUser();
  }

}
