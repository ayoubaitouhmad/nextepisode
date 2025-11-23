import {NgFor, NgIf} from '@angular/common';
import {Component, OnInit, AfterViewInit, ViewChild, ElementRef, OnDestroy} from '@angular/core';
import {FormsModule} from '@angular/forms';
import {UserService, UserProfile, PasswordChangeRequest} from '../../../../core/services/user.service';
import {AuthService} from '../../../../core/services/auth-service';
import {Router} from '@angular/router';
import {Subscription} from 'rxjs';
import {
  UserMovieService,
  MovieDto,
  MovieStatistics,
  DetailedMovieStatistics
} from '../../../../core/services/user-movie.service';

interface Movie {
  id: number;
  title: string;
  year: number;
  genre: string;
  rating: number;
  poster: string;
}

@Component({
  selector: 'app-profile',
  imports: [NgFor, FormsModule, NgIf],
  templateUrl: './profile.component.html',
  styleUrls: ['./profile.component.scss']
})
export class ProfileComponent implements OnInit, AfterViewInit, OnDestroy {
  @ViewChild('favMoviesSwiper', {static: false}) favMoviesSwiper!: ElementRef;
  @ViewChild('watchedSwiper', {static: false}) watchedSwiper!: ElementRef;
  @ViewChild('toWatchSwiper', {static: false}) toWatchSwiper!: ElementRef;

  activeTab: 'overview' | 'favorites' | 'watched' | 'watchlist' | 'settings' = 'overview';
  isEditingProfile = false;
  isChangingPassword = false;
  loading = false;
  errorMessage = '';
  successMessage = '';

  currentUser: UserProfile | null = null;
  editedProfile: Partial<UserProfile> = {};
  private userSubscription: Subscription | null = null;

  passwordData: PasswordChangeRequest = {
    currentPassword: '',
    newPassword: '',
    confirmPassword: ''
  };

  favoriteMovies: MovieDto[] = [];
  favoriteMoviesGrouped: { movies: MovieDto[], tvSeries: MovieDto[] } = {movies: [], tvSeries: []};

  watchedMovies: MovieDto[] = [];
  watchedMoviesGrouped: { movies: MovieDto[], tvSeries: MovieDto[] } = {movies: [], tvSeries: []};

  toWatchMovies: MovieDto[] = [];
  toWatchMoviesGrouped: { movies: MovieDto[], tvSeries: MovieDto[] } = {movies: [], tvSeries: []};

  // Statistics properties
  movieStatistics: MovieStatistics = {
    favorites: 0,
    watched: 0,
    watchlist: 0
  };

  detailedMovieStatistics: DetailedMovieStatistics = {
    favorites: {total: 0, movies: 0, tvSeries: 0},
    watched: {total: 0, movies: 0, tvSeries: 0},
    watchlist: {total: 0, movies: 0, tvSeries: 0}
  };

  constructor(
    private userService: UserService,
    private authService: AuthService,
    private router: Router,
    private userMovieService: UserMovieService
  ) {

    console.log("dsfdsfds")
  }

  ngOnInit(): void {
    // Subscribe to current user changes
    this.userSubscription = this.userService.currentUser$.subscribe(user => {
      this.currentUser = user;
      if (user) {
        this.editedProfile = {...user};
        // Load statistics when user is available
        this.loadMovieStatistics();
        // Clean up duplicates when profile loads
        this.cleanupDuplicates();
      }
    });

    // Load current user if not already loaded
    if (!this.currentUser) {
      this.loadCurrentUser();
    }
  }

  cleanupDuplicates(): void {
    this.userMovieService.cleanupDuplicates().subscribe({
      next: () => {
        console.log('Duplicates cleaned up successfully');
      },
      error: (error) => {
        console.error('Failed to cleanup duplicates:', error);
      }
    });
  }

  ngOnDestroy(): void {
    if (this.userSubscription) {
      this.userSubscription.unsubscribe();
    }
  }

  ngAfterViewInit(): void {
    // Initialize swipers after view init
    this.initializeSwiper('fav-movies-swiper');
    this.initializeSwiper('watched-swiper');
    this.initializeSwiper('to-watch-swiper');
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

  setActiveTab(tab: 'overview' | 'favorites' | 'watched' | 'watchlist' | 'settings'): void {
    this.activeTab = tab;

    // Load movies when switching to movie tabs
    if (tab === 'favorites') {
      this.loadFavoriteMovies();
    } else if (tab === 'watched') {
      this.loadWatchedMovies();
    } else if (tab === 'watchlist') {
      this.loadWatchlistMovies();
    }

    // Update statistics when switching to movie tabs to ensure they're current
    if (tab === 'favorites' || tab === 'watched' || tab === 'watchlist') {
      this.loadMovieStatistics();
    }

    // Initialize swipers when switching to movie tabs
    setTimeout(() => {
      if (tab === 'favorites') {
        this.initializeSwiper('fav-movies-swiper');
      } else if (tab === 'watched') {
        this.initializeSwiper('watched-swiper');
      } else if (tab === 'watchlist') {
        this.initializeSwiper('to-watch-swiper');
      }
    }, 100);
  }

  startEditingProfile(): void {
    if (this.currentUser) {
      this.editedProfile = {...this.currentUser};
      this.isEditingProfile = true;
      this.clearMessages();
    }
  }

  saveProfile(): void {
    console.log(this.editedProfile)
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

  private initializeSwiper(className: string): void {
    // Basic swiper functionality using native JavaScript
    const swiper = document.querySelector(`.${className}`);
    if (swiper) {
      const container = swiper.querySelector('.swiper-container') as HTMLElement;
      const prevBtn = swiper.querySelector('.swiper-btn-prev') as HTMLElement;
      const nextBtn = swiper.querySelector('.swiper-btn-next') as HTMLElement;

      if (container && prevBtn && nextBtn) {
        let currentPosition = 0;
        const cardWidth = 220; // card width + gap (200px card + 20px gap)
        const visibleCards = 4; // number of cards visible at once
        const maxPosition = Math.max(0, container.children.length - visibleCards);

        // Reset position when initializing
        container.style.transform = 'translateX(0px)';
        currentPosition = 0;

        // Update button states
        const updateButtonStates = () => {
          prevBtn.style.opacity = currentPosition > 0 ? '1' : '0.5';
          nextBtn.style.opacity = currentPosition < maxPosition ? '1' : '0.5';
        };

        // Initialize button states
        updateButtonStates();

        // Remove existing event listeners to prevent duplicates
        const newPrevBtn = prevBtn.cloneNode(true) as HTMLElement;
        const newNextBtn = nextBtn.cloneNode(true) as HTMLElement;
        prevBtn.parentNode?.replaceChild(newPrevBtn, prevBtn);
        nextBtn.parentNode?.replaceChild(newNextBtn, nextBtn);

        newPrevBtn.addEventListener('click', () => {
          if (currentPosition > 0) {
            currentPosition--;
            container.style.transform = `translateX(-${currentPosition * cardWidth}px)`;
            updateButtonStates();
          }
        });

        newNextBtn.addEventListener('click', () => {
          if (currentPosition < maxPosition) {
            currentPosition++;
            container.style.transform = `translateX(-${currentPosition * cardWidth}px)`;
            updateButtonStates();
          }
        });
      }
    }
  }

  getFormattedDate(dateString: string): string {
    const date = new Date(dateString);
    return date.toLocaleDateString('en-US', {
      year: 'numeric',
      month: 'long',
      day: 'numeric'
    });
  }

  getRatingStars(rating: number): string[] {
    const fullStars = Math.floor(rating);
    const hasHalfStar = rating % 1 !== 0;
    const stars: string[] = [];

    for (let i = 0; i < fullStars; i++) {
      stars.push('full');
    }

    if (hasHalfStar) {
      stars.push('half');
    }

    while (stars.length < 5) {
      stars.push('empty');
    }

    return stars;
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

  getProfileCompletionPercentage(): number {
    if (!this.currentUser) return 0;
    return this.userService.getProfileCompletionPercentage(this.currentUser);
  }

  // Movie loading methods
  loadFavoriteMovies(): void {
    this.loading = true;
    this.userMovieService.getUserFavorites().subscribe({
      next: (movies) => {
        this.favoriteMovies = movies;
        this.favoriteMoviesGrouped = this.groupMoviesByType(movies);
        this.loading = false;
      },
      error: (error) => {
        console.error('Failed to load favorite movies:', error);
        this.errorMessage = 'Failed to load favorite movies. Please try again.';
        this.loading = false;
      }
    });
  }

  loadWatchedMovies(): void {
    this.loading = true;
    this.userMovieService.getUserWatched().subscribe({
      next: (movies) => {
        this.watchedMovies = movies;
        this.watchedMoviesGrouped = this.groupMoviesByType(movies);
        this.loading = false;
      },
      error: (error) => {
        console.error('Failed to load watched movies:', error);
        this.errorMessage = 'Failed to load watched movies. Please try again.';
        this.loading = false;
      }
    });
  }

  loadWatchlistMovies(): void {
    this.loading = true;
    this.userMovieService.getUserWatchlist().subscribe({
      next: (movies) => {
        this.toWatchMovies = movies;
        this.toWatchMoviesGrouped = this.groupMoviesByType(movies);
        this.loading = false;
      },
      error: (error) => {
        console.error('Failed to load watchlist movies:', error);
        this.errorMessage = 'Failed to load watchlist movies. Please try again.';
        this.loading = false;
      }
    });
  }

  loadMovieStatistics(): void {
    this.userMovieService.getUserMovieStatistics().subscribe({
      next: (statistics) => {
        this.movieStatistics = statistics;
      },
      error: (error) => {
        console.error('Failed to load movie statistics:', error);
      }
    });
  }

  // Helper methods for MovieDto
  getMoviePosterUrl(posterPath: string): string {
    if (!posterPath) {
      return 'https://images.unsplash.com/photo-1489599732536-9a2b0caa395a?w=300&h=400&fit=crop';
    }
    return `https://image.tmdb.org/t/p/w500${posterPath}`;
  }

  getMovieYear(releaseDate: string): string {
    if (!releaseDate) return 'Unknown';
    return new Date(releaseDate).getFullYear().toString();
  }

  getMovieGenres(genres: string[]): string {
    if (!genres || genres.length === 0) return 'Unknown';
    return genres.slice(0, 2).join(', ');
  }

  // Helper method to group movies by type
  private groupMoviesByType(movies: MovieDto[]): { movies: MovieDto[], tvSeries: MovieDto[] } {
    const moviesList: MovieDto[] = [];
    const tvSeriesList: MovieDto[] = [];

    movies.forEach(movie => {
      if (movie.type === 'movie') {
        moviesList.push(movie);
      } else if (movie.type === 'tv') {
        tvSeriesList.push(movie);
      }
    });

    return {movies: moviesList, tvSeries: tvSeriesList};
  }
}
