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
import {MovieFilters} from '../../models/tmdb/request/content-filters';


@Injectable({
  providedIn: 'root'
})
export class _TmdbService extends Service {
  private static readonly LOGGER_CLASS = 'TmdbService';


  public static readonly MOVIE_GENRE_CACHE_KEY = `MOVIE_GENRES_CACHE`;
  public static readonly TV_SHOW_CACHE_KEY = `TV_SHOW_GENRES_CACHE`;
  public static readonly LANGUAGES_CACHE_KEY = `LANGUAGES_CACHE`;
  public static readonly REGIONS_CACHE_KEY = `REGIONS_CACHE`;
  public static readonly YEARS_CACHE_KEY = `YEARS_CACHE`;
  public static readonly SORT_BY_CACHE_KEY = `SORT_BY_CACHE`;
  public static readonly RUNTIMES_CACHE_KEY = `RUNTIMES_CACHE`;

  protected apiUrl: string;

  constructor(
    http: HttpClient,
    private localCache: LocalStorageCacheService
  ) {
    super(http);
    this.logger = this.loggerService.create(_TmdbService.LOGGER_CLASS);
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
    return this.fetchDataFromCache(path, cacheKey);
  }


  /**
   * Get current user profile
   */
  getLanguages(): Observable<LanguageList> {
    return this.fetchDataFromCache("/configuration/languages", _TmdbService.LANGUAGES_CACHE_KEY);
  }

  fetchDataFromCache<T>(path: string, cacheKey: string): Observable<T> {
    this.logger.debug(`Start fetching data...`);


    const cached = this.localCache.get<T>(cacheKey);
    if (cached) {
      this.logger.debug(`The key:${cacheKey} found in cached data`);

      return of(cached);
    }
    {
      this.logger.debug(`Get data from path:${path} and store in cached data with key:${cacheKey}`);

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


  getRuntimes(): Observable<RuntimeResponse> {
    this.logger.debug(`Getting runtimes for content filtering`);

    return this.fetchDataFromCache("/configuration/runtimes", _TmdbService.RUNTIMES_CACHE_KEY);
  }


  getYears(): Observable<Number[]> {
    this.logger.debug(`Getting years for content filtering`);

    return this.fetchDataFromCache("/configuration/years", _TmdbService.YEARS_CACHE_KEY);
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
    this.logger.debug(`Getting sort by options for content filtering.`);

    return this.fetchDataFromCache("/configuration/sorting", _TmdbService.SORT_BY_CACHE_KEY);


    // return this.http.get<SortOptionsDto>(`${this.apiUrl}/configuration/sorting`);
  }

  /**
   * Discover movies with advanced filters
   */
  discoverMovies(filters: any): Observable<MovieList> {
    this.logger.info('[Movies] Discover movies request');

    const filterParams = this.transformContentFilterToMovieFilters(filters);
    this.logger.debug('[Movies] Transformed filters', filterParams);

    const params = this.buildDiscoverHttpParams(filterParams);
    this.logger.debug('[Movies] HTTP params', params.toString());

    return this.http.get<MovieList>(`${this.apiUrl}/movies/discover`, {params});
  }

  /**
   * Transform the given ContentFilters object to the MovieFilters
   * */
  private transformContentFilterToMovieFilters(filters: any): MovieFilters {
    this.logger.debug('[Movies] Transforming content filters', filters);

    return {
      page: filters.page || 1,
      genres: filters.genres,
      year: filters.year,
      includeAdult: filters.includeAdult !== undefined ? filters.includeAdult : false,
      yearFrom: filters.yearFrom,
      yearTo: filters.yearTo,
      language: filters.language || 'en',
      runtime: filters.runtime,
      castAndCrew: filters.castAndCrew,
      keyword: filters.keyword,
      sortBy: filters.sortBy || 'popularity.desc',
      certification: filters.certification,
      watchProviders: filters.watchProviders,
      region: filters.region || 'US',
    }
  }

  /**
   * Build HttpParams from a given HttpParams object for http request
   */
  private buildDiscoverHttpParams(filters: MovieFilters): HttpParams {
    this.logger.debug('[Movies] Building HTTP params from filters', filters);

    let params = new HttpParams();

    // Always add required parameters with defaults
    params = params.set('page', filters.page?.toString() || '1');

    // Only add optional parameters if they have valid values
    if (filters.sortBy) {
      params = params.set('sortBy', filters.sortBy);
    }

    if (filters.includeAdult !== undefined && filters.includeAdult !== null) {
      params = params.set('includeAdult', filters.includeAdult.toString());
    }

    if (filters.region) {
      params = params.set('region', filters.region);
    }

    // Add year filters
    if (filters.year) {
      params = params.set('year', filters.year.toString());
    } else {
      if (filters.yearFrom) {
        params = params.set('yearFrom', filters.yearFrom);
      }
      if (filters.yearTo) {
        params = params.set('yearTo', filters.yearTo);
      }
    }

    if (filters.watchProviders && filters.watchProviders.length > 0) {
      params = params.set('watchProviders', filters.watchProviders.join(','));
    }

    // Add genre filters
    if (filters.genres && filters.genres.length > 0) {
      params = params.set('genres', filters.genres.join(','));
    }

    // Add language filter
    if (filters.language) {
      params = params.set('language', filters.language);
    }

    // Add runtime filter
    if (filters.runtime) {
      params = params.set('runtime', filters.runtime);
    }

    // Add cast and crew filter
    if (filters.castAndCrew) {
      params = params.set('castAndCrew', filters.castAndCrew);
    }

    // Add keyword filter
    if (filters.keyword) {
      params = params.set('keyword', filters.keyword);
    }

    // Add certification filter
    if (filters.certification) {
      params = params.set('certification', filters.certification);
    }

    this.logger.debug('[Movies] Final HTTP params', params.toString());

    return params;
  }
}
