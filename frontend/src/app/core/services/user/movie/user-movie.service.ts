import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {Observable} from 'rxjs';
import {UserMovieList} from '../../../models/common/movie.model';
import {Service} from '../service';
import {UserMoviesAndTvShowStats} from '../../../models/common/shared-dtos';


@Injectable({
  providedIn: 'root'
})
export class UserMovieService extends Service {

  protected apiUrl: string;

  constructor(http: HttpClient) {
    super(http);
    this.apiUrl = `${this.baseServiceApiUrl}/movies`;
  }

  /**
   * Get user's movie statistics (counts by category)
   */
  getUserMovieStatistics(): Observable<UserMoviesAndTvShowStats> {
    return this.http.get<UserMoviesAndTvShowStats>(`${this.apiUrl}/stats`);
  }

  /**
   * Get user's movie statistics (counts by category)
   */
  getUserFavoriteMovies(): Observable<UserMovieList> {
    return this.http.get<UserMovieList>(`${this.apiUrl}/favorite`);
  }

  /**
   * Get user's movie statistics (counts by category)
   */
  getUserWatchedMovies(): Observable<UserMovieList> {
    return this.http.get<UserMovieList>(`${this.apiUrl}/watched`);
  }

  /**
   * Get user's movie statistics (counts by category)
   */
  getUserInWatchListMovies(): Observable<UserMovieList> {
    return this.http.get<UserMovieList>(`${this.apiUrl}/watchlist`);
  }


  /*******************All functions above will be removed  *******************************/

  /**
   * Add or remove a movie from user's category
   */
  handleUserMovieRequest(request: any): Observable<any> {
    return this.http.post(`${this.apiUrl}/user-movie`, request);
  }

  /**
   * Get user's favorite movies
   */
  getUserFavorites(): Observable<any[]> {
    return this.http.get<any[]>(`${this.apiUrl}/user/favorites`);
  }

  /**
   * Get user's watched movies
   */
  getUserWatched(): Observable<any[]> {
    return this.http.get<any[]>(`${this.apiUrl}/user/watched`);
  }

  /**
   * Get user's watchlist
   */
  getUserWatchlist(): Observable<any[]> {
    return this.http.get<any[]>(`${this.apiUrl}/user/watchlist`);
  }

  /**
   * Get user's movies by category
   */
  getUserMoviesByCategory(category: string): Observable<any[]> {
    return this.http.get<any[]>(`${this.apiUrl}/user/${category.toLowerCase()}`);
  }

  /**
   * Check movie status for current user
   */
  checkMovieStatus(tmdbId: number): Observable<any> {
    return this.http.get<any>(`${this.apiUrl}/check-status/${tmdbId}`);
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
  getDetailedMovieStatistics(): Observable<any> {
    return this.http.get<any>(`${this.apiUrl}/user/statistics/detailed`);
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
