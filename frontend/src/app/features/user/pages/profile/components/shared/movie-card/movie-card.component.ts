import {Component, Input} from '@angular/core';
import {NgIf} from '@angular/common';
import {getUserMovieGenreAsString, UserMovie} from '../../../../../../../core/models/user/movie/movie.model';
import {getYearFromDate} from '../../../../../../../shared/utils';

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


  getYear(): string {
    return getYearFromDate(this.movie.release_date);
  }

  getGenres(): string {
    return getUserMovieGenreAsString(this.movie.genres);
  }
}
