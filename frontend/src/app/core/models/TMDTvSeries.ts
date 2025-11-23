import {TMDBGenre} from './TMDBGenre';
import {StreamingService} from './streaming-service';

export interface TMDBTvSeries {
  id: number;
  name: string;  // TV shows use 'name' instead of 'title'
  original_name: string;
  overview: string;
  poster_path: string | null;
  backdrop_path: string | null;
  first_air_date: string;  // Instead of release_date
  vote_average: number;
  vote_count: number;
  genre_ids: number[];
  origin_country: string[];
  original_language: string;
  popularity: number;
}

export interface TMDBTvSeriesResponse {
  page: number;
  results: TMDBTvSeries[];
  total_pages: number;
  total_results: number;
}

export interface TvSeries {
  id: string;
  title: string;  // We'll use 'title' for consistency with Movie interface
  rating: number;
  year: number;
  genres: TMDBGenre[];
  overview: string;
  imageUrl: string;
  backdropUrl: string;
  releaseDate: string;
  voteCount: number;
  originalLanguage: string;
  originCountry: string[];
  streamingService?: StreamingService;
}
