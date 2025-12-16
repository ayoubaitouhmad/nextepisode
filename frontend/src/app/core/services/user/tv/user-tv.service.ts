import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {Service} from '../service';
import {Observable} from 'rxjs';
import {UserMoviesAndTvShowStats} from '../../../models/common/shared-dtos';
import {UserTvShowList} from '../../../models/common/tv.model';

@Injectable({
  providedIn: 'root'
})
export class UserTvService extends Service {
  protected apiUrl: string;

  constructor(http: HttpClient) {
    super(http);
    this.apiUrl = `${this.baseServiceApiUrl}/tvs`;
  }


  /**
   * Get user's tv shows statistics (counts by category)
   */
  getUserTvShowsStatistics(): Observable<UserMoviesAndTvShowStats> {
    return this.http.get<UserMoviesAndTvShowStats>(`${this.apiUrl}/stats`);
  }

  /**
   * Get user's tv shows statistics (counts by category)
   */
  getUserFavoriteTvShows(): Observable<UserTvShowList> {
    return this.http.get<UserTvShowList>(`${this.apiUrl}/favorite`);
  }

  /**
   * Get user's tv shows statistics (counts by category)
   */
  getUserWatchedTvShows(): Observable<UserTvShowList> {
    return this.http.get<UserTvShowList>(`${this.apiUrl}/watched`);
  }

  /**
   * Get user's tv shows statistics (counts by category)
   */
  getUserInWatchListTvShows(): Observable<UserTvShowList> {
    return this.http.get<UserTvShowList>(`${this.apiUrl}/watchlist`);
  }


}
