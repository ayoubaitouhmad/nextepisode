import {Component, EventEmitter, inject, Input, OnChanges, OnInit, Output, SimpleChanges} from '@angular/core';
import {CommonModule} from '@angular/common';
import {FormsModule} from '@angular/forms';
import {AutocompleteComponent} from '../../../../shared/components/auto-complete-component/AutocompleteComponent';
import {ContentFilters} from '../../../../core/models/tmdb/request/content-filters';
import {
  Certification,
  Genre,
  GenreList,
  Language,
  Person,
  PersonList,
  Region,
  WatchProvider
} from '../../../../core/models/common/shared-dtos';
import {_TmdbService} from '../../../../core/services/tmdb/_-tmdb.service';
import {RuntimeResponse} from '../../../../core/models/tmdb/runtime';
import {capitalizeFirstLetter} from '../../../../shared/utils';


@Component({
  selector: 'app-movie-filters',
  standalone: true,
  imports: [CommonModule, FormsModule, AutocompleteComponent],
  templateUrl: './content-filters.component.html',
  styleUrls: ['./content-filters.component.scss']
})
export class ContentFiltersComponent implements OnInit, OnChanges {
  private maxShowedStreamingServices = 15;
  private tmdbService: _TmdbService = inject(_TmdbService);


  @Input({required: true}) genreList: GenreList = {
    total: 0,
    stored_at: new Date(),
    genres: []
  };
  @Input() contentType: 'movie' | 'tv' = 'movie';
  @Output() filtersChange = new EventEmitter<ContentFilters>();
  @Output() onCountryCodeChange = new EventEmitter<string>();
  @Input() certifications: Certification[] = [];
  @Input() streamingServices: WatchProvider[] = [];

  filters: ContentFilters = {
    type: 'movie',
    genres: [],
    yearFrom: 1975,
    yearTo: 2025,
    language: 'en',
    runtime: 'Any',
    castAndCrew: '',
    keyword: '',
    lookFor: 'High Rated',
    ageFilter: "No Filter",
    streamingServices: [],
    country: 'US'
  };

  languages: Language[] = [];

  genres: Genre[] = [];

  regions: Region[] = [];
  people: PersonList = {
    page: 0,
    total_pages: 0,
    total_results: 0,
    results: []
  };

  runtimes: RuntimeResponse = {
    data: {} as any
  };

  sortOptions: {} = {};


  ngOnInit(): void {
    this.tmdbService.getLanguages().subscribe(languages => this.languages = languages.languages)
    this.tmdbService.getRegions().subscribe(regionList => this.regions = regionList.results);
    this.tmdbService.getSortOptions().subscribe(options => this.sortOptions = options);
    this.filters.type = this.contentType;
    this.emitFilters();


    this.tmdbService.getRuntimes().subscribe((value: RuntimeResponse) => {
      this.runtimes = value;
    });
  }

  ngOnChanges(changes: SimpleChanges): void {
    if (changes['genreList']) {
      const newGenreList = changes['genreList'].currentValue as GenreList;

      if (newGenreList && newGenreList.genres && newGenreList.genres.length > 0) {
        this.genres = newGenreList.genres;
      }
    }

    if (changes['contentType'] && !changes['contentType'].firstChange) {
      this.filters.type = this.contentType;
      this.filters.genres = [];
      this.emitFilters();
    }

    if (changes['certifications']) {
      this.certifications = changes['certifications'].currentValue as Certification[];
    }
  }

  getServiceName(serviceId: number): string {
    const service = this.streamingServices.find(s => s.provider_id === serviceId);
    return service ? service.provider_name : '';
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
    if (idx > -1) {
      this.filters.genres.splice(idx, 1);
    } else {
      this.filters.genres.push(genre.id);
    }
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
    this.filters.language = lang === 'xx' ? '' : lang;
    this.emitFilters();
  }

  onRuntimeChange(rt: string): void {
    this.filters.runtime = rt;
    console.log(rt)
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
    this.onCountryCodeChange.emit(countryCode);
    // this.tmdbService.getStreamingServices(countryCode).subscribe(s =>
    //   this.streamingServices = s.slice(0, this.maxShowedStreamingServices)
    // );
    this.emitFilters();
  }

  onStreamingServiceToggle(serviceId: number): void {
    const idx = this.filters.streamingServices.indexOf(serviceId);
    if (idx > -1) {
      this.filters.streamingServices.splice(idx, 1);
    } else {
      this.filters.streamingServices.push(serviceId);
    }
    this.emitFilters();
  }

  onSearch(text: string): void {
    console.log('Current input:', text);
    this.tmdbService.searchPerson(text).subscribe(list => this.people = list);
  }

  onSelected(item: Person): void {
    console.log('User selected:', item);
  }

  protected readonly capitalizeFirstLetter = capitalizeFirstLetter;
}
