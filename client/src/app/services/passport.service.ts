import { Injectable } from '@angular/core';
import {Http, Response, Headers, RequestOptions} from '@angular/http';
import {Observable} from 'rxjs/Observable';
import {Passport} from '../table-classes/passport';
import 'rxjs/add/observable/throw';

@Injectable()
export class PassportService {
  allPassportsUrl = 'http://localhost:9090/passports';
  passportUrl = 'http://localhost:9090/passport';
  defaultUrl = 'http://localhost:9090/';

  constructor(private http: Http) { }

  getAllPassports(): Observable<Passport[]> {
    return this.http.get(this.allPassportsUrl)
      .map(this.extractData)
      .catch(this.handleError);
  }

  createPassport(passport: Passport): Observable<number> {
    const cpHeaders = new Headers({ 'Content-Type': 'application/json' });
    const options = new RequestOptions({headers: cpHeaders});
    return this.http.post(this.passportUrl, passport, options)
      .map(success => success.status)
      .catch(this.handleError);
  }

  getPassportById(passportId: string): Observable<Passport> {
    const cpHeaders = new Headers({ 'Content-Type': 'application/json' });
    const cpParams = new URLSearchParams();
    cpParams.set('id', passportId);
    const options = new RequestOptions({ headers: cpHeaders, params: cpParams });
    return this.http.get(this.passportUrl, options)
      .map(this.extractData)
      .catch(this.handleError);
  }

  getPassportByWorkerLogin(workerLogin: string): Observable<Passport> {
    const cpHeaders = new Headers({ 'Content-Type': 'application/json' });
    const newUrl = this.defaultUrl + 'worker_passport?login=' + workerLogin;
    const options = new RequestOptions({ headers: cpHeaders });
    return this.http.get(newUrl, options)
      .map(this.extractData)
      .catch(this.handleError);
  }

  getPassportByWorkerStatus(workerStatus: string): Observable<Passport[]> {
    const cpHeaders = new Headers({ 'Content-Type': 'application/json' });
    const newUrl = this.defaultUrl + 'passports_to_admin?status=' + workerStatus;
    const options = new RequestOptions({ headers: cpHeaders });
    return this.http.get(newUrl, options)
      .map(this.extractData)
      .catch(this.handleError);
  }

  updatePassport(passport: Passport): Observable<number> {
    const cpHeaders = new Headers({ 'Content-Type': 'application/json' });
    const options = new RequestOptions({ headers: cpHeaders });
    return this.http.put(this.passportUrl, passport, options)
      .map(success => success.status)
      .catch(this.handleError);
  }

  deletePassportById(passportId: string): Observable<number> {
    return this.http.delete(this.passportUrl + '?id=' + passportId)
      .map(success => success.status)
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
