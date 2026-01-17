import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {Observable} from 'rxjs';
import {
  MoviesStatus,
  MovieStatus,
  MovieStatusAction,
  MovieStatusCategory,
  MovieStatusRequest,
  UserMovieList
} from '../../../models/common/movie.model';
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
    return this.http.get<UserMoviesAndTvShowStats>(`${this.apiUrl}/statistics`);
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
  handleUserMovieRequest(request: MovieStatusRequest): Observable<any> {
    return this.http.post(`${this.apiUrl}/${request.movieId}/statistics`, request);
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
  checkMovieStatus(tmdbId: number): Observable<MovieStatus> {
    console.debug(`[UserMovieService] Check movie: ${tmdbId} listing stats`);
    return this.http.get<MovieStatus>(`${this.apiUrl}/${tmdbId}/statistics`);
  }

  /**
   * Add movie to favorites
   */
  addToFavorites(movieId: number): Observable<any> {
    return this.handleUserMovieRequest({
      movieId: movieId,
      category: MovieStatusCategory.favorite,
      action: MovieStatusAction.add
    });
  }

  /**
   * Remove movie from favorites
   */
  removeFromFavorites(movieId: number): Observable<any> {
    return this.handleUserMovieRequest({
      movieId: movieId,
      category: MovieStatusCategory.favorite,
      action: MovieStatusAction.remove
    });
  }

  /**
   * Add movie to watched
   */
  addToWatched(movieId: number): Observable<any> {
    return this.handleUserMovieRequest({
      movieId,
      category: MovieStatusCategory.watched,
      action: MovieStatusAction.add
    });
  }

  /**
   * Remove movie from watched
   */
  removeFromWatched(movieId: number): Observable<any> {
    return this.handleUserMovieRequest({
      movieId: movieId,
      category: MovieStatusCategory.watched,
      action: MovieStatusAction.remove
    });
  }

  /**
   * Add movie to watchlist
   */
  addToWatchlist(movieId: number): Observable<any> {
    return this.handleUserMovieRequest({
      movieId: movieId,
      category: MovieStatusCategory.watchlist,
      action: MovieStatusAction.add
    });
  }

  /**
   * Remove movie from watchlist
   */
  removeFromWatchlist(movieId: number): Observable<any> {
    return this.handleUserMovieRequest({
      movieId: movieId,
      category: MovieStatusCategory.watchlist,
      action: MovieStatusAction.remove
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

  public checkMultipleMovieStatuses(movieIds: number[]): Observable<MoviesStatus> {
    console.debug("[UserMovieService] Check movie statuses", movieIds);

    return this.http.post<MoviesStatus>(`${this.apiUrl}/statistics`, {
      movieIds
    });
  }

}
