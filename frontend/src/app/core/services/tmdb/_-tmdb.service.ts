import {Injectable} from '@angular/core';
import {HttpClient, HttpParams} from '@angular/common/http';
import {Service} from './service';
import {Observable, of} from 'rxjs';
import {
  Certification,
  CertificationList,
  GenreList,
  LanguageList,
  PersonList,
  RegionList,
  SortOptionsDto,
  WatchProviderList
} from '../../models/common/shared-dtos';
import {LocalStorageCacheService} from '../local-storage-cache.service';
import {tap} from 'rxjs/operators';
import {RuntimeResponse} from '../../models/tmdb/runtime';
import {MovieList} from '../../models/common/movie.model';


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
  getMovieWatchProvidersByCountry(countryCode = 'us'): Observable<WatchProviderList> {
    return this.http.get<WatchProviderList>(`${this.apiUrl}/watch-providers/movie`, {
      params: {
        "region": countryCode
      }
    });
  }

  /**
   * Get current user profile
   */
  getTvWatchProvidersByCountry(countryCode = 'us'): Observable<WatchProviderList> {
    return this.http.get<WatchProviderList>(`${this.apiUrl}/watch-providers/tv-show`, {
      params: {
        "region": countryCode
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

  getYears(): Observable<Number[]> {
    return this.http.get<Number[]>(`${this.apiUrl}/configuration/years`);
  }


  getMoviesCertification() {
    return this.http.get<CertificationList | Certification[]>(`${this.apiUrl}/certifications/movies`);
  }

  getTvCertification() {
    return this.http.get<CertificationList | Certification[]>(`${this.apiUrl}/certifications/tv`);
  }

  getMoviesCertificationByCountry(countryCode = 'us'): Observable<Certification[]> {
    if (countryCode != "") {
      countryCode = countryCode.toLowerCase();
    }
    return this.http.get<Certification[]>(`${this.apiUrl}/certifications/movies/${countryCode}`);
  }

  getTvCertificationByCountry(countryCode = 'us'): Observable<Certification[]> {
    if (!countryCode) {
      countryCode = countryCode.toLowerCase();
    }
    return this.http.get<Certification[]>(`${this.apiUrl}/certifications/tv/${countryCode}`);
  }

  getSortOptions(): Observable<SortOptionsDto> {
    return this.http.get<SortOptionsDto>(`${this.apiUrl}/configuration/sorting`);
  }

  /**
   * Discover movies with advanced filters
   */
  discoverMovies(filters: {
    type?: string;
    year?: number;
    yearFrom?: number;
    with_watch_providers?: number[];
    yearTo?: number;
    genres?: number[];
    sortBy?: string;
    page?: number;
    language?: string;
    includeAdult?: boolean;
    watch_region?: string;
  } = {}): Observable<MovieList> {

    let params = new HttpParams()
      .set('page', filters.page || 1)
      .set('sort_by', filters.sortBy || 'vote_average.desc')
      .set('vote_count.gte', '50')
      .set('include_adult', (filters.includeAdult || false).toString())
      .set("watch_region", filters.watch_region || "US");

    // Add year filters
    if (filters.year) {
      params = params
        .set('primary_release_year', filters.year.toString());
    } else {
      if (filters.yearFrom) {
        params = params.set('primary_release_date.gte', `${filters.yearFrom}-01-01`);
      }
      if (filters.yearTo) {
        params = params.set('primary_release_date.lte', `${filters.yearTo}-12-31`);
      }
    }

    if (filters.with_watch_providers) {
      params = params.set('with_watch_providers', filters.with_watch_providers.join('|'));
    }

    // Add genre filters
    if (filters.genres && filters.genres.length > 0) {
      params = params.set('with_genres', filters.genres.join(','));
    }

    // Add language filter
    if (filters.language && filters.language !== 'Any') {
      params = params.set('with_original_language', filters.language.toLowerCase());
    }

    console.log(filters)

    return this.http.get<MovieList>(`${this.apiUrl}/movies/discover`, {params});
  }

}
