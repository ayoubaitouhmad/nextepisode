import {Injectable} from '@angular/core';

interface CacheItem<T> {
  data: T;
  timestamp: number;
}

@Injectable({
  providedIn: 'root'
})
export class LocalStorageCacheService {
  private readonly CACHE_DURATION = 24 * 60 * 60 * 1000; // 24 hours

  set<T>(key: string, data: T): void {
    const cacheItem: CacheItem<T> = {
      data,
      timestamp: Date.now()
    };
    localStorage.setItem(key, JSON.stringify(cacheItem));
  }

  get<T>(key: string): T | null {
    const item = localStorage.getItem(key);
    if (!item) return null;

    try {
      const cacheItem: CacheItem<T> = JSON.parse(item);

      if (this.isCacheValid(cacheItem.timestamp)) {
        console.info(`Getting cache data for ${key}`);
        return cacheItem.data;
      } else {
        this.remove(key);
        return null;
      }
    } catch (error) {
      this.remove(key);
      return null;
    }
  }

  remove(key: string): void {
    localStorage.removeItem(key);
  }

  clear(): void {
    localStorage.clear();
  }

  private isCacheValid(timestamp: number): boolean {
    return Date.now() - timestamp < this.CACHE_DURATION;
  }
}
