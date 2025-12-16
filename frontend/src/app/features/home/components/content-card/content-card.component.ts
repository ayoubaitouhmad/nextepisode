import {Component, EventEmitter, inject, Input, OnInit, Output} from '@angular/core';
import {CommonModule} from '@angular/common';
import {UserMovieService} from '../../../../core/services/user/movie/user-movie.service';
import {AuthService} from '../../../../core/services/auth/auth-service';
import {XMovie} from '../../../../core/models/common/movie.model';
import {TvSeries} from '../../../../core/models/common/tv.model';

@Component({
  selector: 'app-content-card',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './content-card.component.html',
  styleUrl: './content-card.component.scss'
})
export class ContentCardComponent implements OnInit {

  @Input({required: true}) content!: (XMovie | TvSeries);
  @Output() share = new EventEmitter<XMovie>();
  @Output() addToFavorites = new EventEmitter<XMovie>();
  @Output() addToWatched = new EventEmitter<XMovie>();
  @Output() addToWatchlist = new EventEmitter<XMovie>();

  // State properties for visual feedback
  isInFavorites = false;
  isInWatched = false;
  isInWatchlist = false;

  // Debouncing properties to prevent rapid clicks
  private isProcessingFavorites = false;
  private isProcessingWatched = false;
  private isProcessingWatchlist = false;

  private userMovieService = inject(UserMovieService);

  constructor(public auth: AuthService) {
  }


  onShare(): void {
    this.share.emit(this.content as XMovie);
  }

  onAddToFavorites(): void {
    if (this.isProcessingFavorites) {
      console.log('Already processing favorites request');
      return;
    }

    this.isProcessingFavorites = true;

    if (this.isInFavorites) {
      this.userMovieService.removeFromFavorites(parseInt(this.content.id)).subscribe({
        next: () => {
          this.isInFavorites = false;
          this.addClickAnimation('favorite');
          this.isProcessingFavorites = false;
        },
        error: (error) => {
          console.error('Error removing from favorites:', error);
          this.isProcessingFavorites = false;
        }
      });
    } else {
      this.userMovieService.addToFavorites(parseInt(this.content.id)).subscribe({
        next: () => {
          this.isInFavorites = true;
          this.addClickAnimation('favorite');
          this.isProcessingFavorites = false;
        },
        error: (error) => {
          console.error('Error adding to favorites:', error);
          this.isProcessingFavorites = false;
        }
      });
    }
    this.addToFavorites.emit(this.content as XMovie);
  }

  onAddToWatched(): void {
    if (this.isProcessingWatched) {
      console.log('Already processing watched request');
      return;
    }

    this.isProcessingWatched = true;

    if (this.isInWatched) {
      this.userMovieService.removeFromWatched(parseInt(this.content.id)).subscribe({
        next: () => {
          this.isInWatched = false;
          this.addClickAnimation('watched');
          this.isProcessingWatched = false;
        },
        error: (error) => {
          console.error('Error removing from watched:', error);
          this.isProcessingWatched = false;
        }
      });
    } else {
      this.userMovieService.addToWatched(parseInt(this.content.id)).subscribe({
        next: () => {
          this.isInWatched = true;
          this.addClickAnimation('watched');
          this.isProcessingWatched = false;
        },
        error: (error) => {
          console.error('Error adding to watched:', error);
          this.isProcessingWatched = false;
        }
      });
    }
    this.addToWatched.emit(this.content as XMovie);
  }

  onAddToWatchlist(): void {
    if (this.isProcessingWatchlist) {
      console.log('Already processing watchlist request');
      return;
    }

    this.isProcessingWatchlist = true;

    if (this.isInWatchlist) {
      this.userMovieService.removeFromWatchlist(parseInt(this.content.id)).subscribe({
        next: () => {
          this.isInWatchlist = false;
          this.addClickAnimation('watchlist');
          this.isProcessingWatchlist = false;
        },
        error: (error) => {
          console.error('Error removing from watchlist:', error);
          this.isProcessingWatchlist = false;
        }
      });
    } else {
      this.userMovieService.addToWatchlist(parseInt(this.content.id)).subscribe({
        next: () => {
          this.isInWatchlist = true;
          this.addClickAnimation('watchlist');
          this.isProcessingWatchlist = false;
        },
        error: (error) => {
          console.error('Error adding to watchlist:', error);
          this.isProcessingWatchlist = false;
        }
      });
    }
    this.addToWatchlist.emit(this.content as XMovie);
  }

  ngOnInit() {
    // Initialize state based on user's lists
    this.checkUserLists();
  }

  private checkUserLists(): void {
    // Check if movie is in user's lists by calling the backend API
    if (this.content && 'id' in this.content) {
      this.userMovieService.checkMovieStatus(parseInt(this.content.id)).subscribe({
        next: (status: any) => {
          this.isInFavorites = status.isFavorite;
          this.isInWatched = status.isWatched;
          this.isInWatchlist = status.isInWatchlist;
        },
        error: (error) => {
          console.error('Error checking movie status:', error);
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
