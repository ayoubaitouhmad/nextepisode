import {Component, OnInit} from '@angular/core';
import {NgFor, NgIf} from '@angular/common';
import {
  MovieDto,
  GroupedMovies,
  STATIC_WATCHED_MOVIES,
  groupMoviesByType
} from '../movie.model';
import {TvCardComponent} from '../shared/tv-card/tv-card.component';

@Component({
  selector: 'app-watched',
  standalone: true,
  imports: [NgFor, NgIf, TvCardComponent],
  templateUrl: './watched.component.html',
  styleUrls: ['./watched.component.scss']
})
export class WatchedComponent implements OnInit {
  watchedMovies: MovieDto[] = [];
  watchedMoviesGrouped: GroupedMovies = {movies: [], tvSeries: []};
  loading = false;
  errorMessage = '';

  ngOnInit(): void {
    this.loadWatched();
  }

  loadWatched(): void {
    this.loading = true;
    setTimeout(() => {
      this.watchedMovies = STATIC_WATCHED_MOVIES;
      this.watchedMoviesGrouped = groupMoviesByType(STATIC_WATCHED_MOVIES);
      this.loading = false;
    }, 300);
  }

  hasContent(): boolean {
    return this.watchedMoviesGrouped.movies.length > 0 ||
      this.watchedMoviesGrouped.tvSeries.length > 0;
  }
}
