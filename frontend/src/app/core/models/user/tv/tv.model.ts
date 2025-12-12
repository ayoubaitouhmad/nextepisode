import {Genre} from '../shared/shared-dtos';


export interface UserTvShow {
  id: number;
  name: string;
  created_at: string;      // when added to list
  watched_at?: string;     // only for watched movies
  release_date: string;
  poster_path: string;
  genres: Genre[];
}
export interface UserTvShowList {
  page: number;
  total_pages: number;
  results: UserTvShow[];
  total_results: number;      // when added to list
}
