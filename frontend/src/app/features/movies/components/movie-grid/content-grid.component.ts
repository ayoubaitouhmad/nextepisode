import {Component, Input, Output, EventEmitter} from '@angular/core';
import {CommonModule} from '@angular/common';
import {Movie} from '../../../../core/models/movie.model';
import {ContentCardComponent} from '../content-card/content-card.component';
import {TvSeries} from '../../../../core/models/TMDTvSeries';

@Component({
  selector: 'app-content-grid',
  standalone: true,
  imports: [CommonModule, ContentCardComponent],
  templateUrl: './content-grid.component.html',
  styleUrl: './content-grid.component.scss'
})
export class ContentGridComponent {
  @Input() items: (Movie | TvSeries)[] = [];
  @Input() loading = false;
  @Input() viewMode: 'grid' | 'list' = 'grid';

  @Output() addToFavorites = new EventEmitter<Movie | TvSeries>();
  @Output() addToWatched = new EventEmitter<Movie | TvSeries>();
  @Output() addToWatchlist = new EventEmitter<Movie | TvSeries>();
  @Output() movieClick = new EventEmitter<Movie | TvSeries>();
  @Output() shareItem = new EventEmitter<Movie | TvSeries>();

  ngOnLoad() {
  }

  onShare(item: Movie | TvSeries): void {
    this.shareItem.emit(item);
  }

  onAddToFavorites(item: Movie | TvSeries): void {
    this.addToFavorites.emit(item);
  }

  onAddToWatched(item: Movie | TvSeries): void {
    this.addToWatched.emit(item);
  }

  onAddToWatchlist(item: Movie | TvSeries): void {
    this.addToWatchlist.emit(item);
  }

  onContentClick(item: Movie | TvSeries): void {
    this.movieClick.emit(item);
  }
}
