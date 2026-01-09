import {Component, inject, OnInit} from '@angular/core';
import {Router} from '@angular/router';
import {CommonModule} from '@angular/common';
import {UserMovieService} from '../../../../core/services/user/movie/user-movie.service';
import {AuthService} from '../../../../core/services/auth/auth-service';
import {ContentFilters} from '../../../../core/models/tmdb/request/content-filters';
import {MovieStatusRequest, XMovie} from '../../../../core/models/common/movie.model';
import {ContentFiltersComponent} from '../content-filters/content-filters.component';
import {Certification, GenreList, WatchProvider} from '../../../../core/models/common/shared-dtos';
import {_TmdbService} from '../../../../core/services/tmdb/_-tmdb.service';
import {MovieCardComponent} from '../../../../shared/components/movie-card/movie-card.component';

/**
 * Component responsible for displaying a grid of movies with filtering capabilities.
 * It handles loading movies from TMDB, managing filters, and user interactions like
 * adding to favorites, watched list, or watchlist.
 */
@Component({
  selector: 'app-movies',
  standalone: true,
  imports: [CommonModule, ContentFiltersComponent, MovieCardComponent],
  templateUrl: './movies.component.html',
  styleUrl: './movies.component.scss'
})
export class MoviesComponent implements OnInit {
  private tmdbService = inject(_TmdbService);
  private userMovieService = inject(UserMovieService);
  private _tmdbService: _TmdbService = inject(_TmdbService);
  private authService = inject(AuthService);
  private router = inject(Router);
  readonly contentType = 'movie' as const;

  // List of movies to display
  items: XMovie[] = [];
  // Loading state flag
  loading = false;
  // Error message if any
  error: string | null = null;
  // Pagination state
  currentPage = 1;
  totalPages = 1;
  totalResults = 0;
  // Current active filters
  currentFilters: ContentFilters | null = null;
  // Available certifications for filtering
  certifications: Certification[] = [];
  // Available streaming services for filtering
  streamingServices: WatchProvider[] = [];

  // List of movie genres
  genres: GenreList = {
    total: 0,
    stored_at: new Date(),
    genres: []
  };


  /**
   * Initializes the component by loading genres, initial movies, certifications, and watch providers.
   */
  ngOnInit(): void {
    this.loadGenres();
    this.loadCertification();
    this.loadWatchProviders();
    this.loadMovies();
  }

  /**
   * Loads the list of movie genres from TMDB.
   */
  private loadGenres() {
    console.info('[MoviesComponent] load movie genres');

    this._tmdbService.getMoviesGenres().subscribe({
      next: (genres) => {
        this.genres = genres;
      },
      error: (error) => {
        console.error('[MoviesComponent] Failed to load movie genres', error);
        this.error = 'Failed to load movies genres. Please try again.';
        this.loading = false;
      }
    });
  }

  /**
   * Loads movie certifications for a specific country.
   * @param countryCode The ISO 3166-1 alpha-2 country code (default: "US").
   */
  loadCertification(countryCode = "US") {
    console.info('[MoviesComponent] load certification by country');

    this.tmdbService.getMoviesCertificationByCountry(countryCode).subscribe({
      next: (certifications) => {
        this.certifications = certifications;
      },
      error: (error) => {
        console.error('[MoviesComponent] Failed to load movie certification', error);
        this.error = 'Failed to load movie certification. Please try again.';
      }
    })
  }

  /**
   * Loads available watch providers (streaming services) for a specific country.
   * @param coutryCode The ISO 3166-1 alpha-2 country code (default: "us").
   */
  loadWatchProviders(coutryCode = "us"): void {
    console.info('[MoviesComponent] load movies watch providers');

    this.tmdbService.getMovieWatchProvidersByCountry(coutryCode).subscribe({
      next: data => {
        this.streamingServices = data.results.slice(0, 20)
      },
      error: error => {
        console.error('[MoviesComponent] Failed to load movies watch providers', error);
        this.error = 'Failed load movies watch providers. Please try again.';
      },
    })
  }

  /**
   * Loads the initial list of movies (discover) sorted by popularity.
   */
  loadMovies(): void {
    console.info('[MoviesPage] Load movies');

    this.loading = true;
    this.error = null;
    this._tmdbService.discoverMovies({}).subscribe({
      next: (response) => {
        this.items = response.results;
        this.totalPages = response.total_pages;
        this.totalResults = response.total_results;
        this.loading = false;
      },
      error: (error) => {
        console.error('[MoviesComponent] Failed to Load movies', error);
        this.error = 'Failed to load movies. Please try again.';
        this.loading = false;
      }
    });
  }

  /**
   * Navigates to the details page of a specific movie.
   * @param item The movie object containing the ID.
   */
  onOpenDetails(item: any): void {
    this.router.navigate(['/movie', item.id]);
  }

  /**
   * Handles changes in content filters and reloads movies based on new filters.
   * @param filters The new set of filters to apply.
   */
  onFiltersChange(filters: ContentFilters): void {
    this.currentFilters = filters;
    this.items = [];
    this.loading = true;
    this.error = null;

    this._tmdbService.discoverMovies({...filters, page: this.currentPage}).subscribe({
      next: (response) => {
        console.log(response)
        this.items = response.results;
        this.totalPages = response.total_pages;
        this.totalResults = response.total_results;
        this.loading = false;
      },
      error: (error) => {
        console.error('Error loading movies:', error);
        this.error = 'Failed to load movies. Please try again.';
        this.loading = false;
      }
    });
  }

  /**
   * Loads the next page of results if available.
   */
  loadNextPage(): void {
    if (this.currentPage < this.totalPages) {
      this.currentPage++;
      this.loadMoviesWithCurrentFilters();
    }
  }

  /**
   * Loads the previous page of results if available.
   */
  loadPreviousPage(): void {
    if (this.currentPage > 1) {
      this.currentPage--;
      this.loadMoviesWithCurrentFilters();
    }
  }

  /**
   * Helper method to load movies using the current filters and page number.
   */
  private loadMoviesWithCurrentFilters(): void {
    if (this.currentFilters) {
      this.onFiltersChange(this.currentFilters);
    } else {
      this.loadMovies();
    }
  }

  /**
   * Shares the content (movie) using the Web Share API or copies the link to clipboard.
   * @param movie The movie to share.
   */
  onShareContent(movie: any): void {
    const shareUrl = `https://www.themoviedb.org/movie/${movie.id}`;
    if (navigator.share) {
      navigator.share({
        title: movie.title,
        text: `Check out ${movie.title} (${movie.year})`,
        url: shareUrl
      });
    } else {
      navigator.clipboard.writeText(shareUrl);
    }
  }

  /**
   * Adds a movie to the user's favorites list.
   * Requires authentication.
   * @param moviestatusrequest The movie content to add.
   */
  onAddToFavorites(moviestatusrequest: MovieStatusRequest): void {
    console.log(`[MoviesComponent] movie with id:${moviestatusrequest.movieId} has a ${moviestatusrequest.action} to favorite movies`);
  }

  /**
   * Adds a movie to the user's watched list.
   * Requires authentication.
   * @param content The movie content to add.
   */
  onAddToWatched(content: any): void {
    if (!this.authService.isAuthenticated()) {
      console.log('User not authenticated');
      return;
    }

    const tmdbId = parseInt(content.id);
    this.userMovieService.addToWatched(tmdbId).subscribe({
      next: () => console.log('Added to watched successfully'),
      error: (error) => console.error('Failed to add to watched:', error)
    });
  }

  /**
   * Adds a movie to the user's watchlist.
   * Requires authentication.
   * @param content The movie content to add.
   */
  onAddToWatchlist(content: any): void {
    if (!this.authService.isAuthenticated()) {
      console.log('User not authenticated');
      return;
    }


    const tmdbId = parseInt(content.id);
    this.userMovieService.addToWatchlist(tmdbId).subscribe({
      next: () => console.log('Added to watchlist successfully'),
      error: (error) => console.error('Failed to add to watchlist:', error)
    });
  }

  /**
   * Handles country change events to reload certifications and watch providers for the selected country.
   * @param countryCode The new country code.
   */
  onCountryChange(countryCode: string) {
    this.loadCertification(countryCode);
    this.loadWatchProviders(countryCode);
  }

}
