import {Injectable} from '@angular/core';
import {HttpClient, HttpHeaders} from '@angular/common/http';
import {BehaviorSubject, Observable} from 'rxjs';
import {tap} from 'rxjs/operators';
import {AuthService} from '../auth/auth-service';
import {
  ProfileNotificationSettingsUpdateRequest,
  ProfilePrivacySettingsUpdateRequest,
  ProfileUpdateRequest,
  UserProfile
} from '../../models/user/user.model';
import {Service} from './service';
import {PasswordChangeRequest} from '../../models/auth/auth.model';


@Injectable({
  providedIn: 'root'
})
export class UserService extends Service {

  private currentUserSubject = new BehaviorSubject<UserProfile | null>(null);
  public currentUser$ = this.currentUserSubject.asObservable();

  protected apiUrl: string;

  constructor(http: HttpClient, private authService: AuthService) {
    super(http);
    this.apiUrl = `${this.baseServiceApiUrl}`;
  }

  /**
   * Change user password
   */
  changePassword(passwordData: PasswordChangeRequest): Observable<any> {
    return this.http.patch(`${this.apiUrl}/change-password`, passwordData);
  }

  /**
   * Get current user profile
   */
  getCurrentUser(): Observable<UserProfile> {
    const headers = this.getAuthHeaders();
    return this.http.get<UserProfile>(`${this.apiUrl}/me`, {headers})
      .pipe(
        tap(user => {
          this.currentUserSubject.next(user);
        })
      );
  }

  /**
   * Update user profile
   */
  updateProfile(profileData: ProfileUpdateRequest): Observable<UserProfile> {
    const headers = this.getAuthHeaders();
    return this.http.put<UserProfile>(`${this.apiUrl}/me`, profileData, {headers})
      .pipe(
        tap(user => {
          this.currentUserSubject.next(user);
        })
      );
  }

  /**
   * Change user privacy settings
   */
  changePrivacySettings(privacySettings: ProfilePrivacySettingsUpdateRequest): Observable<any> {
    const headers = this.getAuthHeaders();
    return this.http.patch(`${this.apiUrl}/me/change-privacy-settings`, privacySettings, {headers});
  }

  /**
   * Change user privacy settings
   */
  changeNotificationSettings(notificationSettings: ProfileNotificationSettingsUpdateRequest): Observable<any> {
    const headers = this.getAuthHeaders();
    return this.http.patch(`${this.apiUrl}/me/change-notifications-settings`, notificationSettings, {headers});
  }


  /**
   * Delete user account
   */
  deleteAccount(): any {
    console.log("deleteAccount")
    return true;
  }

  /**
   * Load current user profile
   */
  loadCurrentUser(): void {
    if (this.authService.isAuthenticated()) {
      this.getCurrentUser().subscribe({
        next: (user) => {
          this.currentUserSubject.next(user);
        },
        error: (error) => {
          console.error('Failed to load current user:', error);
          this.currentUserSubject.next(null);
        }
      });
    }
  }


  /**
   * Refresh current user data
   */
  refreshCurrentUser(): void {
    this.loadCurrentUser();
  }


  /**
   * Get auth headers with JWT token
   */
  private getAuthHeaders(): HttpHeaders {
    const token = this.authService.getToken();
    return new HttpHeaders({
      'Content-Type': 'application/json',
      'Authorization': `Bearer ${token}`
    });
  }

  /**
   * Get profile completion percentage
   */
  getProfileCompletionPercentage(user: UserProfile): number {
    const fields = [
      user.firstName,
      user.lastName,
      user.email,
      user.bio,
      user.location,
      user.avatar
    ];

    const completedFields = fields.filter(field => field && field.trim() !== '').length;
    return Math.round((completedFields / fields.length) * 100);
  }
}
