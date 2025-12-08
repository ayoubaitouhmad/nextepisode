import {Injectable} from '@angular/core';
import {environment} from '../../../../environments/environment';
import {HttpClient} from '@angular/common/http';

@Injectable({
  providedIn: 'root'
})
export class Service {

  protected baseServiceApiUrl = `${environment.apiUrl}/user`;

  constructor(protected http: HttpClient) {
  }



}
