export interface Genre {
  id: number;
  name: string;
}

export interface GenreList {
  total: number;
  stored_at: Date;
  genres: Genre[];
}

export interface UserMoviesAndTvShowStats {
  favoriteCount: number;
  watchedCount: number;
  watchlistCount: number;
}


export function getUserMovieGenreAsString(genres: Genre[]) {
  if (genres.length < 0) {
    return "";
  }
  return genres.map(genre => genre.name).join(", ");
}


export interface TitleStatus {
  isFavorite: boolean;
  isWatched: boolean;
  isInWatchlist: boolean;
}


export interface UserTitleRequest {
  tmdbId: number;
  category: 'FAVORITE' | 'WATCHED' | 'WATCHLIST';
  action: 'ADD' | 'REMOVE';
}


export interface Language {
  english_name: string;
  iso_639_1: string;
  name: string;
}

export interface LanguageList {
  total: number;
  stored_at: Date;
  languages: Language[];
}


export interface StreamingService {
  id: number;
  name: string;
  logoUrl: string;
}

export interface Region {
  iso: string,
  iso_3166_1: string,
  english_name: string,
  native_name: string
}

export interface RegionList {
  results: Region[]
}

export interface WatchProvider {
  provider_id: number;
  provider_name: string;
  logo_path: string;
  display_priority: number;
  display_priorities: []
}

export interface WatchProviderList {
  total: number;
  results: WatchProvider[]
}

export interface WatchProviderCountry {
  buy: WatchProvider[];
  rent: WatchProvider[];
  flatrate: WatchProvider[]
}

export interface Person {
  adult: boolean;
  gender: string;
  name: string;
  original_name: string;
  known_for_department: string;
  popularity: number;
  profile_path: string;
}

export interface PersonList {
  page: number;
  total_pages: number;
  total_results: number;
  results: Person[]
}

export interface Certification {
  certification: string,
  meaning: string,
  order: string,
}

export interface CertificationList {
  certification: Certification[]
}

export type SortOptionKey =
  | 'VOTE_COUNT_DESC'
  | 'VOTE_AVERAGE_ASC'
  | 'POPULARITY_ASC'
  | 'PRIMARY_RELEASE_DATE_DESC'
  | 'TITLE_DESC'
  | 'REVENUE_ASC'
  | 'ORIGINAL_TITLE_DESC'
  | 'PRIMARY_RELEASE_DATE_ASC'
  | 'VOTE_COUNT_ASC'
  | 'TITLE_ASC'
  | 'ORIGINAL_TITLE_ASC'
  | 'VOTE_AVERAGE_DESC'
  | 'POPULARITY_DESC'
  | 'REVENUE_DESC';

export type SortOptionsDto = Record<SortOptionKey, string>;
