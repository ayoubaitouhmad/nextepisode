import {Component, Input} from '@angular/core';
import {NgIf} from '@angular/common';
import {getMovieGenres, getMoviePosterUrl, getMovieYear, MovieDto} from '../../movie.model';
import {UserTvShow} from '../../../../../../../core/models/user/tv/tv.model';
import {getUserMovieGenreAsString} from '../../../../../../../core/models/user/shared/shared-dtos';

@Component({
  selector: 'app-tv-card',
  standalone: true,
  imports: [NgIf],
  templateUrl: './tv-card.component.html',
  styleUrls: ['./tv-card.component.scss']
})
export class TvCardComponent {
  @Input() tvShow!: UserTvShow;
  @Input() type: 'favorite' | 'watched' | 'watchlist' = 'favorite';

  getPosterUrl(): string {
    return getMoviePosterUrl(this.tvShow.poster_path);
  }

  getYear(): string {
    return getMovieYear(this.tvShow.release_date);
  }

  getGenres(): string {
    return getUserMovieGenreAsString(this.tvShow.genres);
  }
}
