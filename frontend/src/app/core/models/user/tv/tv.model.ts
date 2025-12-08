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
  totalPages: number;
  results: UserTvShow[];
  totalResults: number;      // when added to list
}
