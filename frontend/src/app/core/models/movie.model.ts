import {StreamingService} from './streaming-service';
import {TMDBGenre} from './TMDBGenre';


export interface Movie {
  id: string;
  title: string;
  rating: number;
  year: number;
  genres: TMDBGenre[];
  overview: string;
  imageUrl: string;
  backdropUrl: string;
  releaseDate: string;
  voteCount: number;
  originalLanguage: string;
  streamingService?: StreamingService;
}


export interface Language {
  english_name: string;
  iso_639_1: string;
  name: string;
}
