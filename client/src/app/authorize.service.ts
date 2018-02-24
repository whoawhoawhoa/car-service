import { Injectable } from '@angular/core';
import {Http, Response, Headers, RequestOptions} from '@angular/http';
import {Observable} from 'rxjs/Observable';
import {Client} from './client';
import {Worker} from './worker';

@Injectable()
export class AuthorizeService {
  checkUrl = 'http://localhost:9090/';
  constructor(private http: Http) { }

  checkAuthW(login: string, password: string): Observable<Worker> {
    const cpHeaders = new Headers({ 'Content-Type': 'application/json' });
    const newUrl = this.checkUrl + 'worker?login=' + login + '&password=' + password;
    const options = new RequestOptions({ headers: cpHeaders });
    return this.http.get(newUrl, options)
      .map(this.extractData)
      .catch(this.handleError);
  }

  checkAuthC(login: string, password: string): Observable<Client> {
    const cpHeaders = new Headers({ 'Content-Type': 'application/json' });
    const newUrl = this.checkUrl + 'client?login=' + login + '&password=' + password;
    const options = new RequestOptions({ headers: cpHeaders });
    return this.http.get(newUrl, options)
      .map(this.extractData)
      .catch(this.handleError);
  }

  private extractData(res: Response) {
    return res.json();
  }

  private handleError (error: Response | any) {
    console.error(error.message || error);
    return Observable.throw(error.status);
  }
}
