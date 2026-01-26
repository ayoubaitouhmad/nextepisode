import {inject, Injectable} from '@angular/core';
import {environment} from '../../../../environments/environment';
import {HttpClient} from '@angular/common/http';
import {Logger, LoggerService} from '../logger.service';

// service.ts (base class)
@Injectable({providedIn: 'root'})
export class Service {
  protected baseServiceApiUrl = `${environment.apiUrl}/tmdb`;
  protected loggerService = inject(LoggerService);
  protected logger: Logger;

  constructor(protected http: HttpClient) {
    this.logger = this.loggerService.create('Service');
  }
}
