import { Component, Input, OnInit, OnChanges, SimpleChanges } from '@angular/core';
import { NgFor, NgIf } from '@angular/common';

import { MovieCardComponent } from '../shared/movie-card/movie-card.component';
import {MovieDto, UserMovieService} from '../../../../../../core/services/user-movie.service';

@Component({
  selector: 'app-favorites',
  standalone: true,
  imports: [NgFor, NgIf, MovieCardComponent],
  templateUrl: './favorites.component.html',
  styleUrls: ['./favorites.component.scss']
})
export class FavoritesComponent implements OnInit, OnChanges {
  @Input() isActive = false;

  loading = false;
  errorMessage = '';

  favoriteMovies: MovieDto[] = [];
  favoriteMoviesGrouped: { movies: MovieDto[], tvSeries: MovieDto[] } = { movies: [], tvSeries: [] };

  constructor(private userMovieService: UserMovieService) {}

  ngOnInit(): void {
    if (this.isActive) {
      this.loadFavoriteMovies();
    }
  }

  ngOnChanges(changes: SimpleChanges): void {
    if (changes['isActive'] && this.isActive && this.favoriteMovies.length === 0) {
      this.loadFavoriteMovies();
    }
  }

  loadFavoriteMovies(): void {
    this.loading = true;
    this.errorMessage = '';

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
