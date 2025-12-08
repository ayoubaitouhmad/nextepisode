import {Component, OnInit} from '@angular/core';
import {NgFor, NgIf} from '@angular/common';
import {TvCardComponent} from '../shared/tv-card/tv-card.component';
import {UserMovieList} from '../../../../../../core/models/user/movie/movie.model';
import {UserTvShowList} from '../../../../../../core/models/user/tv/tv.model';
import {MovieCardComponent} from '../shared/movie-card/movie-card.component';
import {UserMovieService} from '../../../../../../core/services/user/movie/user-movie.service';
import {UserTvService} from '../../../../../../core/services/user/tv/user-tv.service';

@Component({
  selector: 'app-watchlist',
  standalone: true,
  imports: [NgFor, NgIf, TvCardComponent, MovieCardComponent],
  templateUrl: './watchlist.component.html',
  styleUrls: ['./watchlist.component.scss']
})
export class WatchlistComponent implements OnInit {
  watchListMovies: UserMovieList = {page: 0, totalPages: 0, results: [], totalResults: 0};
  watchListTvShows: UserTvShowList = {page: 0, totalPages: 0, results: [], totalResults: 0};

  loading = false;
  errorMessage = '';

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
      },
      error: err => console.log(err)
    });
  }

  loadInWatchListTvShows(): void {
    this.userTvService.getUserInWatchListTvShows().subscribe({
      next: (tvShows) => {
        this.watchListTvShows = tvShows;
      },
      error: err => console.log(err)
    });
  }

  hasContent(): boolean {
    return this.watchListMovies.results.length > 0 ||
      this.watchListTvShows.results.length > 0;
  }
}
