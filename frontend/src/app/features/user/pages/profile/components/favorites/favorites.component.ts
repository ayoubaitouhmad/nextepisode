import { Component, OnInit } from '@angular/core';
import { NgFor, NgIf } from '@angular/common';
import { MovieCardComponent } from '../shared/movie-card/movie-card.component';
import {
  MovieDto,
  GroupedMovies,
  STATIC_FAVORITE_MOVIES,
  groupMoviesByType
} from '../movie.model';

@Component({
  selector: 'app-favorites',
  standalone: true,
  imports: [NgFor, NgIf, MovieCardComponent],
  templateUrl: './favorites.component.html',
  styleUrls: ['./favorites.component.scss']
})
export class FavoritesComponent implements OnInit {
  favoriteMovies: MovieDto[] = [];
  favoriteMoviesGrouped: GroupedMovies = { movies: [], tvSeries: [] };
  loading = false;
  errorMessage = '';

  ngOnInit(): void {
    this.loadFavorites();
  }

  loadFavorites(): void {
    this.loading = true;
    setTimeout(() => {
      this.favoriteMovies = STATIC_FAVORITE_MOVIES;
      this.favoriteMoviesGrouped = groupMoviesByType(STATIC_FAVORITE_MOVIES);
      this.loading = false;
    }, 300);
  }

  hasContent(): boolean {
    return this.favoriteMoviesGrouped.movies.length > 0 ||
      this.favoriteMoviesGrouped.tvSeries.length > 0;
  }
}
