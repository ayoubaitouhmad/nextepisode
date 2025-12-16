import {Component, inject, OnInit} from '@angular/core';
import {MovieService} from '../../../../core/services/tmdb/movie.service';
import {TvSeriesService} from '../../../../core/services/tmdb/tv-series.service';
import {forkJoin} from 'rxjs';
import {DecimalPipe, NgForOf, NgIf} from '@angular/common';
import {Router} from '@angular/router';
import {XMovie} from '../../../../core/models/common/movie.model';
import {TvSeries} from '../../../../core/models/common/tv.model';

type Section = { id: string; title: string; items: any[]; kind: 'movie' | 'tv' };


@Component({
  selector: 'app-browse',
  imports: [
    DecimalPipe,
    NgForOf,
    NgIf
  ],
  templateUrl: './browse.component.html',
  styleUrl: './browse.component.scss'
})
export class BrowseComponent implements OnInit {

  private movieService = inject(MovieService);
  private tv = inject(TvSeriesService);
  loading = true;
  sections: Section[] = [];


  constructor(private router: Router) {
  }

  ngOnInit(): void {
    forkJoin({
      // New
      newMovies: this.movieService.getNowPlaying(),
      newTV: this.tv.getOnTheAir(),
      // Popular
      popularMovies: this.movieService.getPopular(),
      popularTV: this.tv.getPopular(),
      // Trending (Today)
      trendingMovies: this.movieService.getTrending('day'),
      trendingTV: this.tv.getTrending('day'),
      // Top Rated (proxy "Awarded")
      topMovies: this.movieService.getTopRated(),
      topTV: this.tv.getTopRated(),
      awardedMovies: this.movieService.getAwarded({minVotes: 1500}),
      awardedTV: this.tv.getAwarded({minVotes: 800}),
    }).subscribe(res => {
      this.sections = [
        {id: 'new-m', title: 'Nouveaux films', kind: 'movie', items: res.newMovies.results},
        {id: 'new-t', title: 'Nouvelles séries', kind: 'tv', items: res.newTV.results},
        {id: 'pop-m', title: 'Films les plus populaires', kind: 'movie', items: res.popularMovies.results},
        {id: 'pop-t', title: 'Séries les plus populaires', kind: 'tv', items: res.popularTV.results},
        {id: 'trend-m', title: 'Films tendance (aujourd’hui)', kind: 'movie', items: res.trendingMovies.results},
        {id: 'trend-t', title: 'Séries tendance (aujourd’hui)', kind: 'tv', items: res.trendingTV.results},
        {id: 'top-m', title: 'Films les mieux notés', kind: 'movie', items: res.topMovies.results},
        {id: 'top-t', title: 'Séries les mieux notées', kind: 'tv', items: res.topTV.results},
        {id: 'award-m', title: 'Films primés', kind: 'movie', items: res.awardedMovies.results},
        {id: 'award-t', title: 'Séries primées', kind: 'tv', items: res.awardedTV.results},

      ];
      this.loading = false;
    });
  }

  titleOf(i: any) {
    return i.title ?? i.name ?? '';
  }

  img(path: string | null) {
    // tu as déjà imageBaseUrl dans TMDBService ; si tu exposes un helper, utilise-le
    return path ? `https://image.tmdb.org/t/p/w780${path}` : 'assets/images/no-poster.jpg';
  }

  scrollRow(id: string, dir: 1 | -1) {
    const el = document.getElementById(id);
    if (el) el.scrollBy({left: dir * 900, behavior: 'smooth'});
  }

  onOpenDetails(item: XMovie | TvSeries): void {

    const id = 'id' in item ? item.id : '';
    const isTv = (item as any).origin_country != undefined;
    this.router.navigate([isTv ? '/tv' : '/movie', id]);
  }
}
