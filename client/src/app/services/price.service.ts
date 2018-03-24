import { Injectable } from '@angular/core';
import {Headers, Http, RequestOptions, Response} from '@angular/http';
import {Observable} from 'rxjs/Observable';
import {Price} from '../table-classes/price';

@Injectable()
export class PriceService {

  pricesUrl = 'http://localhost:9090/prices';
  priceUrl = 'http://localhost:9090/price';

  constructor(private http: Http) { }

  getPrices() {
    return this.http.get(this.pricesUrl)
      .map(this.extractData)
      .catch(this.handleError);
  }

  getPricesByCarType(carType: string) {
    return this.http.get(this.pricesUrl + 'ByCarType?carType=' + carType)
      .map(this.extractData)
      .catch(this.handleError);
  }

  getPricesByServiceType(serviceType: string) {
    return this.http.get(this.pricesUrl + 'ByServiceType?serviceType=' + serviceType)
      .map(this.extractData)
      .catch(this.handleError);
  }

  createPrice(price: Price): Observable<number> {
    const cpHeaders = new Headers({ 'Content-Type': 'application/json' });
    const options = new RequestOptions({headers: cpHeaders});
    return this.http.put(this.priceUrl, price, options)
      .map(success => success.status)
      .catch(this.handleError);
  }

  updatePrice(price: Price): Observable<number> {
    const cpHeaders = new Headers({ 'Content-Type': 'application/json' });
    const options = new RequestOptions({ headers: cpHeaders });
    return this.http.put(this.priceUrl, price, options)
      .map(success => success.status)
      .catch(this.handleError);
  }

  getPriceById(id: string) {
    return this.http.get(this.priceUrl + '?id=' + id)
      .map(this.extractData)
      .catch(this.handleError);
  }

  deletePriceById(id: string) {
    return this.http.delete(this.priceUrl + '?id=' + id)
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
