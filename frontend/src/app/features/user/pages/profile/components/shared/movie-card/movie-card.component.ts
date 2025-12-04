import { Component, Input } from '@angular/core';
import { NgIf } from '@angular/common';
import { MovieDto, getMoviePosterUrl, getMovieYear, getMovieGenres } from '../../movie.model';

@Component({
  selector: 'app-movie-card',
  standalone: true,
  imports: [NgIf],
  templateUrl: './movie-card.component.html',
  styleUrls: ['./movie-card.component.scss']
})
export class MovieCardComponent {
  @Input() movie!: MovieDto;
  @Input() type: 'favorite' | 'watched' | 'watchlist' = 'favorite';

  getPosterUrl(): string {
    return getMoviePosterUrl(this.movie?.posterPath);
  }

  getYear(): string {
    return getMovieYear(this.movie?.releaseDate);
  }

  getGenres(): string {
    return getMovieGenres(this.movie?.genres);
  }
}
