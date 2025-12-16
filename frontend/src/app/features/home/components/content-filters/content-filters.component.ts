import {Component, EventEmitter, inject, OnInit, Output} from '@angular/core';
import {CommonModule} from '@angular/common';
import {FormsModule} from '@angular/forms';
import {TMDBService} from '../../../../core/services/tmdb/tmdb.service';
import {ContentFilters} from '../../../../core/models/tmdb/request/content-filters';
import {AutocompleteComponent} from '../../../../shared/components/auto-complete-component/AutocompleteComponent';
import {Genre, Language} from '../../../../core/models/common/shared-dtos';


@Component({
  selector: 'app-movie-filters',
  standalone: true,
  imports: [CommonModule, FormsModule, AutocompleteComponent],
  templateUrl: './content-filters.component.html',
  styleUrls: ['./content-filters.component.scss']
})
export class ContentFiltersComponent implements OnInit {
  private tmdbService = inject(TMDBService);
  private maxShowedStreamingServices = 15;
  @Output() filtersChange = new EventEmitter<ContentFilters>();


  filters: ContentFilters = {
    type: 'movie',
    genres: [],
    yearFrom: 1975,
    yearTo: 2025,
    language: 'Any',
    runtime: 'Any',
    castAndCrew: '',
    keyword: '',
    lookFor: 'High Rated',
    ageFilter: 'No Filter',
    streamingServices: [],
    country: 'US'
  };

  languages: Language[] = [];
  genres: Genre[] = [];
  streamingServices: any[] = [];
  regions: any[] = [];
  persones: string[] = [];
  tvGenres: Genre[] = [];

  ngOnInit(): void {
    this.tmdbService.getMovieGenres().subscribe(g => this.genres = g);
    this.tmdbService.geTvGenres().subscribe(tvGenres => {
      // Store TV genres for later use
      this.tvGenres = tvGenres;
    });
    this.tmdbService.getRegions().subscribe(r => this.regions = r);
    this.tmdbService.getStreamingServices(this.filters.country).subscribe(s => this.streamingServices = s.slice(0, this.maxShowedStreamingServices));
    this.tmdbService.getLanguages().subscribe(l => this.languages = l);
    this.emitFilters();
  }

  getServiceName(serviceId: number): string {
    const service = this.streamingServices.find(s => s.id === serviceId);
    return service ? service.name : '';
  }

  clearStreamingServices(): void {
    this.filters.streamingServices = [];
    this.emitFilters();
  }


  private emitFilters(): void {
    this.filtersChange.emit({...this.filters});
  }


  onGenreToggle(genre: Genre): void {
    const idx = this.filters.genres.indexOf(genre.id);
    if (idx > -1) this.filters.genres.splice(idx, 1);
    else this.filters.genres.push(genre.id);
    this.emitFilters();
  }

  onYearFromChange(year: number): void {
    this.filters.yearFrom = year;
    this.emitFilters();
  }

  onYearToChange(year: number): void {
    this.filters.yearTo = year;
    this.emitFilters();
  }

  onLanguageChange(lang: string): void {
    if (lang == "xx") {
      lang = "";
    }
    this.filters.language = lang;
    this.emitFilters();
  }

  onRuntimeChange(rt: string): void {
    this.filters.runtime = rt;
    this.emitFilters();
  }

  onCastAndCrewChange(text: string): void {
    this.filters.castAndCrew = text;
    this.emitFilters();
  }

  onKeywordChange(text: string): void {
    this.filters.keyword = text;
    this.emitFilters();
  }

  onLookForChange(value: string): void {
    this.filters.lookFor = value;
    this.emitFilters();
  }

  onAgeFilterChange(value: string): void {
    this.filters.ageFilter = value;
    this.emitFilters();
  }

  onCountryChange(countryCode: string): void {
    this.filters.country = countryCode;
    this.tmdbService.getStreamingServices(countryCode).subscribe(s => this.streamingServices = s.slice(0, this.maxShowedStreamingServices));
    this.emitFilters();
  }

  onStreamingServiceToggle(serviceId: number): void {
    const idx = this.filters.streamingServices.indexOf(serviceId);
    if (idx > -1) this.filters.streamingServices.splice(idx, 1);
    else this.filters.streamingServices.push(serviceId);
    this.emitFilters();
  }

  toggleSelection() {
    this.filters.type = this.filters.type === 'movie' ? 'tv' : 'movie';
    if (this.filters.type === 'movie') {
      this.genres = TMDBService.movieGenreList;
    } else {
      this.genres = this.tvGenres;
    }
    this.emitFilters();
  }

  onSearch(text: string) {
    console.log('Current input:', text);

    this.tmdbService.searchPerson(text).subscribe(p => this.persones = p.map((p) => p.name));
  }

  onSelected(item: string) {
    console.log('User selected:', item);
  }

}
