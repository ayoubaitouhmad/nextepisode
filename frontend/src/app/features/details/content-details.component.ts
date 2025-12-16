import {Component, inject} from '@angular/core';
import {CommonModule} from '@angular/common';
import {ActivatedRoute} from '@angular/router';
import {HttpClient, HttpParams} from '@angular/common/http';
import {environment} from '../../../environments/environment';
import {SafeUrlPipe} from '../../shared/pipes/safe-url.pipe';
import {MovieService} from '../../core/services/tmdb/movie.service';
import {FormsModule, ReactiveFormsModule} from '@angular/forms';


interface rESponse {
  "logo_path": String,
  "provider_id": String,
  "provider_name": String,
  "display_priority": String
}


@Component({
  selector: 'app-content-details',
  standalone: true,
  imports: [CommonModule, SafeUrlPipe, ReactiveFormsModule, FormsModule],
  templateUrl: './content-details.component.html',
  styleUrl: './content-details.component.scss'
})
export class ContentDetailsComponent {
  private route = inject(ActivatedRoute);
  private http = inject(HttpClient);
  private movieService = inject(MovieService);

  region: string = "US";
  regions: any[] = [];
  type: 'movie' | 'tv' = 'movie';
  id = '';
  data: any = null;
  trailerKey: string | null = null;
  // expose environment to template
  env = environment;
  providers: rESponse[] = [];
  tmdbWatchLink = undefined;

  get year(): string {
    const d: string | undefined = this.data?.release_date || this.data?.first_air_date;
    if (!d) {
      return '';
    }
    const y = new Date(d).getFullYear();
    return isNaN(y) ? '' : String(y);
  }

  get genresList(): string {
    const arr: any[] = this.data?.genres || [];
    return arr.map(g => g?.name).filter(Boolean).join(', ');
  }

  ngOnInit() {
    this.route.url.subscribe(segments => {
      this.type = (segments[0]?.path as 'movie' | 'tv') || 'movie';
      this.id = this.route.snapshot.params['id'];
      this.loadDetails();
    });

    this.loadWatchProviders()

    // this.tmdbService.getRegions().subscribe(r => this.regions = r);

  }


  loadWatchProviders() {
    this.movieService.getWatchProviders(parseInt(this.id)).subscribe(resp => {
      const r = resp.results?.[this.region] || {};
      this.tmdbWatchLink = r?.link || null;
      this.providers = r?.flatrate ?? r?.free ?? r?.ads ?? r?.rent ?? r?.buy ?? [];
    });
  }


  private loadDetails() {
    const params = new HttpParams().set('api_key', environment.tmdbApiKey).set('append_to_response', 'videos');
    this.http.get(`${environment.tmdbBaseUrl}/${this.type}/${this.id}`, {params}).subscribe((resp: any) => {
      this.data = resp;
      const videos = resp?.videos?.results || [];
      const trailer = videos.find((v: any) => v.site === 'YouTube' && v.type === 'Trailer')
        || videos.find((v: any) => v.site === 'YouTube');
      this.trailerKey = trailer ? trailer.key : null;
    });
  }

  onCountryChange(countryCode: string): void {
    this.region = countryCode;
    this.loadWatchProviders();
  }

}


