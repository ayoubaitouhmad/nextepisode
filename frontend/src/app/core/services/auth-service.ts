// auth.service.ts
import {Injectable} from '@angular/core';
import {HttpClient, HttpHeaders} from '@angular/common/http';
import {BehaviorSubject, Observable} from 'rxjs';
import {tap} from 'rxjs/operators';
import {environment} from '../../../environments/environment';


export interface SignupRequest {
  username: string;
  email: string;
  password: string;
}

export interface User {
  id: number;
  firstName: string;
  lastName: string;
  email: string;
  role: string;
  emailVerified: boolean;
  isActive: boolean;
  createdAt: string;
  lastLogin: string | null;
}

export interface LoginRequest {
  username: string;
  password: string;
}

export interface LoginResponse {
  token: string;
  message: string;
  user: {
    id: number;
    email: string;
    username: string
  };
}

@Injectable({providedIn: 'root'})
export class AuthService {
  private base = environment.apiUrl;
  private _loggedIn$ = new BehaviorSubject<boolean>(!!this.getToken());
  public loggedIn$ = this._loggedIn$.asObservable();


  constructor(private http: HttpClient) {

  }

  isAuthenticated(): boolean {
    // you could also decode the JWT and check exp here
    return !!this.getToken();
  }


  /** Create a new user */
  register(req: SignupRequest): Observable<User> {
    return this.http.post<User>(
      `http://localhost:8081/api/v1/auth/register`,
      req,
      {
        headers: new HttpHeaders({'Content-Type': 'application/json'})
      }
    );
  }

  /** Log in and get a JWT + user info */
  login(req: LoginRequest): Observable<LoginResponse> {
    return this.http.post<LoginResponse>(
      `http://localhost:8081/api/v1/auth/login`,
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
