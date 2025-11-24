import {Component, OnInit, inject} from '@angular/core';
import {Router} from '@angular/router';
import {CommonModule} from '@angular/common';
import {ContentFiltersComponent} from '../../components/movie-filters/content-filters.component';
import {ContentGridComponent} from '../../components/movie-grid/content-grid.component';
import {TMDBService} from '../../../../core/services/tmdb.service';
import {Movie} from '../../../../core/models/movie.model';
import {LoginComponent} from '../../../auth/pages/login/login.component';
import {forkJoin, switchMap} from 'rxjs';
import {ContentFilters} from '../../../../core/models/content-filters';
import {TvSeriesService} from '../../../../core/services/tv-series.service';
import {TvSeries} from '../../../../core/models/TMDTvSeries';
import {MovieService} from '../../../../core/services/movie.service';
import {UserMovieService} from '../../../../core/services/user-movie.service';
import {AuthService} from '../../../../core/services/auth-service';

@Component({
  selector: 'app-movie-list',
  standalone: true,
  imports: [CommonModule, ContentFiltersComponent, ContentGridComponent],
  templateUrl: './content-list.component.html',
  styleUrl: './content-list.component.scss'
})
export class ContentListComponent implements OnInit {

  private movieService = inject(MovieService);
  private tvSeriesService = inject(TvSeriesService);
  private userMovieService = inject(UserMovieService);
  private authService = inject(AuthService);
  private router = inject(Router);

  items: (Movie | TvSeries)[] = [];
  loading = false;
  error: string | null = null;
  currentPage = 1;
  totalPages = 1;
  totalResults = 0;
  currentFilters: ContentFilters | null = null;

  ngOnInit(): void {
    this.loadMovies();
    this.testAuthentication();
  }

  onOpenDetails(item: Movie | TvSeries): void {
    const id = 'id' in item ? item.id : '';
    // Heuristic: TvSeries has originCountry property in our model; Movie does not
    const isTv = (item as any).originCountry !== undefined;
    this.router.navigate([isTv ? '/tv' : '/movie', id]);
  }

  testAuthentication(): void {
    if (this.authService.isAuthenticated()) {
      console.log('User is authenticated, testing API call...');
      this.userMovieService.testAuthentication().subscribe({
        next: (response) => {
          console.log('Authentication test successful:', response);
        },
        error: (error) => {
          console.error('Authentication test failed:', error);
          if (error.status === 401) {
            console.log('User needs to log in again - JWT token may be invalid');
            // Clear the invalid token
            this.authService.logout();
          }
        }
      });
    } else {
      console.log('User is not authenticated');
    }
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
    console.log(filters)
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

    if (filters.type === 'tv') {
      this.tvSeriesService.discoverSeries(filterParams).subscribe({
        next: (response) => {
          this.items = response.series;
          this.totalPages = response.totalPages;
          this.totalResults = response.totalResults;
          this.loading = false;
        },
        error: (error) => {
          console.error('Error loading TV series:', error);
          this.error = 'Failed to load TV series. Please try again.';
          this.loading = false;
        }
      });
    } else {
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
    console.log(this.currentFilters)
    if (this.currentFilters) {
      this.onFiltersChange(this.currentFilters);
    } else {
      this.loadMovies();
    }
  }

  private getSortBy(lookFor: string): 'release_date.desc' | 'popularity.desc' | 'vote_average.desc' {
    switch (lookFor) {
      case 'High Rated':
        return 'vote_average.desc';
      case 'Most Popular':
        return 'popularity.desc';
      case 'Newest':
      default:
        return 'release_date.desc';
    }
  }

  onShareContent(movie: Movie): void {
    // Implement share functionality
    const shareUrl = `https://www.themoviedb.org/movie/${movie.id}`;
    if (navigator.share) {
      navigator.share({
        title: movie.title,
        text: `Check out ${movie.title} (${movie.year})`,
        url: shareUrl
      });
    } else {
      // Fallback: copy to clipboard
      navigator.clipboard.writeText(shareUrl);
    }
  }

  onAddToFavorites(content: Movie | TvSeries): void {
    if (!this.authService.isAuthenticated()) {
      console.log('User not authenticated');
      return;
    }

    if ('id' in content) {
      const tmdbId = parseInt(content.id);
      this.userMovieService.addToFavorites(tmdbId).subscribe({
        next: () => {
          console.log('Added to favorites successfully');
        },
        error: (error) => {
          console.error('Failed to add to favorites:', error);
        }
      });
    }
  }

  onAddToWatched(content: Movie | TvSeries): void {
    if (!this.authService.isAuthenticated()) {
      console.log('User not authenticated');
      return;
    }

    if ('id' in content) {
      const tmdbId = parseInt(content.id);
      this.userMovieService.addToWatched(tmdbId).subscribe({
        next: () => {
          console.log('Added to watched successfully');
        },
        error: (error) => {
          console.error('Failed to add to watched:', error);
        }
      });
    }
  }

  onAddToWatchlist(content: Movie | TvSeries): void {
    if (!this.authService.isAuthenticated()) {
      console.log('User not authenticated');
      return;
    }

    if ('id' in content) {
      const tmdbId = parseInt(content.id);
      this.userMovieService.addToWatchlist(tmdbId).subscribe({
        next: () => {
          console.log('Added to watchlist successfully');
        },
        error: (error) => {
          console.error('Failed to add to watchlist:', error);
        }
      });
    }
  }
}
