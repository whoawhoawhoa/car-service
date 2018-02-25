import { Injectable } from '@angular/core';
import {Http, Response, Headers, RequestOptions} from '@angular/http';
import {Observable} from 'rxjs/Observable';
import 'rxjs/add/operator/map';
import 'rxjs/add/operator/catch';
import {Worker} from './worker';

@Injectable()
export class WorkerService {
  allWorkersUrl = 'http://localhost:9090/all_workers';
  workerUrl = 'http://localhost:9090/worker';

  constructor(private http: Http) { }

  getAllWorkers(): Observable<Worker[]> {
    return this.http.get(this.allWorkersUrl)
      .map(this.extractData)
      .catch(this.handleError);
  }

  createWorker(worker: Worker): Observable<number> {
    const cpHeaders = new Headers({ 'Content-Type': 'application/json' });
    const options = new RequestOptions({headers: cpHeaders});
    return this.http.post(this.workerUrl, worker, options)
      .map(success => success.status)
      .catch(this.handleError);
  }

  getWorkerByLoginAndPassword(workerLogin: string, workerPassword: string): Observable<Worker> {
    const cpHeaders = new Headers({ 'Content-Type': 'application/json' });
    const cpParams = new URLSearchParams();
    cpParams.set('login', workerLogin);
    cpParams.set('password', workerPassword);
    const options = new RequestOptions({ headers: cpHeaders, params: cpParams });
    return this.http.get(this.workerUrl, options)
      .map(this.extractData)
      .catch(this.handleError);
  }

  updateWorker(worker: Worker): Observable<number> {
    const cpHeaders = new Headers({ 'Content-Type': 'application/json' });
    const options = new RequestOptions({ headers: cpHeaders });
    return this.http.put(this.workerUrl, worker, options)
      .map(success => success.status)
      .catch(this.handleError);
  }

  deleteWorkerById(workerId: string): Observable<number> {
    const cpHeaders = new Headers({ 'Content-Type': 'application/json' });
    const cpParams = new URLSearchParams();
    cpParams.set('id', workerId);
    const options = new RequestOptions({ headers: cpHeaders, params: cpParams });
    return this.http.delete(this.workerUrl, options)
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
