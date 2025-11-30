import { Component, Input } from '@angular/core';
import { NgIf } from '@angular/common';
import { MovieDto } from '../../../../../core/services/user-movie.service';

export type MovieCardType = 'favorite' | 'watched' | 'watchlist';

@Component({
  selector: 'app-movie-card',
  standalone: true,
  imports: [NgIf],
  templateUrl: './movie-card.component.html',
  styleUrls: ['./movie-card.component.scss']
})
export class MovieCardComponent {
  @Input() movie!: MovieDto;
  @Input() cardType: MovieCardType = 'favorite';

  getMoviePosterUrl(posterPath: string): string {
    if (!posterPath) {
      return 'https://images.unsplash.com/photo-1489599732536-9a2b0caa395a?w=300&h=400&fit=crop';
    }
    return `https://image.tmdb.org/t/p/w500${posterPath}`;
  }

  getMovieYear(releaseDate: string): string {
    if (!releaseDate) return 'Unknown';
    return new Date(releaseDate).getFullYear().toString();
  }

  getMovieGenres(genres: string[]): string {
    if (!genres || genres.length === 0) return 'Unknown';
    return genres.slice(0, 2).join(', ');
  }
}
