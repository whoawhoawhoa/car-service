import { Injectable } from '@angular/core';
import {Http, Response, Headers, RequestOptions} from '@angular/http';
import {Service} from '../table-classes/service';
import {Observable} from 'rxjs/Observable';
import 'rxjs/add/operator/map';
import 'rxjs/add/operator/catch';

@Injectable()
export class ServiceService {
  allServicesUrl = 'http://localhost:9090/services';
  serviceUrl = 'http://localhost:9090/service';
  defaultUrl = 'http://localhost:9090/';

  constructor(private http: Http) { }

  getAllServices(): Observable<Service[]> {
    return this.http.get(this.allServicesUrl)
      .map(this.extractData)
      .catch(this.handleError);
  }

  createService(service: Service): Observable<number> {
    const cpHeaders = new Headers({ 'Content-Type': 'application/json' });
    const options = new RequestOptions({headers: cpHeaders});
    return this.http.post(this.serviceUrl, service, options)
      .map(success => success.status)
      .catch(this.handleError);
  }

  getServiceById(serviceId: string): Observable<Service> {
    const cpHeaders = new Headers({ 'Content-Type': 'application/json' });
    const cpParams = new URLSearchParams();
    cpParams.set('id', serviceId);
    const options = new RequestOptions({ headers: cpHeaders, params: cpParams });
    return this.http.get(this.serviceUrl, options)
      .map(this.extractData)
      .catch(this.handleError);
  }

  getServicesByWorkerLogin(workerLogin: string): Observable<Service[]> {
    const cpHeaders = new Headers({ 'Content-Type': 'application/json' });
    const newUrl = this.defaultUrl + 'serviceWorker?login=' + workerLogin;
    const options = new RequestOptions({ headers: cpHeaders });
    return this.http.get(newUrl, options)
      .map(this.extractData)
      .catch(this.handleError);
  }

  updateService(service: Service): Observable<number> {
    const cpHeaders = new Headers({ 'Content-Type': 'application/json' });
    const options = new RequestOptions({ headers: cpHeaders });
    return this.http.put(this.serviceUrl, service, options)
      .map(success => success.status)
      .catch(this.handleError);
  }

  deleteServiceById(serviceId: string): Observable<number> {
    return this.http.delete(this.serviceUrl + '?id=' + serviceId)
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
