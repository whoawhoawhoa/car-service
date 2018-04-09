import { Injectable } from '@angular/core';
import {Http, Response, Headers, RequestOptions} from '@angular/http';
import {Observable} from 'rxjs/Observable';
import {Worker} from '../table-classes/worker';
import 'rxjs/add/observable/throw';

@Injectable()
export class WorkerService {
  allWorkersUrl = 'http://localhost:9090/workers';
  workerUrl = 'http://localhost:9090/worker';
  defaultUrl = 'http://localhost:9090/';

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
    const newUrl = this.defaultUrl + 'worker?login=' + workerLogin + '&password=' + workerPassword;
    const options = new RequestOptions({ headers: cpHeaders });
    return this.http.get(newUrl, options)
      .map(this.extractData)
      .catch(this.handleError);
  }

  getWorkersByIds(workerIds: number[]): Observable<Worker[]> {
    const cpHeaders = new Headers({ 'Content-Type': 'application/json' });
    let newUrl = this.defaultUrl + 'workers?';
    for (const id of workerIds) {
      let a = '';
      if (workerIds.indexOf(id) !== 0) {
        a = '&';
      }
      newUrl = newUrl + a + '=' + id.toString();
    }
    const options = new RequestOptions({ headers: cpHeaders });
    return this.http.get(newUrl, options)
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

  deleteWorkerByLogin(workerLogin: string): Observable<number> {
    return this.http.delete(this.workerUrl + '?login=' + workerLogin)
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
