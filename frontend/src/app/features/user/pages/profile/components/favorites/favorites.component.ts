import {Component, OnInit} from '@angular/core';
import {NgFor, NgIf} from '@angular/common';
import {UserMovieList} from '../../../../../../core/models/common/movie.model';
import {UserMovieService} from '../../../../../../core/services/user/movie/user-movie.service';
import {TvCardComponent} from '../shared/tv-card/tv-card.component';
import {MovieCardComponent} from '../shared/movie-card/movie-card.component';
import {UserTvShowList} from '../../../../../../core/models/common/tv.model';
import {UserTvService} from '../../../../../../core/services/user/tv/user-tv.service';
import {ListPaginationComponent} from '../shared/list-pagination/list-pagination.component';

@Component({
  selector: 'app-favorites',
  standalone: true,
  imports: [NgFor, NgIf, TvCardComponent, MovieCardComponent, ListPaginationComponent],
  templateUrl: './favorites.component.html',
  styleUrls: ['./favorites.component.scss']
})
export class FavoritesComponent implements OnInit {
  favoriteMovies: UserMovieList = {page: 0, total_pages: 0, results: [], total_results: 0};
  favoriteTvShows: UserTvShowList = {page: 0, total_pages: 0, results: [], total_results: 0};

  favoriteTvShowsToShow: UserTvShowList = {page: 0, total_pages: 0, results: [], total_results: 0};
  favoriteMoviesToShow: UserMovieList = {page: 0, total_pages: 0, results: [], total_results: 0};

  loading = false;


  errorMessage = '';

  moviesDisplayCount: number = 5;
  tvShowsDisplayCount: number = 5;


  public constructor(
    private userMovieService: UserMovieService,
    private userTvService: UserTvService
  ) {
    this.loadFavorites();
  }

  ngOnInit(): void {

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
        this.favoriteMoviesToShow = {
          page: this.favoriteMovies.page,
          total_pages: this.favoriteMovies.total_pages,
          results: this.favoriteMovies.results.slice(0, this.moviesDisplayCount),
          total_results: this.favoriteMovies.total_results
        };
      },
      error: err => console.log(err)
    });
  }

  loadFavoriteTvShows(): void {
    this.userTvService.getUserFavoriteTvShows().subscribe({
      next: (tvShows) => {
        this.favoriteTvShows = tvShows;
        this.favoriteTvShowsToShow = {
          page: this.favoriteTvShows.page,
          total_pages: this.favoriteTvShows.total_pages,
          results: this.favoriteTvShows.results.slice(0, this.tvShowsDisplayCount),
          total_results: this.favoriteTvShows.total_results
        };
      },
      error: err => console.log(err)
    });
  }

  hasContent(): boolean {
    return this.favoriteMovies.results.length > 0 ||
      this.favoriteTvShows.results.length > 0;
  }

  showMoreTvShows(): void {
    if (this.favoriteTvShowsToShow.results.length >= this.favoriteTvShows.results.length) return;
    this.favoriteTvShowsToShow.results = this.favoriteTvShows.results.slice(0, this.favoriteTvShowsToShow.results.length + this.tvShowsDisplayCount);
  }

  showLessTvShows(): void {
    if (this.favoriteTvShowsToShow.results.length <= this.tvShowsDisplayCount) return;
    this.favoriteTvShowsToShow.results = this.favoriteTvShows.results.slice(0, this.favoriteTvShowsToShow.results.length - this.tvShowsDisplayCount);
  }

  showMoreMovies(): void {
    if (this.favoriteMoviesToShow.results.length >= this.favoriteMovies.results.length) return;
    this.favoriteMoviesToShow.results = this.favoriteMovies.results.slice(0, this.favoriteMoviesToShow.results.length + this.moviesDisplayCount);
  }

  showLessMovies(): void {
    if (this.favoriteMoviesToShow.results.length <= this.moviesDisplayCount) return;
    this.favoriteMoviesToShow.results = this.favoriteMovies.results.slice(0, this.favoriteMoviesToShow.results.length - this.moviesDisplayCount);
  }
}
