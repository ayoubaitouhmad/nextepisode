import {Component, signal} from '@angular/core';
import {CommonModule} from '@angular/common';
import {MoviesComponent} from './components/movies/movies.component';
import {TvShowsComponent} from './components/tvshows/tvshows.component';

export type ContentType = 'movie' | 'tv';

@Component({
  selector: 'app-home',
  standalone: true,
  imports: [CommonModule, MoviesComponent, TvShowsComponent],
  templateUrl: './home.component.html',
  styleUrl: './home.component.scss'
})
export class HomeComponent {
  contentType = signal<ContentType>('movie');

  toggleContentType(): void {
    this.contentType.update(type => type === 'movie' ? 'tv' : 'movie');
  }
}
