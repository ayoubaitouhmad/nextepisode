import {Component, OnInit} from '@angular/core';
import {NgFor, NgIf} from '@angular/common';
import {GroupedMovies, groupMoviesByType, MovieDto, STATIC_WATCHLIST_MOVIES} from '../movie.model';
import {TvCardComponent} from '../shared/tv-card/tv-card.component';
import {UserMovieList} from '../../../../../../core/models/user/movie/movie.model';
import {UserTvShowList} from '../../../../../../core/models/user/tv/tv.model';
import {MovieCardComponent} from '../shared/movie-card/movie-card.component';

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

  ngOnInit(): void {
    this.loadWatchlist();
  }

  loadWatchlist(): void {
    this.loading = true;
    setTimeout(() => {

      this.loading = false;
    }, 300);
  }

  hasContent(): boolean {
    return this.watchListMovies.results.length > 0 ||
      this.watchListTvShows.results.length > 0;
  }
}
