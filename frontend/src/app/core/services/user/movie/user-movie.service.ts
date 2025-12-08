import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {Observable} from 'rxjs';
import {environment} from '../../../../../environments/environment';
import {UserMovieList} from '../../../models/user/movie/movie.model';

export interface UserMovieRequest {
  tmdbId: number;
  category: 'FAVORITE' | 'WATCHED' | 'WATCHLIST';
  action: 'ADD' | 'REMOVE';
}

export interface MovieStatus {
  isFavorite: boolean;
  isWatched: boolean;
  isInWatchlist: boolean;
}

export interface MovieStatistics {
  favoriteCount: number;
  watchedCount: number;
  watchlistCount: number;
}

export interface DetailedMovieStatistics {
  favorites: {
    total: number;
    movies: number;
    tvSeries: number;
  };
  watched: {
    total: number;
    movies: number;
    tvSeries: number;
  };
  watchlist: {
    total: number;
    movies: number;
    tvSeries: number;
  };
}

export interface MovieDto {
  id: number;
  tmdbId: number;
  title: string;
  overview: string;
  posterPath: string;
  backdropPath: string;
  releaseDate: string;
  voteAverage: number;
  voteCount: number;
  originalLanguage: string;
  originalTitle: string;
  adult: boolean;
  status: string;
  tagline: string;
  runtime: number;
  genres: string[];
  type: string; // 'movie' or 'tv'
  category: string;
  isInUserList: boolean;
}

@Injectable({
  providedIn: 'root'
})
export class UserMovieService {

  private apiUrl = `${environment.apiUrl}/user/movies`;

  constructor(private http: HttpClient) {
  }


  /**
   * Get user's movie statistics (counts by category)
   */
  getUserMovieStatistics(): Observable<MovieStatistics> {
    return this.http.get<MovieStatistics>(`${this.apiUrl}/stats`);
  }

  /**
   * Get user's movie statistics (counts by category)
   */
  getUserFavoriteMovies(): Observable<UserMovieList> {
    return this.http.get<UserMovieList>(`${this.apiUrl}/favorite`);
  }


  /**************************************************/

  /**
   * Add or remove a movie from user's category
   */
  handleUserMovieRequest(request: UserMovieRequest): Observable<any> {
    return this.http.post(`${this.apiUrl}/user-movie`, request);
  }

  /**
   * Get user's favorite movies
   */
  getUserFavorites(): Observable<MovieDto[]> {
    return this.http.get<MovieDto[]>(`${this.apiUrl}/user/favorites`);
  }

  /**
   * Get user's watched movies
   */
  getUserWatched(): Observable<MovieDto[]> {
    return this.http.get<MovieDto[]>(`${this.apiUrl}/user/watched`);
  }

  /**
   * Get user's watchlist
   */
  getUserWatchlist(): Observable<MovieDto[]> {
    return this.http.get<MovieDto[]>(`${this.apiUrl}/user/watchlist`);
  }

  /**
   * Get user's movies by category
   */
  getUserMoviesByCategory(category: string): Observable<MovieDto[]> {
    return this.http.get<MovieDto[]>(`${this.apiUrl}/user/${category.toLowerCase()}`);
  }

  /**
   * Check movie status for current user
   */
  checkMovieStatus(tmdbId: number): Observable<MovieStatus> {
    return this.http.get<MovieStatus>(`${this.apiUrl}/check-status/${tmdbId}`);
  }

  /**
   * Add movie to favorites
   */
  addToFavorites(tmdbId: number): Observable<any> {
    return this.handleUserMovieRequest({
      tmdbId,
      category: 'FAVORITE',
      action: 'ADD'
    });
  }

  /**
   * Remove movie from favorites
   */
  removeFromFavorites(tmdbId: number): Observable<any> {
    return this.handleUserMovieRequest({
      tmdbId,
      category: 'FAVORITE',
      action: 'REMOVE'
    });
  }

  /**
   * Add movie to watched
   */
  addToWatched(tmdbId: number): Observable<any> {
    return this.handleUserMovieRequest({
      tmdbId,
      category: 'WATCHED',
      action: 'ADD'
    });
  }

  /**
   * Remove movie from watched
   */
  removeFromWatched(tmdbId: number): Observable<any> {
    return this.handleUserMovieRequest({
      tmdbId,
      category: 'WATCHED',
      action: 'REMOVE'
    });
  }

  /**
   * Add movie to watchlist
   */
  addToWatchlist(tmdbId: number): Observable<any> {
    return this.handleUserMovieRequest({
      tmdbId,
      category: 'WATCHLIST',
      action: 'ADD'
    });
  }

  /**
   * Remove movie from watchlist
   */
  removeFromWatchlist(tmdbId: number): Observable<any> {
    return this.handleUserMovieRequest({
      tmdbId,
      category: 'WATCHLIST',
      action: 'REMOVE'
    });
  }


  /**
   * Get detailed user's movie statistics with type breakdown
   */
  getDetailedMovieStatistics(): Observable<DetailedMovieStatistics> {
    return this.http.get<DetailedMovieStatistics>(`${this.apiUrl}/user/statistics/detailed`);
  }

  /**
   * Test authentication endpoint
   */
  testAuthentication(): Observable<any> {
    return this.http.get(`${this.apiUrl}/test-auth`);
  }

  /**
   * Clean up duplicate entries
   */
  cleanupDuplicates(): Observable<any> {
    return this.http.post(`${this.apiUrl}/cleanup-duplicates`, {});
  }
}
