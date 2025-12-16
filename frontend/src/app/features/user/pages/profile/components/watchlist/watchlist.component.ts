import {Component, OnInit} from '@angular/core';
import {NgFor, NgIf} from '@angular/common';
import {TvCardComponent} from '../shared/tv-card/tv-card.component';
import {UserMovieList} from '../../../../../../core/models/common/movie.model';
import {UserTvShowList} from '../../../../../../core/models/common/tv.model';
import {MovieCardComponent} from '../shared/movie-card/movie-card.component';
import {UserMovieService} from '../../../../../../core/services/user/movie/user-movie.service';
import {UserTvService} from '../../../../../../core/services/user/tv/user-tv.service';
import {ListPaginationComponent} from '../shared/list-pagination/list-pagination.component';

@Component({
  selector: 'app-watchlist',
  standalone: true,
  imports: [NgFor, NgIf, TvCardComponent, MovieCardComponent, ListPaginationComponent],
  templateUrl: './watchlist.component.html',
  styleUrls: ['./watchlist.component.scss']
})
export class WatchlistComponent implements OnInit {
  watchListMovies: UserMovieList = {page: 0, total_pages: 0, results: [], total_results: 0};
  watchListTvShows: UserTvShowList = {page: 0, total_pages: 0, results: [], total_results: 0};

  watchListMoviesToShow: UserMovieList = {page: 0, total_pages: 0, results: [], total_results: 0};
  watchListTvShowsToShow: UserTvShowList = {page: 0, total_pages: 0, results: [], total_results: 0};

  loading = false;
  errorMessage = '';

  moviesDisplayCount: number = 5;
  tvShowsDisplayCount: number = 5;

  public constructor(
    private userMovieService: UserMovieService,
    private userTvService: UserTvService
  ) {
  }



  ngOnInit(): void {
    this.loadWatchlist();
  }

  loadWatchlist(): void {
    this.loading = true;
    setTimeout(() => {
      this.loadInWatchListMovies();
      this.loadInWatchListTvShows();
      this.loading = false;
    }, 300);
  }

  loadInWatchListMovies(): void {
    this.userMovieService.getUserInWatchListMovies().subscribe({
      next: (movies) => {
        this.watchListMovies = movies;
        this.watchListMoviesToShow = {
          page: this.watchListMovies.page,
          total_pages: this.watchListMovies.total_pages,
          results: this.watchListMovies.results.slice(0, this.moviesDisplayCount),
          total_results: this.watchListMovies.total_results
        };
      },
      error: err => console.log(err)
    });
  }

  loadInWatchListTvShows(): void {
    this.userTvService.getUserInWatchListTvShows().subscribe({
      next: (tvShows) => {
        this.watchListTvShows = tvShows;
        this.watchListTvShowsToShow = {
          page: this.watchListTvShows.page,
          total_pages: this.watchListTvShows.total_pages,
          results: this.watchListTvShows.results.slice(0, this.tvShowsDisplayCount),
          total_results: this.watchListTvShows.total_results
        };
      },
      error: err => console.log(err)
    });
  }

  hasContent(): boolean {
    return this.watchListMovies.results.length > 0 ||
      this.watchListTvShows.results.length > 0;
  }

  showMoreMovies(): void {
    if (this.watchListMoviesToShow.results.length >= this.watchListMovies.results.length) return;
    this.watchListMoviesToShow.results = this.watchListMovies.results.slice(0, this.watchListMoviesToShow.results.length + this.moviesDisplayCount);
  }

  showLessMovies(): void {
    if (this.watchListMoviesToShow.results.length <= this.moviesDisplayCount) return;
    this.watchListMoviesToShow.results = this.watchListMovies.results.slice(0, this.watchListMoviesToShow.results.length - this.moviesDisplayCount);
  }

  showMoreTvShows(): void {
    if (this.watchListTvShowsToShow.results.length >= this.watchListTvShows.results.length) return;
    this.watchListTvShowsToShow.results = this.watchListTvShows.results.slice(0, this.watchListTvShowsToShow.results.length + this.tvShowsDisplayCount);
  }

  showLessTvShows(): void {
    if (this.watchListTvShowsToShow.results.length <= this.tvShowsDisplayCount) return;
    this.watchListTvShowsToShow.results = this.watchListTvShows.results.slice(0, this.watchListTvShowsToShow.results.length - this.tvShowsDisplayCount);
  }
}
