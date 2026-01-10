// movie-status-cache.service.ts
import {Injectable} from '@angular/core';
import {BehaviorSubject, Observable} from 'rxjs';
import {MovieStatus} from '../../../models/common/movie.model';

@Injectable({
  providedIn: 'root'
})
export class MovieStatusCacheService {
  private statusCache = new Map<number, BehaviorSubject<MovieStatus>>();
  private loadedMovies = new Set<number>(); // Track which have been loaded

  /**
   * Get status observable for a movie (creates empty if doesn't exist)
   */
  getStatus(movieId: number): Observable<MovieStatus> {
    if (!this.statusCache.has(movieId)) {
      this.statusCache.set(movieId, new BehaviorSubject<MovieStatus>({
        inWatchlist: false,
        isFavorite: false,
        watched: false
      }));
    } else {
      console.log(`has the ${movieId}`)
    }

    return this.statusCache.get(movieId)!.asObservable();
  }

  /**
   * Update status for a movie
   */
  updateStatus(movieId: number, status: Partial<MovieStatus>): void {
    if (!this.statusCache.has(movieId)) {
      this.statusCache.set(movieId, new BehaviorSubject<MovieStatus>({
        inWatchlist: false,
        isFavorite: false,
        watched: false
      }));
    }

    const current = this.statusCache.get(movieId)!;
    current.next({...current.value, ...status});
    this.loadedMovies.add(movieId);
  }

  /**
   * Batch update multiple statuses
   */
  updateMultiple(statuses: Map<number, MovieStatus>): void {
    if (statuses instanceof Map) {
      // It's a Map
      statuses.forEach((status, movieId) => {
        this.updateStatus(movieId, status);
      });
    } else {
      // It's a plain object
      Object.entries(statuses).forEach(([id, status]) => {
        this.updateStatus(Number(id), status as MovieStatus);
      });
    }

  }

  /**
   * Check if status has been loaded (not just default)
   */
  hasStatus(movieId: number): boolean {
    return this.statusCache.has(movieId);
  }

  /**
   * Clear cache (e.g., on logout)
   */
  clearCache(): void {
    this.statusCache.clear();
    this.loadedMovies.clear();
  }

  /**
   * Invalidate specific movie (for refresh)
   */
  invalidate(movieId: number): void {
    this.loadedMovies.delete(movieId);
  }
}
