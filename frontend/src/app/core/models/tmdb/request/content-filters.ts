export interface ContentFilters {
  type: 'movie' | 'tv';
  genres: number[];
  year: number;
  includeAdult: boolean;
  yearFrom?: number;
  yearTo?: number;
  language: string;
  runtime: string;
  castAndCrew: string;
  keyword: string;
  sortBy: string;
  certification: string;
  watchProviders: number[];
  region: string;
}

