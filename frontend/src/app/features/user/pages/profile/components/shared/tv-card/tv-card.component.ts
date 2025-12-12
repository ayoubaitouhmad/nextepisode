import {Component, Input} from '@angular/core';
import {NgIf} from '@angular/common';
import {UserTvShow} from '../../../../../../../core/models/user/tv/tv.model';
import {getUserMovieGenreAsString} from '../../../../../../../core/models/user/shared/shared-dtos';
import {getYearFromDate} from '../../../../../../../shared/utils';

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


  getYear(): string {
    return getYearFromDate(this.tvShow.release_date);
  }

  getGenres(): string {
    return getUserMovieGenreAsString(this.tvShow.genres);
  }
}
