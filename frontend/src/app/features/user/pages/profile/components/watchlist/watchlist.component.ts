import { Component, Input, OnInit, OnChanges, SimpleChanges } from '@angular/core';
import { NgFor, NgIf } from '@angular/common';
import { MovieDto, UserMovieService } from '../../../../../core/services/user-movie.service';
import { MovieCardComponent } from '../shared/movie-card/movie-card.component';

@Component({
  selector: 'app-watchlist',
  standalone: true,
  imports: [NgFor, NgIf, MovieCardComponent],
  templateUrl: './watchlist.component.html',
  styleUrls: ['./watchlist.component.scss']
})
export class WatchlistComponent implements OnInit, OnChanges {
  @Input() isActive = false;

  loading = false;
  errorMessage = '';

  toWatchMovies: MovieDto[] = [];
  toWatchMoviesGrouped: { movies: MovieDto[], tvSeries: MovieDto[] } = { movies: [], tvSeries: [] };

  constructor(private userMovieService: UserMovieService) {}

  ngOnInit(): void {
    if (this.isActive) {
      this.loadWatchlistMovies();
    }
  }

  ngOnChanges(changes: SimpleChanges): void {
    if (changes['isActive'] && this.isActive && this.toWatchMovies.length === 0) {
      this.loadWatchlistMovies();
    }
  }

  loadWatchlistMovies(): void {
    this.loading = true;
    this.errorMessage = '';

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
