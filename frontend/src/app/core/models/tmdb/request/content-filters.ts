export interface ContentFilters {
  type: 'movie' | 'tv';
  // Optional filters that can be undefined
  includeAdult?: boolean;
  genres?: number[];
  watchProviders?: number[];
  year?: number;
  yearFrom?: number;
  yearTo?: number;
  language?: string;
  runtime?: string;
  castAndCrew?: string;
  keyword?: string;
  sortBy?: string;
  certification?: string;
  region?: string;
}

export interface MovieFilters {
  page?: number;
  genres?: number[];
  year?: number;
  includeAdult?: boolean;
  yearFrom?: number;
  yearTo?: number;
  language?: string;
  runtime?: string;
  castAndCrew?: string;
  keyword?: string;
  sortBy?: string;
  certification?: string;
  watchProviders?: number[];
  region?: string;
}
