import {Component, EventEmitter, inject, Input, OnInit, Output} from '@angular/core';
import {CommonModule} from '@angular/common';
import {MovieStatus, XMovie} from '../../../core/models/common/movie.model';
import {AuthService} from '../../../core/services/auth/auth-service';
import {getYearFromDate} from '../../utils';
import {getFirstTwoFlatRate} from '../../utils/movie.utils';
import {UserMovieService} from '../../../core/services/user/movie/user-movie.service';
import {AlertService} from '../alert/alert.service';
import {MovieStatusCacheService} from '../../../core/services/user/movie/movie-status-cache.service';


@Component({
  selector: 'app-movie-card',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './movie-card.component.html',
  styleUrl: './movie-card.component.scss'
})
export class MovieCardComponent implements OnInit {

  private userMovieService: UserMovieService = inject(UserMovieService);
  private movieStatusCacheService = inject(MovieStatusCacheService);

  protected readonly getYearFromDate = getYearFromDate;
  protected readonly getFirstTwoFlatRate = getFirstTwoFlatRate;

  @Input({required: true}) content!: XMovie;
  @Output() share = new EventEmitter<XMovie>();

  movieStatus: MovieStatus = {
    inWatchlist: false,
    isFavorite: false,
    watched: false,
  }

  // Debouncing properties to prevent rapid clicks
  private isProcessingFavorites = false;
  private isProcessingWatched = false;
  private isProcessingWatchlist = false;

  constructor(
    public auth: AuthService,
    private alertService: AlertService) {
  }

  ngOnInit() {
    this.movieStatusCacheService.getStatus(this.content.id)
      .subscribe(status => {
        this.movieStatus = status;
      });

    // Check if we need to load (cache miss)
    if (!this.movieStatusCacheService.hasStatus(this.content.id)) {
      this.checkUserLists();
    }
  }

  /**
   * Check movie listing state depend on the logged user
   * @private
   */
  private checkUserLists(): void {
    console.log("[MovieCardComponent] Check user movies status")

    if (this.content && 'id' in this.content) {
      this.userMovieService.checkMovieStatus(this.content.id).subscribe({
        next: (status: MovieStatus) => {
          this.movieStatus = status;
        },
        error: (error) => {
          console.error('Error checking user movie status:', error);
        }
      });
    }
  }

  onShare(): void {
    this.share.emit(this.content as XMovie);
  }

  onAddToFavorites(): void {
    console.debug(`[MovieCardComponent] favorite request requested for movie:${this.content.id}`);

    if (this.isProcessingFavorites) {
      console.debug('[MovieCardComponent] Already processing favorites request');
      this.alertService.warning("Already processing favorites request")
      return;
    }

    this.isProcessingFavorites = true;

    if (this.movieStatus.isFavorite) {
      this.userMovieService.removeFromFavorites(this.content.id).subscribe({
        next: () => {
          this.movieStatus.isFavorite = false;
          this.addClickAnimation('favorite');
          this.isProcessingFavorites = false;
          this.movieStatusCacheService.updateStatus(this.content.id, {isFavorite: false});

          console.debug(`[MovieCardComponent] The movie with id:${this.content.id} removed successfully from favorites.`, this.content);
          this.alertService.success(`${this.content.title} removed from favorites.`);
        },
        error: (error) => {
          console.error('[MovieCardComponent] Error removing from favorites:', error);
          this.isProcessingFavorites = false;
        }
      });
    } else {
      this.userMovieService.addToFavorites(this.content.id).subscribe({
        next: () => {
          this.movieStatus.isFavorite = true;
          this.addClickAnimation('favorite');
          this.isProcessingFavorites = false;
          this.movieStatusCacheService.updateStatus(this.content.id, {isFavorite: true});

          console.debug(`[MovieCardComponent] The movie with id:${this.content.id} added successfully to favorites.`, this.content);
          this.alertService.success(`${this.content.title} added to favorites.`);
        },
        error: (error) => {
          console.error('[MovieCardComponent]Error adding to favorites:', error);
          this.isProcessingFavorites = false;
        }
      });
    }

  }

  onAddToWatched(): void {
    console.debug(`[MovieCardComponent] watched request requested for movie:${this.content.id}`);


    if (this.isProcessingWatched) {
      console.debug('[MovieCardComponent] Already processing watched request');
      this.alertService.warning("Already processing watched request")
      return;
    }

    this.isProcessingWatched = true;

    if (this.movieStatus.watched) {
      this.userMovieService.removeFromWatched(this.content.id).subscribe({
        next: () => {
          this.movieStatus.watched = false;
          this.addClickAnimation('watched');
          this.isProcessingWatched = false;
          this.movieStatusCacheService.updateStatus(this.content.id, {watched: false});

          console.debug(`[MovieCardComponent] The movie with id:${this.content.id} removed successfully from watched list.`, this.content);
          this.alertService.success(`${this.content.title} removed from watched list.`);
        },
        error: (error) => {
          console.error(`[MovieCardComponent] Error removing movie with id:${this.content.id} from watched list:`, error, this.content);
          this.isProcessingFavorites = false;
        }
      });
    } else {
      this.userMovieService.addToWatched(this.content.id).subscribe({
        next: () => {
          this.movieStatus.watched = true;
          this.addClickAnimation('watched');
          this.isProcessingWatched = false;
          this.movieStatusCacheService.updateStatus(this.content.id, {watched: true});

          console.debug(`[MovieCardComponent] The movie with id:${this.content.id} added successfully to watched list.`, this.content);
          this.alertService.success(`${this.content.title} added to watched list.`);
        },
        error: (error) => {
          console.error(`[MovieCardComponent] Error adding movie with id:${this.content.id} to watched list:`, error, this.content);
          this.isProcessingFavorites = false;
        }
      });
    }
  }

  onAddToWatchlist(): void {
    console.debug(`[MovieCardComponent] watch list request requested for movie:${this.content.id}`);


    if (this.isProcessingWatchlist) {
      console.debug('[MovieCardComponent] Already processing watch list request');
      this.alertService.warning("Already processing watch list request")
      return;
    }

    this.isProcessingWatchlist = true;

    if (this.movieStatus.inWatchlist) {
      this.userMovieService.removeFromWatchlist(this.content.id).subscribe({
        next: () => {
          this.movieStatus.inWatchlist = false;
          this.addClickAnimation('watchlist');
          this.isProcessingWatchlist = false;
          this.movieStatusCacheService.updateStatus(this.content.id, {inWatchlist: false});

          console.debug(`[MovieCardComponent] The movie with id:${this.content.id} removed successfully from watch list.`, this.content);
          this.alertService.success(`${this.content.title} removed from watch list.`);
        },
        error: (error) => {
          console.error(`[MovieCardComponent] Error removing movie with id:${this.content.id} from watch list:`, error, this.content);
          this.isProcessingWatchlist = false;
        }
      });
    } else {
      this.userMovieService.addToWatchlist(this.content.id).subscribe({
        next: () => {
          this.movieStatus.inWatchlist = true;
          this.addClickAnimation('watchlist');
          this.isProcessingWatchlist = false;
          this.movieStatusCacheService.updateStatus(this.content.id, {inWatchlist: true});

          console.debug(`[MovieCardComponent] The movie with id:${this.content.id} added successfully to watch list.`, this.content);
          this.alertService.success(`${this.content.title} added from watch list.`);
        },
        error: (error) => {
          console.error(`[MovieCardComponent] Error adding movie with id:${this.content.id} to watch list:`, error, this.content);
          this.isProcessingFavorites = false;
        }
      });
    }
  }


  private addClickAnimation(buttonType: 'favorite' | 'watched' | 'watchlist'): void {
    // Add click animation class
    const button = document.querySelector(`.${buttonType}`) as HTMLElement;
    if (button) {
      button.classList.add('button-clicked');
      setTimeout(() => {
        button.classList.remove('button-clicked');
      }, 300);
    }
  }

}
