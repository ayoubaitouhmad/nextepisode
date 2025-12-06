import { Component, Input } from '@angular/core';
import { NgIf } from '@angular/common';
import { MovieDto, getMoviePosterUrl, getMovieYear, getMovieGenres } from '../../movie.model';
import {getUserMovieGenreAsString, UserMovie} from '../../../../../../../core/models/movie/movie.model';

@Component({
  selector: 'app-tv-card',
  standalone: true,
  imports: [NgIf],
  templateUrl: './tv-card.component.html',
  styleUrls: ['./tv-card.component.scss']
})
export class TvCardComponent {
  @Input() movie!: MovieDto;
  @Input() type: 'favorite' | 'watched' | 'watchlist' = 'favorite';

  getPosterUrl(): string {
    return getMoviePosterUrl(this.movie.posterPath);
  }

  getYear(): string {
    return getMovieYear(this.movie.releaseDate);
  }

  getGenres(): string {
    return getMovieGenres(this.movie.genres);
  }
}
