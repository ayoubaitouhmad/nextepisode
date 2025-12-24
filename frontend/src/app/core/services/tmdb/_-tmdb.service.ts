import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {Service} from './service';
import {Observable, of} from 'rxjs';
import {GenreList, LanguageList, PersonList, RegionList, WatchProviderList} from '../../models/common/shared-dtos';
import {LocalStorageCacheService} from '../local-storage-cache.service';
import {tap} from 'rxjs/operators';
import {RuntimeResponse} from '../../models/tmdb/runtime';


@Injectable({
  providedIn: 'root'
})
export class _TmdbService extends Service {

  public static readonly MOVIE_GENRE_CACHE_KEY = `movie-genres`;
  public static readonly TV_SHOW_CACHE_KEY = `tv-show-genres`;
  public static readonly LANGUAGES_CACHE_KEY = `languages`;
  public static readonly REGIONS_CACHE_KEY = `regions`;
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


  fetchGenres(path: string, cacheKey: string): Observable<GenreList> {
    const cached = this.localCache.get<GenreList>(cacheKey);
    return this.fetchDataFromCache(path, cacheKey);
  }


  /**
   * Get current user profile
   */
  getLanguages(): Observable<LanguageList> {
    return this.fetchDataFromCache("/configuration/languages", _TmdbService.LANGUAGES_CACHE_KEY);
  }

  fetchDataFromCache<T>(path: string, cacheKey: string): Observable<T> {
    const cached = this.localCache.get<T>(cacheKey);
    if (cached) {
      return of(cached);
    }
    {
      return this.http.get<T>(`${this.apiUrl}${path}`).pipe(
        tap(list => {
          this.localCache.set(cacheKey, list)
        })
      );
    }
  }


  /**
   * Get current user profile
   */
  getRegions(): Observable<RegionList> {
    return this.fetchDataFromCache("/watch-providers/available-regions", _TmdbService.REGIONS_CACHE_KEY);
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


  getRuntimes() {
    return this.http.get<RuntimeResponse>(`${this.apiUrl}/configuration/runtimes`);
  }

}
