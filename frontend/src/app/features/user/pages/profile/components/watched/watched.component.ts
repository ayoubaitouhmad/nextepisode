import { Component, Input, OnInit, OnChanges, SimpleChanges } from '@angular/core';
import { NgFor, NgIf } from '@angular/common';
import { MovieDto, UserMovieService } from '../../../../../core/services/user-movie.service';
import { MovieCardComponent } from '../shared/movie-card/movie-card.component';

@Component({
  selector: 'app-watched',
  standalone: true,
  imports: [NgFor, NgIf, MovieCardComponent],
  templateUrl: './watched.component.html',
  styleUrls: ['./watched.component.scss']
})
export class WatchedComponent implements OnInit, OnChanges {
  @Input() isActive = false;

  loading = false;
  errorMessage = '';

  watchedMovies: MovieDto[] = [];
  watchedMoviesGrouped: { movies: MovieDto[], tvSeries: MovieDto[] } = { movies: [], tvSeries: [] };

  constructor(private userMovieService: UserMovieService) {}

  ngOnInit(): void {
    if (this.isActive) {
      this.loadWatchedMovies();
    }
  }

  ngOnChanges(changes: SimpleChanges): void {
    if (changes['isActive'] && this.isActive && this.watchedMovies.length === 0) {
      this.loadWatchedMovies();
    }
  }

  loadWatchedMovies(): void {
    this.loading = true;
    this.errorMessage = '';

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

    return { movies: moviesList, tvSeries: tvSeriesList };
  }
}
