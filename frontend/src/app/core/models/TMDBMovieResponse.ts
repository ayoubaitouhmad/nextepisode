import {TMDBMovie} from "./TMDBMovie";

export interface TMDBMovieResponse {
  page: number;
  results: TMDBMovie[];
  total_pages: number;
  total_results: number;
}
