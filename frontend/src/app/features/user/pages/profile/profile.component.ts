import {Component, OnDestroy, OnInit} from '@angular/core';
import {NgIf} from '@angular/common';
import {FormsModule} from '@angular/forms';
import {Router} from '@angular/router';
import {Subscription} from 'rxjs';


import {UserOverviewComponent} from './components/user-overview/user-overview.component';
import {FavoritesComponent} from './components/favorites/favorites.component';
import {SettingsComponent} from './components/settings/settings.component';
import {PasswordChangeRequest, UserProfile, UserService} from '../../../../core/services/user.service';
import {MovieStatistics} from './components/movie.model';
import {UserMovieService} from '../../../../core/services/user-movie.service';
import {AuthService} from '../../../../core/services/auth-service';


@Component({
  selector: 'app-profile',
  standalone: true,
  imports: [
    NgIf,
    FormsModule,
    UserOverviewComponent,
    FavoritesComponent,
    SettingsComponent
  ],
  templateUrl: './profile.component.html',
  styleUrls: ['./profile.component.scss']
})
export class ProfileComponent implements OnInit, OnDestroy {

  activeTab: 'overview' | 'favorites' | 'watched' | 'watchlist' | 'settings' = 'overview';
  currentUser: UserProfile | null = null;
  editedProfile: Partial<UserProfile> = {};
  private userSubscription: Subscription | null = null;

  isEditingProfile = false;
  isChangingPassword = false;

  passwordData: PasswordChangeRequest = {
    currentPassword: '',
    newPassword: '',
    confirmPassword: ''
  };

  movieStatistics: MovieStatistics = {
    favoriteCount: 0,
    watchedCount: 0,
    watchlistCount: 0
  };

  loading = false;
  errorMessage = '';
  successMessage = '';

  constructor(
    private userService: UserService,
    private authService: AuthService,
    private router: Router,
    private userMovieService: UserMovieService
  ) {
    console.clear();
  }

  ngOnInit(): void {
    this.userSubscription = this.userService.currentUser$.subscribe(user => {
      this.currentUser = user;
      if (user) {
        this.editedProfile = {...user};
        this.loadMovieStatistics();
      }
    });

    if (!this.currentUser) {
      this.loadCurrentUser();
    }
  }

  ngOnDestroy(): void {
    if (this.userSubscription) {
      this.userSubscription.unsubscribe();
    }
  }

  loadCurrentUser(): void {
    this.loading = true;
    this.userService.getCurrentUser().subscribe({
      next: (user) => {
        this.currentUser = user;
        this.editedProfile = {...user};
        this.loading = false;
      },
      error: (error) => {
        console.error('Failed to load user profile:', error);
        this.errorMessage = 'Failed to load profile. Please try again.';
        this.loading = false;
      }
    });
  }

  loadMovieStatistics(): void {
    this.userMovieService.getUserMovieStatistics().subscribe({
      next: (statistics) => {
        this.movieStatistics = {
          favoriteCount: (statistics as any).favoriteCount ?? (statistics as any).favorites ?? 0,
          watchedCount: (statistics as any).watchedCount ?? (statistics as any).watched ?? 0,
          watchlistCount: (statistics as any).watchlistCount ?? (statistics as any).watchlist ?? 0
        };
      },
      error: (error) => {
        console.error('Failed to load movie statistics:', error);
      }
    });
  }

  setActiveTab(tab: 'overview' | 'favorites' | 'watched' | 'watchlist' | 'settings'): void {
    this.activeTab = tab;
    if (tab === 'favorites' || tab === 'watched' || tab === 'watchlist' || tab === 'overview') {
      this.loadMovieStatistics();
    }
  }

  startEditingProfile(): void {
    if (this.currentUser) {
      this.editedProfile = {...this.currentUser};
      this.isEditingProfile = true;
      this.clearMessages();
    }
  }

  saveProfile(): void {
    if (!this.currentUser) return;

    this.loading = true;
    this.clearMessages();

    this.userService.updateProfile(this.editedProfile).subscribe({
      next: (updatedUser) => {
        this.currentUser = updatedUser;
        this.editedProfile = {...updatedUser};
        this.isEditingProfile = false;
        this.successMessage = 'Profile updated successfully!';
        this.loading = false;
      },
      error: (error) => {
        console.error('Failed to update profile:', error);
        this.errorMessage = error.error?.error || 'Failed to update profile. Please try again.';
        this.loading = false;
      }
    });
  }

  cancelEditing(): void {
    if (this.currentUser) {
      this.editedProfile = {...this.currentUser};
    }
    this.isEditingProfile = false;
    this.clearMessages();
  }

  onEditedProfileChange(profile: Partial<UserProfile>): void {
    this.editedProfile = profile;
  }

  startChangingPassword(): void {
    this.passwordData = {
      currentPassword: '',
      newPassword: '',
      confirmPassword: ''
    };
    this.isChangingPassword = true;
    this.clearMessages();
  }

  savePassword(): void {
    if (this.passwordData.newPassword !== this.passwordData.confirmPassword) {
      this.errorMessage = 'New password and confirmation password do not match.';
      return;
    }

    if (this.passwordData.newPassword.length < 6) {
      this.errorMessage = 'New password must be at least 6 characters long.';
      return;
    }

    this.loading = true;
    this.clearMessages();

    this.userService.changePassword(this.passwordData).subscribe({
      next: () => {
        this.isChangingPassword = false;
        this.passwordData = {
          currentPassword: '',
          newPassword: '',
          confirmPassword: ''
        };
        this.successMessage = 'Password changed successfully!';
        this.loading = false;
      },
      error: (error) => {
        console.error('Failed to change password:', error);
        this.errorMessage = error.error?.error || 'Failed to change password. Please try again.';
        this.loading = false;
      }
    });
  }

  cancelPasswordChange(): void {
    this.isChangingPassword = false;
    this.passwordData = {
      currentPassword: '',
      newPassword: '',
      confirmPassword: ''
    };
    this.clearMessages();
  }

  onPasswordDataChange(data: PasswordChangeRequest): void {
    this.passwordData = data;
  }

  deleteAccount(): void {
    if (confirm('Are you sure you want to delete your account? This action cannot be undone.')) {
      this.loading = true;
      this.userService.deleteAccount().subscribe({
        next: () => {
          this.router.navigate(['/auth/login']);
        },
        error: (error) => {
          console.error('Failed to delete account:', error);
          this.errorMessage = 'Failed to delete account. Please try again.';
          this.loading = false;
        }
      });
    }
  }

  private clearMessages(): void {
    this.errorMessage = '';
    this.successMessage = '';
  }

  getFullName(): string {
    if (!this.currentUser) return '';
    return `${this.currentUser.firstName} ${this.currentUser.lastName}`.trim();
  }

  getAvatarUrl(): string {
    if (!this.currentUser?.avatar) {
      return 'https://images.unsplash.com/photo-1472099645785-5658abf4ff4e?w=150&h=150&fit=crop&crop=face';
    }
    return this.currentUser.avatar;
  }

  getFormattedDate(dateString: string): string {
    const date = new Date(dateString);
    return date.toLocaleDateString('en-US', {
      year: 'numeric',
      month: 'long',
      day: 'numeric'
    });
  }

  getProfileCompletionPercentage(): number {
    if (!this.currentUser) return 0;
    return this.userService.getProfileCompletionPercentage(this.currentUser);
  }
}
