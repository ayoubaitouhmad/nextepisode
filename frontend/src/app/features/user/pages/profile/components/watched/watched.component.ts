import {Component, OnInit} from '@angular/core';
import {NgFor, NgIf} from '@angular/common';
import {TvCardComponent} from '../shared/tv-card/tv-card.component';
import {UserMovieList} from '../../../../../../core/models/user/movie/movie.model';
import {UserTvShowList} from '../../../../../../core/models/user/tv/tv.model';
import {MovieCardComponent} from '../shared/movie-card/movie-card.component';
import {UserMovieService} from '../../../../../../core/services/user/movie/user-movie.service';
import {UserTvService} from '../../../../../../core/services/user/tv/user-tv.service';
import {ListPaginationComponent} from '../shared/list-pagination/list-pagination.component';

@Component({
  selector: 'app-watched',
  standalone: true,
  imports: [NgFor, NgIf, TvCardComponent, MovieCardComponent, ListPaginationComponent],
  templateUrl: './watched.component.html',
  styleUrls: ['./watched.component.scss']
})
export class WatchedComponent implements OnInit {
  watchedMovies: UserMovieList = {page: 0, total_pages: 0, results: [], total_results: 0};
  watchedTvShows: UserTvShowList = {page: 0, total_pages: 0, results: [], total_results: 0};

  watchedMoviesToShow: UserMovieList = {page: 0, total_pages: 0, results: [], total_results: 0};
  watchedTvShowsToShow: UserTvShowList = {page: 0, total_pages: 0, results: [], total_results: 0};

  loading: boolean = false;
  errorMessage: string = '';

  moviesDisplayCount: number = 5;
  tvShowsDisplayCount: number = 5;

  public constructor(
    private userMovieService: UserMovieService,
    private userTvService: UserTvService
  ) {
  }


  ngOnInit(): void {
    this.loadWatched();
  }

  loadWatched(): void {
    this.loading = true;
    setTimeout(() => {
      this.loadWatchedTvShows();
      this.loadWatchedMovies();

      this.loading = false;
    }, 300);
  }

  loadWatchedMovies(): void {
    this.userMovieService.getUserWatchedMovies().subscribe({
      next: (movies) => {
        this.watchedMovies = movies;
        this.watchedMoviesToShow = {
          page: this.watchedMovies.page,
          total_pages: this.watchedMovies.total_pages,
          results: this.watchedMovies.results.slice(0, this.moviesDisplayCount),
          total_results: this.watchedMovies.total_results
        };
      },
      error: err => console.log(err)
    });
  }

  loadWatchedTvShows(): void {
    this.userTvService.getUserWatchedTvShows().subscribe({
      next: (tvShows) => {
        this.watchedTvShows = tvShows;
        this.watchedTvShowsToShow = {
          page: this.watchedTvShows.page,
          total_pages: this.watchedTvShows.total_pages,
          results: this.watchedTvShows.results.slice(0, this.tvShowsDisplayCount),
          total_results: this.watchedTvShows.total_results
        };
      },
      error: err => console.log(err)
    });
  }


  hasContent(): boolean {
    return this.watchedMovies.results.length > 0 ||
      this.watchedTvShows.results.length > 0;
  }

  showMoreMovies(): void {
    if (this.watchedMoviesToShow.results.length >= this.watchedMovies.results.length) return;
    this.watchedMoviesToShow.results = this.watchedMovies.results.slice(0, this.watchedMoviesToShow.results.length + this.moviesDisplayCount);
  }

  showLessMovies(): void {
    if (this.watchedMoviesToShow.results.length <= this.moviesDisplayCount) return;
    this.watchedMoviesToShow.results = this.watchedMovies.results.slice(0, this.watchedMoviesToShow.results.length - this.moviesDisplayCount);
  }

  showMoreTvShows(): void {
    if (this.watchedTvShowsToShow.results.length >= this.watchedTvShows.results.length) return;
    this.watchedTvShowsToShow.results = this.watchedTvShows.results.slice(0, this.watchedTvShowsToShow.results.length + this.tvShowsDisplayCount);
  }

  showLessTvShows(): void {
    if (this.watchedTvShowsToShow.results.length <= this.tvShowsDisplayCount) return;
    this.watchedTvShowsToShow.results = this.watchedTvShows.results.slice(0, this.watchedTvShowsToShow.results.length - this.tvShowsDisplayCount);
  }
}
