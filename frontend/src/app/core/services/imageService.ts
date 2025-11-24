import {Injectable} from '@angular/core';
import {environment} from '../../../environments/environment';


@Injectable({
  providedIn: 'root'
})
export class ImageService {
  private static imageBaseUrl = environment.tmdbImageBaseUrl;

  static getOptimizedImageUrl(imagePath: string, size: string = 'w92'): string {
    return `${ImageService.imageBaseUrl}/${size}${imagePath}`;
  }
}
