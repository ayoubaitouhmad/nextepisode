import {Component, EventEmitter, inject, Input, OnInit, Output} from '@angular/core';
import {CommonModule} from '@angular/common';
import {
  MovieStatus,
  MovieStatusAction,
  MovieStatusCategory,
  MovieStatusRequest,
  XMovie
} from '../../../core/models/common/movie.model';
import {AuthService} from '../../../core/services/auth/auth-service';
import {getYearFromDate} from '../../utils';
import {getFirstTwoFlatRate} from '../../utils/movie.utils';
import {UserMovieService} from '../../../core/services/user/movie/user-movie.service';


@Component({
  selector: 'app-movie-card',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './movie-card.component.html',
  styleUrl: './movie-card.component.scss'
})
export class MovieCardComponent implements OnInit {

  private userMovieService: UserMovieService = inject(UserMovieService);

  protected readonly getYearFromDate = getYearFromDate;
  protected readonly getFirstTwoFlatRate = getFirstTwoFlatRate;

  @Input({required: true}) content!: XMovie;
  @Output() share = new EventEmitter<XMovie>();
  @Output() addToFavorites = new EventEmitter<MovieStatusRequest>();
  @Output() addToWatched = new EventEmitter<MovieStatusRequest>();
  @Output() addToWatchlist = new EventEmitter<MovieStatusRequest>();

  movieStatus: MovieStatus = {
    inWatchlist: false,
    isFavorite: false,
    watched: false,
  }

  // Debouncing properties to prevent rapid clicks
  private isProcessingFavorites = false;
  private isProcessingWatched = false;
  private isProcessingWatchlist = false;


  constructor(public auth: AuthService) {
  }


  ngOnInit() {
    this.checkUserLists();
  }

  /**
   * Check movie listing state depend on the logged user
   * @private
   */
  private checkUserLists(): void {
    console.log("[MovieCardComponent] Check user movie status")

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
    if (this.isProcessingFavorites) {
      console.log('Already processing favorites request');
      return;
    }

    this.isProcessingFavorites = true;

    if (this.movieStatus.isFavorite) {
      this.userMovieService.removeFromFavorites(this.content.id).subscribe({
        next: () => {
          this.movieStatus.isFavorite = false;
          this.addClickAnimation('favorite');
          this.isProcessingFavorites = false;
          this.addToFavorites.emit({
            movieId: this.content.id,
            category: MovieStatusCategory.favorite,
            action: MovieStatusAction.remove
          });

        },
        error: (error) => {
          console.error('Error removing from favorites:', error);
          this.isProcessingFavorites = false;
        }
      });
    } else {
      this.userMovieService.addToFavorites(this.content.id).subscribe({
        next: () => {
          this.movieStatus.isFavorite = true;
          this.addClickAnimation('favorite');
          this.isProcessingFavorites = false;
          this.addToFavorites.emit({
            movieId: this.content.id,
            category: MovieStatusCategory.favorite,
            action: MovieStatusAction.add
          });
        },
        error: (error) => {
          console.error('Error adding to favorites:', error);
          this.isProcessingFavorites = false;
        }
      });
    }
  }

  onAddToWatched(): void {
    if (this.isProcessingWatched) {
      console.log('Already processing watched request');
      return;
    }

    this.isProcessingWatched = true;

    if (this.movieStatus.inWatchlist) {
      // this.userMovieService.removeFromWatched(parseInt(this.content.id)).subscribe({
      //   next: () => {
      //     this.isInWatched = false;
      //     this.addClickAnimation('watched');
      //     this.isProcessingWatched = false;
      //   },
      //   error: (error) => {
      //     console.error('Error removing from watched:', error);
      //     this.isProcessingWatched = false;
      //   }
      // });
    } else {
      // this.userMovieService.addToWatched(parseInt(this.content.id)).subscribe({
      //   next: () => {
      //     this.isInWatched = true;
      //     this.addClickAnimation('watched');
      //     this.isProcessingWatched = false;
      //   },
      //   error: (error) => {
      //     console.error('Error adding to watched:', error);
      //     this.isProcessingWatched = false;
      //   }
      // });
    }
    // this.addToWatched.emit(this.movieStatus);
  }

  onAddToWatchlist(): void {
    if (this.isProcessingWatchlist) {
      console.log('Already processing watchlist request');
      return;
    }

    this.isProcessingWatchlist = true;

    if (this.movieStatus.watched) {
      // this.userMovieService.removeFromWatchlist(parseInt(this.content.id)).subscribe({
      //   next: () => {
      //     this.isInWatchlist = false;
      //     this.addClickAnimation('watchlist');
      //     this.isProcessingWatchlist = false;
      //   },
      //   error: (error) => {
      //     console.error('Error removing from watchlist:', error);
      //     this.isProcessingWatchlist = false;
      //   }
      // });
    } else {
      // this.userMovieService.addToWatchlist(parseInt(this.content.id)).subscribe({
      // next: () => {
      //   this.isInWatchlist = true;
      //   this.addClickAnimation('watchlist');
      //   this.isProcessingWatchlist = false;
      // },
      // error: (error) => {
      //   console.error('Error adding to watchlist:', error);
      //   this.isProcessingWatchlist = false;
      // }
      // });
    }
    // this.addToWatchlist.emit(this.movieStatus);
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
