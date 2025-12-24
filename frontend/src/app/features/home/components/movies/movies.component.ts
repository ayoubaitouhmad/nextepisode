import {Component, inject, OnInit} from '@angular/core';
import {Router} from '@angular/router';
import {CommonModule} from '@angular/common';
import {UserMovieService} from '../../../../core/services/user/movie/user-movie.service';
import {AuthService} from '../../../../core/services/auth/auth-service';
import {ContentGridComponent} from '../content-grid/content-grid.component';
import {ContentFilters} from '../../../../core/models/tmdb/request/content-filters';
import {XMovie} from '../../../../core/models/common/movie.model';
import {ContentFiltersComponent} from '../content-filters/content-filters.component';
import {Certification, GenreList} from '../../../../core/models/common/shared-dtos';
import {_TmdbService} from '../../../../core/services/tmdb/_-tmdb.service';
import {MovieService} from '../../../../core/services/tmdb/movie.service';
import {AlertService} from '../../../../shared/components/alert/alert.service';


@Component({
  selector: 'app-movies',
  standalone: true,
  imports: [CommonModule, ContentFiltersComponent, ContentGridComponent],
  templateUrl: './movies.component.html',
  styleUrl: './movies.component.scss'
})
export class MoviesComponent implements OnInit {
  private tmdbService = inject(_TmdbService);
  private movieService = inject(MovieService);
  private userMovieService = inject(UserMovieService);
  private _tmdbService: _TmdbService = inject(_TmdbService);
  private authService = inject(AuthService);
  private router = inject(Router);


  constructor(private alertService: AlertService) {
  }


  items: XMovie[] = [];
  loading = false;
  error: string | null = null;
  currentPage = 1;
  totalPages = 1;
  totalResults = 0;
  currentFilters: ContentFilters | null = null;
  certifications: Certification[] = [];


  genres: GenreList = {
    total: 0,
    stored_at: new Date(),
    genres: []
  };

  readonly contentType = 'movie' as const;


  ngOnInit(): void {
    this.loadGenres();
    this.loadMovies();
    this.loadCertification();
  }


  loadCertification(countryCode = "US") {
    this.tmdbService.getMoviesCertificationByCountry(countryCode).subscribe({
      next: (certifications) => {
        this.certifications = certifications;
      },
      error: (error) => {
        console.log(error);
        this.alertService.warning(`No certification found the country with the code:${countryCode}, the default certification will be used.`, {
          title: "No certification found"
        });
      }
    })
  }

  private loadGenres() {
    this._tmdbService.getMoviesGenres().subscribe({
      next: (genres) => {
        this.genres = genres;
      },
      error: (error) => console.log(error)
    });
  }


  onOpenDetails(item: XMovie): void {
    this.router.navigate(['/movie', item.id]);
  }

  loadMovies(): void {
    this.loading = true;
    this.error = null;

    this.movieService.discoverMovies({
      sortBy: 'popularity.desc',
      page: this.currentPage
    }).subscribe({
      next: (response) => {
        this.items = response.movies;
        this.totalPages = response.totalPages;
        this.totalResults = response.totalResults;
        this.loading = false;
      },
      error: (error) => {
        console.error('Error loading movies:', error);
        this.error = 'Failed to load movies. Please try again.';
        this.loading = false;
      }
    });
  }

  onFiltersChange(filters: ContentFilters): void {
    this.currentFilters = filters;
    this.currentPage = 1;
    this.loading = true;
    this.error = null;

    const filterParams = {
      yearFrom: filters.yearFrom,
      yearTo: filters.yearTo,
      genres: filters.genres,
      sortBy: this.getSortBy(filters.lookFor),
      page: this.currentPage,
      language: filters.language === 'Any' ? undefined : filters.language,
      with_watch_providers: filters.streamingServices,
      watch_region: filters.country
    };

    this.movieService.discoverMovies(filterParams).subscribe({
      next: (response) => {
        this.items = response.movies;
        this.totalPages = response.totalPages;
        this.totalResults = response.totalResults;
        this.loading = false;
      },
      error: (error) => {
        console.error('Error loading movies:', error);
        this.error = 'Failed to load movies. Please try again.';
        this.loading = false;
      }
    });
  }

  loadNextPage(): void {
    if (this.currentPage < this.totalPages) {
      this.currentPage++;
      this.loadMoviesWithCurrentFilters();
    }
  }

  loadPreviousPage(): void {
    if (this.currentPage > 1) {
      this.currentPage--;
      this.loadMoviesWithCurrentFilters();
    }
  }

  private loadMoviesWithCurrentFilters(): void {
    if (this.currentFilters) {
      this.onFiltersChange(this.currentFilters);
    } else {
      this.loadMovies();
    }
  }

  private getSortBy(lookFor: string): 'release_date.desc' | 'popularity.desc' | 'vote_average.desc' {
    switch (lookFor) {
      case 'High Rated':
      case 'Mieux noté':
        return 'vote_average.desc';
      case 'Most Popular':
      case 'Populaire':
        return 'popularity.desc';
      case 'Newest':
      default:
        return 'release_date.desc';
    }
  }

  onShareContent(movie: XMovie): void {
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

  onAddToFavorites(content: XMovie): void {
    if (!this.authService.isAuthenticated()) {
      console.log('User not authenticated');
      return;
    }

    const tmdbId = parseInt(content.id);
    this.userMovieService.addToFavorites(tmdbId).subscribe({
      next: () => console.log('Added to favorites successfully'),
      error: (error) => console.error('Failed to add to favorites:', error)
    });
  }

  onAddToWatched(content: XMovie): void {
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

  onAddToWatchlist(content: XMovie): void {
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

  onCountryChange(countryCode: string) {
    this.loadCertification(countryCode);
  }
}
