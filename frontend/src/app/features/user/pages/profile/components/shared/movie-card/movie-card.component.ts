import { Component, Input } from '@angular/core';
import { NgIf } from '@angular/common';
import { MovieDto, getMoviePosterUrl, getMovieYear, getMovieGenres } from '../../movie.model';
import {getUserMovieGenreAsString, UserMovie} from '../../../../../../../core/models/user/movie/movie.model';

@Component({
  selector: 'app-movie-card',
  standalone: true,
  imports: [NgIf],
  templateUrl: './movie-card.component.html',
  styleUrls: ['./movie-card.component.scss']
})
export class MovieCardComponent {
  @Input() movie!: UserMovie;
  @Input() type: 'favorite' | 'watched' | 'watchlist' = 'favorite';

  getPosterUrl(): string {
    return getMoviePosterUrl(this.movie.poster_path);
  }

  getYear(): string {
    return getMovieYear(this.movie.release_date);
  }

  getGenres(): string {
    return getUserMovieGenreAsString(this.movie.genres);
  }
}
