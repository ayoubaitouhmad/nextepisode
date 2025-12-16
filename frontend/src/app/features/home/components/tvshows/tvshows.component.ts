import {Component, EventEmitter, inject, OnInit, Output} from '@angular/core';
import {CommonModule} from '@angular/common';
import {ContentFiltersComponent} from '../../components/content-filters/content-filters.component';
import {ContentGridComponent} from '../../components/content-grid/content-grid.component';
import {ContentFilters} from '../../../../core/models/tmdb/request/content-filters';
import {TvSeriesService} from '../../../../core/services/tmdb/tv-series.service';
import {UserTvService} from '../../../../core/services/user/tv/user-tv.service';
import {AuthService} from '../../../../core/services/auth/auth-service';
import {TvSeries} from '../../../../core/models/common/tv.model';
import {XMovie} from '../../../../core/models/common/movie.model';

@Component({
  selector: 'app-tvshows',
  standalone: true,
  imports: [CommonModule, ContentFiltersComponent, ContentGridComponent],
  templateUrl: './tvshows.component.html',
  styleUrl: './tvshows.component.scss'
})
export class TvShowsComponent implements OnInit {
  private tvSeriesService = inject(TvSeriesService);
  private userTvService = inject(UserTvService);
  private authService = inject(AuthService);

  @Output() itemClick = new EventEmitter<TvSeries>();

  items: TvSeries[] = [];
  loading = false;
  error: string | null = null;
  currentPage = 1;
  totalPages = 1;
  totalResults = 0;
  currentFilters: ContentFilters | null = null;

  readonly contentType = 'tv' as const;

  ngOnInit(): void {
    this.loadTvShows();
  }

  loadTvShows(): void {
    this.loading = true;
    this.error = null;

    this.tvSeriesService.discoverSeries({
      sortBy: 'popularity.desc',
      page: this.currentPage
    }).subscribe({
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
  }

  onFiltersChange(filters: ContentFilters): void {
    this.currentFilters = filters;
    this.currentPage = 1; // Reset to first page on filter change
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
  }

  loadNextPage(): void {
    if (this.currentPage < this.totalPages) {
      this.currentPage++;
      this.loadTvShowsWithCurrentFilters();
    }
  }

  loadPreviousPage(): void {
    if (this.currentPage > 1) {
      this.currentPage--;
      this.loadTvShowsWithCurrentFilters();
    }
  }

  private loadTvShowsWithCurrentFilters(): void {
    if (this.currentFilters) {
      this.onFiltersChange(this.currentFilters);
    } else {
      this.loadTvShows();
    }
  }

  private getSortBy(lookFor: string): 'first_air_date.desc' | 'popularity.desc' | 'vote_average.desc' {
    switch (lookFor) {
      case 'High Rated':
      case 'Mieux noté':
        return 'vote_average.desc';
      case 'Most Popular':
      case 'Populaire':
        return 'popularity.desc';
      case 'Newest':
      default:
        return 'first_air_date.desc';
    }
  }

  onContentClick(item: XMovie | TvSeries): void {
    this.itemClick.emit(item as TvSeries);
  }

  onShareContent(item: XMovie | TvSeries): void {
    const shareUrl = `https://www.themoviedb.org/tv/${item.id}`;
    if (navigator.share) {
      navigator.share({
        title: item.title,
        text: `Check out ${item.title} (${item.year})`,
        url: shareUrl
      });
    } else {
      navigator.clipboard.writeText(shareUrl);
    }
  }

  onAddToFavorites(item: XMovie | TvSeries): void {
    if (!this.authService.isAuthenticated()) {
      console.log('User not authenticated');
      return;
    }

    const tmdbId = parseInt(item.id);
    // this.userTvService.addToFavorites(tmdbId).subscribe({
    //   next: () => console.log('Added to favorites successfully'),
    //   error: (error) => console.error('Failed to add to favorites:', error)
    // });
  }

  onAddToWatched(item: XMovie | TvSeries): void {
    if (!this.authService.isAuthenticated()) {
      console.log('User not authenticated');
      return;
    }

    const tmdbId = parseInt(item.id);
    // this.userTvService.addToWatched(tmdbId).subscribe({
    //   next: () => console.log('Added to watched successfully'),
    //   error: (error) => console.error('Failed to add to watched:', error)
    // });
  }

  onAddToWatchlist(item: XMovie | TvSeries): void {
    if (!this.authService.isAuthenticated()) {
      console.log('User not authenticated');
      return;
    }

    const tmdbId = parseInt(item.id);
    // this.userTvService.addToWatchlist(tmdbId).subscribe({
    //   next: () => console.log('Added to watchlist successfully'),
    //   error: (error) => console.error('Failed to add to watchlist:', error)
    // });
  }
}
