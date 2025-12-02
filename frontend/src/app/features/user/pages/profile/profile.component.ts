import {Component, OnDestroy, OnInit, ViewChild} from '@angular/core';
import {NgIf} from '@angular/common';
import {Router} from '@angular/router';
import {Subscription} from 'rxjs';

import {PasswordChangeRequest, UserProfile, UserService} from '../../../../core/services/user.service';
import {AuthService} from '../../../../core/services/auth-service';
import {MovieStatistics, UserMovieService} from '../../../../core/services/user-movie.service';

// Child components
import {UserOverviewComponent} from './components/user-overview/user-overview.component';

type TabType = 'overview' | 'favorites' | 'watched' | 'watchlist' | 'settings';

@Component({
  selector: 'app-profile',
  standalone: true,
  imports: [
    NgIf,
    UserOverviewComponent
  ],
  templateUrl: './profile.component.html',
  styleUrls: ['./profile.component.scss']
})
export class ProfileComponent implements OnInit, OnDestroy {


  activeTab: TabType = 'overview';
  isEditingProfile = false;
  loading = false;
  errorMessage = '';
  successMessage = '';
  warningMessage = '';

  currentUser: UserProfile | null = null;
  editedProfile: Partial<UserProfile> = {};
  private userSubscription: Subscription | null = null;

  movieStatistics: MovieStatistics = {
    favorites: 0,
    watched: 0,
    watchlist: 0
  };

  constructor(
    private userService: UserService,
    private authService: AuthService,
    private router: Router,
    private userMovieService: UserMovieService
  ) {
  }

  ngOnInit(): void {
    this.userSubscription = this.userService.currentUser$.subscribe(user => {
      this.currentUser = user;
      if (user) {
        this.editedProfile = {...user};
      }
    });

    if (!this.currentUser) {
      this.loadCurrentUser();
    }

    this.loadMovieStatistics();
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

        if (!user.isDirty) {
          this.warningMessage = 'Please complete your profile';
        }
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
        console.log(statistics)
        this.movieStatistics = statistics;
      },
      error: (error) => {
        console.error('Failed to load movie statistics:', error);
      }
    });
  }

  setActiveTab(tab: TabType): void {
    this.activeTab = tab;

    // Refresh statistics when switching to movie tabs
    if (tab === 'favorites' || tab === 'watched' || tab === 'watchlist') {
      this.loadMovieStatistics();
    }
  }

  // Profile editing methods
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

  // Password change methods
  onChangePassword(passwordData: PasswordChangeRequest): void {
    this.loading = true;
    this.clearMessages();

    this.userService.changePassword(passwordData).subscribe({
      next: () => {
        this.successMessage = 'Password changed successfully!';
        this.loading = false;
        // Notify settings component of success
        // if (this.settingsComponent) {
        //   this.settingsComponent.onPasswordChangeSuccess();
        // }
      },
      error: (error) => {
        console.error('Failed to change password:', error);
        this.errorMessage = error.error?.error || 'Failed to change password. Please try again.';
        this.loading = false;
      }
    });
  }

  // Account deletion
  onDeleteAccount(): void {
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

  // Helper methods
  getFullName(): string {
    if (!this.currentUser) return '';
    if (!this.currentUser.isDirty) return this.currentUser.username;
    return `${this.currentUser.firstName} ${this.currentUser.lastName}`.trim();
  }

  getAvatarUrl(): string {
    if (!this.currentUser?.avatar) {
      return 'https://images.unsplash.com/photo-1472099645785-5658abf4ff4e?w=150&h=150&fit=crop&crop=face';
    }
    return this.currentUser.avatar;
  }

  getFormattedDate(dateString: string): string {
    if (!dateString) return '';
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

  private clearMessages(): void {
    this.errorMessage = '';
    this.successMessage = '';
  }

  dismissError(): void {
    this.errorMessage = '';
  }

  dismissWarning(): void {
    this.warningMessage = '';
  }

  dismissSuccess(): void {
    this.successMessage = '';
  }
}
