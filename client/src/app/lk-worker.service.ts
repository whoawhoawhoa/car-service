import { Injectable } from '@angular/core';
import {Headers, Http, RequestOptions, Response} from "@angular/http";
import {Observable} from "rxjs/Observable";
import 'rxjs/add/operator/map';
import 'rxjs/add/operator/catch';
import {Worker} from "./worker";

@Injectable()
export class LkWorkerService {

  workerUrl = 'http://localhost:8080/worker';
  updateWorkerUrl = 'http://localhost:8080/updateWorker';

  constructor(private http: Http) { }

  getWorker(): Observable<Worker> {
    return this.http.get(this.workerUrl)
      .map(this.extractData)
      .catch(this.handleError);
  }

  updateWorker(worker: Worker): Observable<number> {
    let cpHeaders = new Headers({ 'Content-Type': 'application/json' });
    let options = new RequestOptions({ headers: cpHeaders });
    return this.http.put(this.updateWorkerUrl, worker, options)
      .map(success => success.status)
      .catch(this.handleError);
  }

  deleteWorkerById(workerId: string): Observable<number> {
    return this.http.delete(this.workerUrl + "?id=" + workerId)
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
