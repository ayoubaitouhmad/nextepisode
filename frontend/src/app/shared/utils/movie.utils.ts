import {XMovie} from '../../core/models/common/movie.model';
import {WatchProvider} from '../../core/models/common/shared-dtos';

const MIN_PROVIDER_SIZE = 0;
const MAX_PROVIDER_SIZE = 2;

export function getFirstTwoFlatRate(movie: XMovie): WatchProvider[] | undefined {
  if (movie && movie.watch_providers?.flatrate && movie.watch_providers?.flatrate.length > 0) {
    return movie.watch_providers.flatrate.slice(MIN_PROVIDER_SIZE, MAX_PROVIDER_SIZE);
  }
  return undefined
}
