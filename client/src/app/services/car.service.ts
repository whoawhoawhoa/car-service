import { Injectable } from '@angular/core';
import {Http, Response, Headers, RequestOptions} from '@angular/http';
import {Observable} from 'rxjs/Observable';
import 'rxjs/add/operator/map';
import 'rxjs/add/operator/catch';
import {Car} from '../table-classes/car';

@Injectable()
export class CarService {

  carUrl = 'http://localhost:9090/car';
  updateCarUrl = 'http://localhost:9090/car';
  clientsCarsUrl = 'http://localhost:9090/client_cars';

  constructor(private http: Http) { }

  getCarsByClientLogin(clientLogin: string): Observable<Car[]> {
    return this.http.get(this.clientsCarsUrl + '?login=' + clientLogin)
      .map(this.extractData)
      .catch(this.handleError);
  }

  createCar(car: Car): Observable<number> {
    const cpHeaders = new Headers({ 'Content-Type': 'application/json' });
    const options = new RequestOptions({headers: cpHeaders});
    return this.http.post(this.carUrl, car, options)
      .map(success => success.status)
      .catch(this.handleError);
  }

  updateCar(car: Car): Observable<number> {
    const cpHeaders = new Headers({ 'Content-Type': 'application/json' });
    const options = new RequestOptions({ headers: cpHeaders });
    return this.http.put(this.updateCarUrl, car, options)
      .map(success => success.status)
      .catch(this.handleError);
  }

  getCarById(id: string) {
    return this.http.get(this.carUrl + '?id=' + id)
      .map(this.extractData)
      .catch(this.handleError);
  }

  deleteCarById(id: string) {
    return this.http.delete(this.carUrl + '?id=' + id)
      .map(success => success.status)
      .catch(this.handleError);
  }

  getCarsByClientIdAndCarType(id: number, carTypeId: number) {
    return this.http.get(this.carUrl + 'sfororder?id=' + id + '&carTypeId=' + carTypeId)
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
