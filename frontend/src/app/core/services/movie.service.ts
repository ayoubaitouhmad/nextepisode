import {Injectable} from '@angular/core';
import {TMDBService} from './tmdb.service';
import {map, Observable} from 'rxjs';
import {HttpParams} from '@angular/common/http';
import {StreamingService} from '../models/common/shared-dtos';
import {TMDBMovie, TMDBMovieResponse, XMovie} from '../models/common/movie.model';

@Injectable({
  providedIn: 'root'
})
export class MovieService extends TMDBService {

  constructor() {
    super();
  }

  /**
   * Transform TMDB movie to our Movie interface
   */
  private transformMovie(tmdbMovie: TMDBMovie): XMovie {


    const movie: XMovie = {
      id: tmdbMovie.id.toString(),
      title: tmdbMovie.title,
      rating: Math.round(tmdbMovie.vote_average * 10) / 10,
      year: new Date(tmdbMovie.release_date).getFullYear(),
      genres: this.transformGenres(tmdbMovie.genre_ids),
      overview: tmdbMovie.overview,
      imageUrl: tmdbMovie.poster_path
        ? `${this.imageBaseUrl}/original${tmdbMovie.poster_path}`
        : 'assets/images/no-poster.jpg',
      backdropUrl: tmdbMovie.backdrop_path
        ? `${this.imageBaseUrl}/w1280${tmdbMovie.backdrop_path}`
        : '',
      releaseDate: tmdbMovie.release_date,
      voteCount: tmdbMovie.vote_count,
      originalLanguage: tmdbMovie.original_language,
      // Don't assign streamingService here
    };


    this.getStreamingProviders(tmdbMovie.id).subscribe(e => {

      movie.streamingService = {
        id: e[0]?.id,
        name: e[0]?.name,
        logoUrl: e[0]?.logoUrl,

      };
    })

    return movie;
  }


  /** Get subscription-based streaming providers for a movie in a given region */
  getStreamingProviders(
    movieId: number,
    region: string = 'US'
  ): Observable<StreamingService[]> {
    const params = new HttpParams()
      .set('api_key', this.apiKey);

    return this.http
      .get<{ id: number; results: Record<string, any> }>(
        `${this.baseUrl}/movie/${movieId}/watch/providers`,
        {params}
      )
      .pipe(
        // dive into results[region].flatrate or return empty array
        map(resp => {
          const entry = resp.results[region];
          if (entry && Array.isArray(entry.flatrate)) {
            return entry.flatrate.map((p: any) => ({
              id: p.provider_id,
              name: p.provider_name,
              logoUrl: p.logo_path
                ? `${this.imageBaseUrl}/original${p.logo_path}`
                : ''
            }));
          }
          return [];
        })
      );
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
  } = {}): Observable<{ movies: XMovie[], totalPages: number, totalResults: number }> {

    let params = new HttpParams()
      .set('api_key', this.apiKey)
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


    return this.http.get<TMDBMovieResponse>(`${this.baseUrl}/discover/movie`, {params})
      .pipe(
        map(response => ({

          movies: response.results.map(movie => this.transformMovie(movie)),
          totalPages: response.total_pages,
          totalResults: response.total_results
        }))
      );
  }


  /**
   * Get movies ordered by year (release date)
   * @param year - Filter by specific year (optional)
   * @param page - Page number (default: 1)
   * @param sortOrder - 'asc' or 'desc' (default: 'desc')
   */
  getMoviesByYear(year?: number, page = 1, sortOrder: 'asc' | 'desc' = 'desc'): Observable<TMDBMovieResponse> {
    const params = new HttpParams()
      .set('api_key', this.apiKey)
      .set('page', page.toString())
      .set('sort_by', `release_date.${sortOrder}`)
      .set('vote_count.gte', '100') // Filter out movies with very few votes
      .set('include_adult', 'false');

    // If specific year is provided, add date range
    let finalParams = params;
    if (year) {
      finalParams = finalParams
        .set('primary_release_date.gte', `${year}-01-01`)
        .set('primary_release_date.lte', `${year}-12-31`);
    }

    return this.http.get<TMDBMovieResponse>(`${this.baseUrl}/discover/movie`, {params: finalParams});
  }

  /**
   * Get popular movies ordered by year
   */
  getPopularMoviesByYear(page = 1): Observable<XMovie[]> {
    const params = new HttpParams()
      .set('api_key', this.apiKey)
      .set('page', page.toString())
      .set('sort_by', 'popularity.desc')
      .set('vote_count.gte', '500')
      .set('include_adult', 'false');

    return this.http.get<TMDBMovieResponse>(`${this.baseUrl}/discover/movie`, {params})
      .pipe(
        map(response => response.results
          .sort((a, b) => new Date(b.release_date).getTime() - new Date(a.release_date).getTime())
          .map(movie => this.transformMovie(movie))
        )
      );
  }


  /**
   * Search movies by keyword
   */
  searchMovies(query: string, page = 1): Observable<XMovie[]> {
    const params = new HttpParams()
      .set('api_key', this.apiKey)
      .set('query', query)
      .set('page', page.toString())
      .set('include_adult', 'false');

    return this.http.get<TMDBMovieResponse>(`${this.baseUrl}/search/movie`, {params})
      .pipe(
        map(response => response.results
          .sort((a, b) => new Date(b.release_date).getTime() - new Date(a.release_date).getTime())
          .map(movie => this.transformMovie(movie))
        )
      );
  }

  /**
   * Get movie details by ID
   */
  getMovieDetails(movieId: number): Observable<any> {
    const params = new HttpParams()
      .set('api_key', this.apiKey)
      .set('append_to_response', 'credits,videos,similar');

    return this.http.get<any>(`${this.baseUrl}/movie/${movieId}`, {params});
  }


  getNowPlaying(page = 1): Observable<TMDBMovieResponse> {
    const params = new HttpParams()
      .set('api_key', this.apiKey)
      .set('page', page);
    return this.http.get<TMDBMovieResponse>(`${this.baseUrl}/movie/now_playing`, {params});
  }

  // Populaires
  getPopular(page = 1): Observable<TMDBMovieResponse> {
    const params = new HttpParams()
      .set('api_key', this.apiKey)
      .set('page', page);
    return this.http.get<TMDBMovieResponse>(`${this.baseUrl}/movie/popular`, {params});
  }

  // Mieux notés
  getTopRated(page = 1): Observable<TMDBMovieResponse> {
    const params = new HttpParams()
      .set('api_key', this.apiKey)
      .set('page', page);
    return this.http.get<TMDBMovieResponse>(`${this.baseUrl}/movie/top_rated`, {params});
  }

  // Tendance du jour (ou de la semaine)
  getTrending(window: 'day' | 'week' = 'day'): Observable<TMDBMovieResponse> {
    const params = new HttpParams()
      .set('api_key', this.apiKey)
    ;
    return this.http.get<TMDBMovieResponse>(`${this.baseUrl}/trending/movie/${window}`, {params});
  }

  // "Awarded" (proxy) = top rated avec un seuil minimum de votes pour éviter les niches
  getAwarded(opts?: {
    minVotes?: number,
    watchRegion?: string,        // ex. 'US'
    withProviders?: string,      // ex. '8,337'
    page?: number
  }): Observable<TMDBMovieResponse> {
    const params = new HttpParams()
      .set('api_key', this.apiKey)
      .set('sort_by', 'vote_average.desc')
      .set('vote_count.gte', String(opts?.minVotes ?? 1500))
      .set('page', String(opts?.page ?? 1))
      // filtre optionnel par pays/fournisseurs
      .append('watch_region', opts?.watchRegion ?? '')
      .append('with_watch_providers', opts?.withProviders ?? '');
    return this.http.get<TMDBMovieResponse>(`${this.baseUrl}/discover/movie`, {params});
  }


  getWatchProviders(id: number) {
    const params = new HttpParams().set('api_key', this.apiKey);
    return this.http.get<{ id: number; results: Record<string, any> }>(
      `${this.baseUrl}/movie/${id}/watch/providers`, {params}
    );
  }


}
