import {Component, OnInit} from '@angular/core';
import {NgFor, NgIf} from '@angular/common';
import {GroupedMovies, groupMoviesByType, STATIC_FAVORITE_MOVIES} from '../movie.model';
import {UserMovieList} from '../../../../../../core/models/movie/movie.model';
import {UserMovieService} from '../../../../../../core/services/user-movie.service';
import {TvCardComponent} from '../shared/tv-card/tv-card.component';
import {MovieCardComponent} from '../shared/movie-card/movie-card.component';

@Component({
  selector: 'app-favorites',
  standalone: true,
  imports: [NgFor, NgIf, TvCardComponent, MovieCardComponent],
  templateUrl: './favorites.component.html',
  styleUrls: ['./favorites.component.scss']
})
export class FavoritesComponent implements OnInit {
  favoriteMovies: UserMovieList = {page: 0, totalPages: 0, results: [], totalResults: 0};
  favoriteMoviesGrouped: GroupedMovies = {movies: [], tvSeries: []};
  loading = false;
  errorMessage = '';


  public constructor(private userMovieService: UserMovieService) {
  }

  ngOnInit(): void {
    this.loadFavorites();
  }

  loadFavorites(): void {
    this.loading = true;
    setTimeout(() => {
      this.userMovieService.getUserFavoriteMovies().subscribe({
        next: (movies) => {
          this.favoriteMovies = movies;
        },
        error: err => console.log(err)
      });
      this.favoriteMoviesGrouped = groupMoviesByType(STATIC_FAVORITE_MOVIES);
      this.loading = false;
    }, 300);
  }

  hasContent(): boolean {
    return this.favoriteMoviesGrouped.movies.length > 0 ||
      this.favoriteMoviesGrouped.tvSeries.length > 0;
  }
}
