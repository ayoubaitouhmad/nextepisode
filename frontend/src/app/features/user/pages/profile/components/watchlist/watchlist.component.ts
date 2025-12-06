import {Component, OnInit} from '@angular/core';
import {NgFor, NgIf} from '@angular/common';
import {GroupedMovies, groupMoviesByType, MovieDto, STATIC_WATCHLIST_MOVIES} from '../movie.model';
import {TvCardComponent} from '../shared/tv-card/tv-card.component';

@Component({
  selector: 'app-watchlist',
  standalone: true,
  imports: [NgFor, NgIf, TvCardComponent],
  templateUrl: './watchlist.component.html',
  styleUrls: ['./watchlist.component.scss']
})
export class WatchlistComponent implements OnInit {
  watchlistMovies: MovieDto[] = [];
  watchlistMoviesGrouped: GroupedMovies = {movies: [], tvSeries: []};
  loading = false;
  errorMessage = '';

  ngOnInit(): void {
    this.loadWatchlist();
  }

  loadWatchlist(): void {
    this.loading = true;
    setTimeout(() => {
      this.watchlistMovies = STATIC_WATCHLIST_MOVIES;
      this.watchlistMoviesGrouped = groupMoviesByType(STATIC_WATCHLIST_MOVIES);
      this.loading = false;
    }, 300);
  }

  hasContent(): boolean {
    return this.watchlistMoviesGrouped.movies.length > 0 ||
      this.watchlistMoviesGrouped.tvSeries.length > 0;
  }
}
