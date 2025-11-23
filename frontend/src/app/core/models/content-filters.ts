export interface ContentFilters {
  type: 'movie' | 'tv';
  genres: number[];
  yearFrom: number;
  yearTo: number;
  language: string;
  runtime: string;
  castAndCrew: string;
  keyword: string;
  lookFor: string;
  ageFilter: string;
  streamingServices: number[];
  country: string;
}

