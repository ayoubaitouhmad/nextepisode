import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {Service} from './service';
import {Observable} from 'rxjs';
import {GenreList, LanguageList, RegionList, WatchProviderList} from '../../models/common/shared-dtos';


@Injectable({
  providedIn: 'root'
})
export class _TmdbService extends Service {

  protected apiUrl: string;

  constructor(http: HttpClient) {
    super(http);
    this.apiUrl = `${this.baseServiceApiUrl}`;
  }


  /**
   * Get current user profile
   */
  getMoviesGenres(): Observable<GenreList> {
    return this.http.get<GenreList>(`${this.apiUrl}/genres/movie-genres`);
  }

  /**
   * Get current user profile
   */
  getTvShowsGenres(): Observable<GenreList> {
    return this.http.get<GenreList>(`${this.apiUrl}/genres/tv-show-genres`);
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


}
