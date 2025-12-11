import {Injectable} from '@angular/core';
import {HttpClient, HttpHeaders} from '@angular/common/http';
import {BehaviorSubject, Observable} from 'rxjs';
import {tap} from 'rxjs/operators';
import {AuthService} from '../auth/auth-service';
import {PasswordChangeRequest} from '../../models/auth/auth.model';
import {
  ProfileNotificationSettingsUpdateRequest,
  ProfilePrivacySettingsUpdateRequest,
  ProfileUpdateRequest,
  UserProfile
} from '../../models/user/user.model';
import {Service} from './service';


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
   * Change user password
   */
  changePassword(passwordData: PasswordChangeRequest): Observable<any> {
    const headers = this.getAuthHeaders();
    return this.http.put(`${this.apiUrl}/change-password`, passwordData, {headers});
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
   * Get user by ID (for public profiles)
   */
  getUserById(userId: number): Observable<UserProfile> {
    return this.http.get<UserProfile>(`${this.apiUrl}/${userId}`);
  }

  /**
   * Delete user account
   */
  deleteAccount(): Observable<any> {
    const headers = this.getAuthHeaders();
    return this.http.delete(`${this.apiUrl}/account`, {headers})
      .pipe(
        tap(() => {
          // Clear current user and logout
          this.currentUserSubject.next(null);
          this.authService.logout();
        })
      );
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
   * Get current user from cache
   */
  getCurrentUserValue(): UserProfile | null {
    return this.currentUserSubject.value;
  }

  /**
   * Refresh current user data
   */
  refreshCurrentUser(): void {
    this.loadCurrentUser();
  }

  /**
   * Update avatar
   */
  updateAvatar(avatarUrl: string): Observable<UserProfile> {
    return this.updateProfile({avatar: avatarUrl});
  }

  /**
   * Update basic profile information
   */
  updateBasicProfile(firstName: string, lastName: string, bio?: string): Observable<UserProfile> {
    return this.updateProfile({
      firstName,
      lastName,
      bio
    });
  }

  /**
   * Update contact information
   */
  updateContactInfo(location?: string, website?: string, phone?: string): Observable<UserProfile> {
    return this.updateProfile({
      location,
      website,
      phone
    });
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
   * Format user's full name
   */
  getFullName(user: UserProfile): string {
    return `${user.firstName} ${user.lastName}`.trim();
  }

  /**
   * Get user's display name (first name + last initial)
   */
  getDisplayName(user: UserProfile): string {
    const firstName = user.firstName || '';
    const lastName = user.lastName || '';
    const lastInitial = lastName ? lastName.charAt(0) + '.' : '';
    return `${firstName} ${lastInitial}`.trim();
  }

  /**
   * Get user's avatar URL or default avatar
   */
  getAvatarUrl(user: UserProfile): string {
    return user.avatar || 'assets/images/default-avatar.png';
  }

  /**
   * Check if user has completed their profile
   */
  isProfileComplete(user: UserProfile): boolean {
    return !!(user.firstName && user.lastName && user.email);
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
