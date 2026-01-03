import {Component, EventEmitter, Input, Output} from '@angular/core';
import {CommonModule} from '@angular/common';
import {XMovie} from '../../../core/models/common/movie.model';
import {MovieCardComponent} from '../movie-card/movie-card.component';
import {getFirstTwoFlatRate} from '../../utils/movie.utils';


@Component({
  selector: 'app-movie-grid',
  standalone: true,
  imports: [CommonModule, MovieCardComponent],
  templateUrl: './movie-grid.component.html',
  styleUrl: 'movie-grid.component.scss'
})
export class MovieGridComponent {

  protected readonly getFirstTwoFlatRate = getFirstTwoFlatRate;

  @Input() items: XMovie[] = [];
  @Input() loading = false;
  @Input() viewMode: 'grid' | 'list' = 'grid';
  @Output() addToFavorites = new EventEmitter<XMovie>();
  @Output() addToWatched = new EventEmitter<XMovie>();
  @Output() addToWatchlist = new EventEmitter<XMovie>();
  @Output() movieClick = new EventEmitter<XMovie>();
  @Output() shareItem = new EventEmitter<XMovie>();

  onShare(item: XMovie): void {
    this.shareItem.emit(item);
  }

  onAddToFavorites(item: XMovie): void {
    this.addToFavorites.emit(item);
  }

  onAddToWatched(item: XMovie): void {
    this.addToWatched.emit(item);
  }

  onAddToWatchlist(item: XMovie): void {
    this.addToWatchlist.emit(item);
  }

  onContentClick(item: XMovie): void {
    this.movieClick.emit(item);
  }

}
