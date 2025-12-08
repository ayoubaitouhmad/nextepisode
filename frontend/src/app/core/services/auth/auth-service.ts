// auth.service.ts
import {Injectable} from '@angular/core';
import {HttpClient, HttpHeaders} from '@angular/common/http';
import {BehaviorSubject, Observable} from 'rxjs';
import {tap} from 'rxjs/operators';
import {environment} from '../../../../environments/environment';
import {User} from '../../models/user/user.model';
import {LoginRequest, LoginResponse, SignupRequest} from '../../models/auth/auth.model';
import {Service} from './service';


@Injectable({providedIn: 'root'})
export class AuthService extends Service {
  protected apiUrl: string;

  private _loggedIn$ = new BehaviorSubject<boolean>(!!this.getToken());
  public loggedIn$ = this._loggedIn$.asObservable();


  constructor(http: HttpClient) {
    super(http);
    this.apiUrl = `${this.baseServiceApiUrl}`;
  }

  isAuthenticated(): boolean {
    // you could also decode the JWT and check exp here
    return !!this.getToken();
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
        localStorage.setItem('jwt', res.token);
        localStorage.setItem("user", JSON.stringify(res.user));
        this._loggedIn$.next(true);
      })
    );
  }

  logout() {
    localStorage.removeItem('jwt');
    localStorage.removeItem('user');
    this._loggedIn$.next(false);

  }

  getToken() {
    return localStorage.getItem('jwt');
  }

  getUser() {
    return JSON.parse(localStorage.getItem("user")!);
  }
}
