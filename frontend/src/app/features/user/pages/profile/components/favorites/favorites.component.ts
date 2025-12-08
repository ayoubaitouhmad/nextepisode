import {Component, OnInit} from '@angular/core';
import {NgFor, NgIf} from '@angular/common';
import {UserMovieList} from '../../../../../../core/models/movie/movie.model';
import {UserMovieService} from '../../../../../../core/services/user-movie.service';
import {TvCardComponent} from '../shared/tv-card/tv-card.component';
import {MovieCardComponent} from '../shared/movie-card/movie-card.component';
import {UserTvShowList} from '../../../../../../core/models/tv/tv.model';
import {UserTvService} from '../../../../../../core/services/user/tv/user-tv.service';

@Component({
  selector: 'app-favorites',
  standalone: true,
  imports: [NgFor, NgIf, TvCardComponent, MovieCardComponent],
  templateUrl: './favorites.component.html',
  styleUrls: ['./favorites.component.scss']
})
export class FavoritesComponent implements OnInit {
  favoriteMovies: UserMovieList = {page: 0, totalPages: 0, results: [], totalResults: 0};
  favoriteTvShows: UserTvShowList = {page: 0, totalPages: 0, results: [], totalResults: 0};

  loading = false;
  errorMessage = '';


  public constructor(
    private userMovieService: UserMovieService,
    private userTvService: UserTvService
  ) {
  }

  ngOnInit(): void {
    this.loadFavorites();
  }

  loadFavorites(): void {
    this.loading = true;
    setTimeout(() => {
      this.loadFavoriteMovies();
      this.loadFavoriteTvShows();
      this.loading = false;
    }, 300);
  }

  loadFavoriteMovies(): void {
    this.userMovieService.getUserFavoriteMovies().subscribe({
      next: (movies) => {
        this.favoriteMovies = movies;
      },
      error: err => console.log(err)
    });
  }

  loadFavoriteTvShows(): void {
    this.userTvService.getUserFavoriteTvShows().subscribe({
      next: (tvShows) => {
        this.favoriteTvShows = tvShows;
      },
      error: err => console.log(err)
    });
  }

  hasContent(): boolean {
    return this.favoriteMovies.results.length > 0 ||
      this.favoriteTvShows.results.length > 0;
  }
}
