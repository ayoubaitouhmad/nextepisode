import {Injectable} from '@angular/core';
import {TMDBService} from './tmdb.service';
import {map, Observable} from 'rxjs';
import {HttpParams} from '@angular/common/http';
import {StreamingService} from '../../models/common/shared-dtos';
import {TMDBTvSeries, TMDBTvSeriesResponse, TvSeries} from '../../models/common/tv.model';

@Injectable({
  providedIn: 'root'
})
export class TvSeriesService extends TMDBService {

  constructor() {
    super();
  }

  /**
   * Discover TV series with advanced filters
   */
  discoverSeries(filters: {
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
  } = {}): Observable<{ series: TvSeries[], totalPages: number, totalResults: number }> {

    let params = new HttpParams()
      .set('api_key', this.apiKey)
      .set('page', filters.page || 1)
      .set('sort_by', filters.sortBy || 'vote_average.desc')
      .set('vote_count.gte', '50')
      .set('include_adult', (filters.includeAdult || false).toString())
      .set("watch_region", filters.watch_region || "US");

    // Add year filters - for TV shows, we use first_air_date
    if (filters.year) {
      params = params
        .set('first_air_date_year', filters.year.toString());
    } else {
      if (filters.yearFrom) {
        params = params.set('first_air_date.gte', `${filters.yearFrom}-01-01`);
      }
      if (filters.yearTo) {
        params = params.set('first_air_date.lte', `${filters.yearTo}-12-31`);
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

    return this.http.get<TMDBTvSeriesResponse>(`${this.baseUrl}/discover/tv`, {params})
      .pipe(
        map(response => ({

          series: response.results.map(series => this.transformSeries(series)),
          totalPages: response.total_pages,
          totalResults: response.total_results
        }))
      );
  }

  /**
   * Transform TMDB series to our Series interface
   */
  private transformSeries(tmdbSeries: TMDBTvSeries): TvSeries {

    const series: TvSeries = {
      id: tmdbSeries.id,
      title: tmdbSeries.name,  // Using name as title for consistency
      rating: Math.round(tmdbSeries.vote_average * 10) / 10,
      year: tmdbSeries.first_air_date ? new Date(tmdbSeries.first_air_date).getFullYear() : 0,
      genres: this.transformGenres(tmdbSeries.genre_ids),
      overview: tmdbSeries.overview,
      poster_path: tmdbSeries.poster_path,
      backdrop_path: tmdbSeries.backdrop_path,
      releaseDate: tmdbSeries.first_air_date,
      voteCount: tmdbSeries.vote_count,
      originalLanguage: tmdbSeries.original_language,
      originCountry: tmdbSeries.origin_country || []
    };

    // Get streaming providers for the series
    this.getSeriesStreamingProviders(tmdbSeries.id).subscribe(providers => {
      if (providers && providers.length > 0) {
        series.streamingService = {
          id: providers[0]?.id,
          name: providers[0]?.name,
          logoUrl: providers[0]?.logoUrl,
        };
      }
    });

    return series;
  }

  /**
   * Get subscription-based streaming providers for a TV series in a given region
   */
  getSeriesStreamingProviders(
    seriesId: number,
    region: string = 'US'
  ): Observable<StreamingService[]> {
    const params = new HttpParams()
      .set('api_key', this.apiKey);

    return this.http
      .get<{ id: number; results: Record<string, any> }>(
        `${this.baseUrl}/tv/${seriesId}/watch/providers`,
        {params}
      )
      .pipe(
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

  getOnTheAir(page = 1): Observable<TMDBTvSeriesResponse> {
    const params = new HttpParams()
      .set('api_key', this.apiKey)
      .set('page', page);
    return this.http.get<TMDBTvSeriesResponse>(`${this.baseUrl}/tv/on_the_air`, {params});
  }

  getPopular(page = 1): Observable<TMDBTvSeriesResponse> {
    const params = new HttpParams().set('api_key', this.apiKey).set('page', page);
    return this.http.get<TMDBTvSeriesResponse>(`${this.baseUrl}/tv/popular`, {params});
  }

  getTopRated(page = 1): Observable<TMDBTvSeriesResponse> {
    const params = new HttpParams().set('api_key', this.apiKey).set('page', page);
    return this.http.get<TMDBTvSeriesResponse>(`${this.baseUrl}/tv/top_rated`, {params});
  }

  getTrending(window: 'day' | 'week' = 'day'): Observable<TMDBTvSeriesResponse> {
    const params = new HttpParams().set('api_key', this.apiKey);
    return this.http.get<TMDBTvSeriesResponse>(`${this.baseUrl}/trending/tv/${window}`, {params});
  }

  getAwarded(opts?: { minVotes?: number, watchRegion?: string, withProviders?: string, page?: number }):
    Observable<TMDBTvSeriesResponse> {
    const params = new HttpParams()
      .set('api_key', this.apiKey)
      .set('sort_by', 'vote_average.desc')
      .set('vote_count.gte', String(opts?.minVotes ?? 800))
      .set('page', String(opts?.page ?? 1))
      .append('watch_region', opts?.watchRegion ?? '')
      .append('with_watch_providers', opts?.withProviders ?? '');
    return this.http.get<TMDBTvSeriesResponse>(`${this.baseUrl}/discover/tv`, {params});
  }

}
