import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {Service} from './service';
import {Observable, of} from 'rxjs';
import {GenreList, LanguageList, PersonList, RegionList, WatchProviderList} from '../../models/common/shared-dtos';
import {LocalStorageCacheService} from '../local-storage-cache.service';
import {tap} from 'rxjs/operators';


@Injectable({
  providedIn: 'root'
})
export class _TmdbService extends Service {

  public static readonly MOVIE_GENRE_CACHE_KEY = `movie-genres`;
  public static readonly TV_SHOW_CACHE_KEY = `tv-show-genres`;
  protected apiUrl: string;

  constructor(http: HttpClient, private localCache: LocalStorageCacheService) {
    super(http);
    this.apiUrl = `${this.baseServiceApiUrl}`;
  }

  /**
   * Get current user profile
   */
  getMoviesGenres(): Observable<GenreList> {
    const path = `/genres/movie-genres`;
    return this.fetchGenres(path, _TmdbService.MOVIE_GENRE_CACHE_KEY)
  }

  /**
   * Get current user profile
   */
  getTvShowsGenres(): Observable<GenreList> {
    const path = `/genres/tv-show-genres`;
    return this.fetchGenres(path, _TmdbService.TV_SHOW_CACHE_KEY)
  }


  fetchGenres(path: string, cacheKey: string) {
    const cached = this.localCache.get<GenreList>(cacheKey);
    if (cached) {
      return of(cached);
    }
    {
      return this.http.get<GenreList>(`${this.apiUrl}${path}`).pipe(
        tap(list => {
          list.stored_at = new Date();
          this.localCache.set(cacheKey, list)
        })
      );
    }
  }

  /**
   * Get current user profile
   */
  getLanguages(): Observable<LanguageList> {
    return this.http.get<LanguageList>(`${this.apiUrl}/configuration/languages`);
  }

  /**
   * Get current user profile
   */
  getRegions(): Observable<RegionList> {
    return this.http.get<RegionList>(`${this.apiUrl}/watch-providers/available-regions`);
  }

  /**
   * Get current user profile
   */
  getMovieWatchProviders(): Observable<WatchProviderList> {
    return this.http.get<WatchProviderList>(`${this.apiUrl}/watch-providers/movie`, {
      params: {
        "region": "US"
      }
    });
  }

  /**
   * Get current user profile
   */
  searchPerson(query: string, page: number = 1): Observable<PersonList> {
    return this.http.get<PersonList>(`${this.apiUrl}/search/person`, {
      params: {
        "page": page,
        "query": query,
      }
    });
  }

}
