import {Component, OnInit} from '@angular/core';
import {NgFor, NgIf} from '@angular/common';
import {TvCardComponent} from '../shared/tv-card/tv-card.component';
import {UserMovieList} from '../../../../../../core/models/user/movie/movie.model';
import {UserTvShowList} from '../../../../../../core/models/user/tv/tv.model';
import {MovieCardComponent} from '../shared/movie-card/movie-card.component';
import {UserMovieService} from '../../../../../../core/services/user/movie/user-movie.service';
import {UserTvService} from '../../../../../../core/services/user/tv/user-tv.service';

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
  loading: boolean = false;
  errorMessage: string = '';

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
      },
      error: err => console.log(err)
    });
  }

  loadWatchedTvShows(): void {
    this.userTvService.getUserWatchedTvShows().subscribe({
      next: (tvShows) => {
        this.watchedTvShows = tvShows;
      },
      error: err => console.log(err)
    });
  }


  hasContent(): boolean {
    return this.watchedMovies.results.length > 0 ||
      this.watchedTvShows.results.length > 0;
  }
}
