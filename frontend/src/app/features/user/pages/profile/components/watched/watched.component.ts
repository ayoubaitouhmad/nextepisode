import {Component, OnInit} from '@angular/core';
import {NgFor, NgIf} from '@angular/common';
import {TvCardComponent} from '../shared/tv-card/tv-card.component';
import {UserMovieList} from '../../../../../../core/models/user/movie/movie.model';
import {UserTvShowList} from '../../../../../../core/models/user/tv/tv.model';
import {MovieCardComponent} from '../shared/movie-card/movie-card.component';

@Component({
  selector: 'app-watched',
  standalone: true,
  imports: [NgFor, NgIf, TvCardComponent, MovieCardComponent],
  templateUrl: './watched.component.html',
  styleUrls: ['./watched.component.scss']
})
export class WatchedComponent implements OnInit {
  watchedMovies: UserMovieList = {page: 0, totalPages: 0, results: [], totalResults: 0};
  watchedTvShows: UserTvShowList = {page: 0, totalPages: 0, results: [], totalResults: 0};

  loading = false;
  errorMessage = '';

  ngOnInit(): void {
    this.loadWatched();
  }

  loadWatched(): void {
    this.loading = true;
    setTimeout(() => {

      this.loading = false;
    }, 300);
  }

  hasContent(): boolean {
    return this.watchedMovies.results.length > 0 ||
      this.watchedTvShows.results.length > 0;
  }
}
