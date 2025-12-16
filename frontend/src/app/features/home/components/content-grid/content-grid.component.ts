import {Component, EventEmitter, Input, Output} from '@angular/core';
import {CommonModule} from '@angular/common';
import {ContentCardComponent} from '../content-card/content-card.component';
import {XMovie} from '../../../../core/models/common/movie.model';
import {TvSeries} from '../../../../core/models/common/tv.model';


@Component({
  selector: 'app-content-grid',
  standalone: true,
  imports: [CommonModule, ContentCardComponent],
  templateUrl: './content-grid.component.html',
  styleUrl: './content-grid.component.scss'
})
export class ContentGridComponent {
  @Input() items: (XMovie | TvSeries)[] = [];
  @Input() loading = false;
  @Input() viewMode: 'grid' | 'list' = 'grid';

  @Output() addToFavorites = new EventEmitter<XMovie | TvSeries>();
  @Output() addToWatched = new EventEmitter<XMovie | TvSeries>();
  @Output() addToWatchlist = new EventEmitter<XMovie | TvSeries>();
  @Output() movieClick = new EventEmitter<XMovie | TvSeries>();
  @Output() shareItem = new EventEmitter<XMovie | TvSeries>();

  ngOnLoad() {
  }

  onShare(item: XMovie | TvSeries): void {
    this.shareItem.emit(item);
  }

  onAddToFavorites(item: XMovie | TvSeries): void {
    this.addToFavorites.emit(item);
  }

  onAddToWatched(item: XMovie | TvSeries): void {
    this.addToWatched.emit(item);
  }

  onAddToWatchlist(item: XMovie | TvSeries): void {
    this.addToWatchlist.emit(item);
  }

  onContentClick(item: XMovie | TvSeries): void {
    this.movieClick.emit(item);
  }
}
