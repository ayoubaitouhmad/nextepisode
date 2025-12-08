export interface Genre {
  id: number;
  name: string;
}

export interface UserMovie {
  id: number;
  title: string;
  created_at: string;      // when added to list
  watched_at?: string;     // only for watched movies
  release_date: string;
  poster_path: string;
  genres: Genre[];
}

export interface UserMovieList {
  page: number;
  totalPages: number;
  results: UserMovie[];
  totalResults: number;      // when added to list
}

export function getUserMovieGenreAsString(genres: Genre[]) {
  if (genres.length < 0) {
    return "";
  }
  return genres.map(genre => genre.name).join(", ");
}
