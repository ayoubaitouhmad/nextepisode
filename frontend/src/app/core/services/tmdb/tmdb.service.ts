import {inject, Injectable} from '@angular/core';
import {HttpClient, HttpParams} from '@angular/common/http';
import {map, Observable} from 'rxjs';
import {environment} from '../../../../environments/environment';
import {Genre, Language, StreamingService} from '../../models/common/shared-dtos';


@Injectable({
  providedIn: 'root'
})
export class TMDBService {
  protected http = inject(HttpClient);
  protected apiKey = environment.tmdbApiKey;
  protected baseUrl = environment.tmdbBaseUrl;

  protected imageBaseUrl = environment.tmdbImageBaseUrl;

  constructor() {
  }


  public static readonly movieGenreList: Genre[] = [
    {id: 28, name: 'Action'},
    {id: 12, name: 'Adventure'},
    {id: 16, name: 'Animation'},
    {id: 35, name: 'Comedy'},
    {id: 80, name: 'Crime'},
    {id: 99, name: 'Documentary'},
    {id: 18, name: 'Drama'},
    {id: 10751, name: 'Family'},
    {id: 14, name: 'Fantasy'},
    {id: 36, name: 'History'},
    {id: 27, name: 'Horror'},
    {id: 10402, name: 'Music'},
    {id: 9648, name: 'Mystery'},
    {id: 10749, name: 'Romance'},
    {id: 878, name: 'Science Fiction'},
    {id: 10770, name: 'TV Movie'},
    {id: 53, name: 'Thriller'},
    {id: 10752, name: 'War'},
    {id: 37, name: 'Western'}
  ];

  public static readonly tvGenreList: Genre[] = [
    {id: 10759, name: 'Action & Adventure'},
    {id: 16, name: 'Animation'},
    {id: 35, name: 'Comedy'},
    {id: 80, name: 'Crime'},
    {id: 99, name: 'Documentary'},
    {id: 18, name: 'Drama'},
    {id: 10751, name: 'Family'},
    {id: 10762, name: 'Kids'},
    {id: 9648, name: 'Mystery'},
    {id: 10763, name: 'News'},
    {id: 10764, name: 'Reality'},
    {id: 10765, name: 'Sci-Fi & Fantasy'},
    {id: 10766, name: 'Soap'},
    {id: 10767, name: 'Talk'},
    {id: 10768, name: 'War & Politics'},
    {id: 37, name: 'Western'}
  ];


  findGenreById(id: number): Genre | undefined {


    const genre = TMDBService.movieGenreList.find(genre => genre.id == id);
    console.log(genre);

    if (genre) {
      return genre;
    } else {
      return undefined;
    }

  }

  /**
   * Get all genres
   */
  getMovieGenres(): Observable<Genre[]> {
    const params = new HttpParams().set('api_key', this.apiKey);
    return this.http.get<{ genres: Genre[] }>(`${this.baseUrl}/genre/movie/list`, {params})
      .pipe(map(response => response.genres));
  }

  /**
   * Get all genres
   */
  geTvGenres(): Observable<Genre[]> {
    const params = new HttpParams().set('api_key', this.apiKey);
    return this.http.get<{ genres: Genre[] }>(`${this.baseUrl}/genre/tv/list`, {params})
      .pipe(map(response => response.genres));
  }


  /**
   * Get all genres
   */
  getStreamingServices(watch_region: string = "US"): Observable<StreamingService[]> {
    const params = new HttpParams().set('api_key', this.apiKey).set('watch_region', watch_region);

    return this.http.get<{ results: any[] }>(`${this.baseUrl}/watch/providers/movie`, {params})
      .pipe(
        map(response =>
          response.results?.map(provider => ({
            id: provider.provider_id,
            name: provider.provider_name,
            logoUrl: provider.logo_path
              ? `${this.imageBaseUrl}/w45/${provider.logo_path}`
              : ''
          })) || []
        )
      );
  }


  getRegions(): Observable<any[]> {
    const params = new HttpParams().set('api_key', this.apiKey);

    return this.http.get<{ results: any[] }>(`${this.baseUrl}/watch/providers/regions`, {params})
      .pipe(
        map(response => response.results
        )
      );
  }

  getLanguages(): Observable<Language[]> {
    const params = new HttpParams().set('api_key', this.apiKey);

    return this.http.get<Language[]>(
      `${this.baseUrl}/configuration/languages`,
      {params}
    );
  }


  transformGenres(ids: number[]): Genre[] {
    let genres: Genre[] = [];

    ids.forEach(id => {
      TMDBService.movieGenreList.forEach(genre => {
        if (id == genre.id) {
          genres.push(genre);
        }
      })
    })

    return genres.map(genre => ({
      id: genre.id,
      name: genre.name
    }));
  }


  searchPerson(name: string): Observable<any[]> {
    return this.http
      .get<{ results: any[] }>(
        `${this.baseUrl}/search/person`,
        {
          params: new HttpParams()
            .set('api_key', this.apiKey)
            .set('query', name)
        }
      )
      .pipe(map(resp => resp.results));
  }


}
