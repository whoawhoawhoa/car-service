import { Injectable } from '@angular/core';
import {Headers, Http, RequestOptions, Response} from '@angular/http';
import {Observable} from 'rxjs/Observable';
import {CarType} from '../table-classes/car-type';

@Injectable()
export class CarTypeService {

  carTypeUrl = 'http://localhost:9090/car_type';
  carTypeUpdateUrl = 'http://localhost:9090/cartypeupdate';
  deleteCarTypeUrl = 'http://localhost:9090/deletecartype';

  constructor(private http: Http) { }

  getAllCarTypes() {
    return this.http.get(this.carTypeUrl + 's')
      .map(this.extractData)
      .catch(this.handleError);
  }

  getCarTypeById(id: string) {
    return this.http.get(this.carTypeUrl + '?id=' + id)
      .map(this.extractData)
      .catch(this.handleError);
  }


  createCarType(carType: CarType): Observable<number>
  {
    const cpHeaders = new Headers({ 'Content-Type': 'application/json' });
    const options = new RequestOptions({headers: cpHeaders});
    return this.http.post(this.carTypeUrl, carType, options)
      .map(success => success.status)
      .catch(this.handleError);
  }

  updateCarType(carType: CarType): Observable<number>
  {
    const cpHeaders = new Headers({ 'Content-Type': 'application/json' });
    const options = new RequestOptions({ headers: cpHeaders });
    return this.http.put(this.carTypeUpdateUrl, carType, options)
      .map(success => success.status)
      .catch(this.handleError);
  }

  deleteCarTypeById(id: string): Observable<number>
  {
    return this.http.delete(this.deleteCarTypeUrl + '?id=' + id)
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
