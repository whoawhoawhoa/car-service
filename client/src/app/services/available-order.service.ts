import { Injectable } from '@angular/core';
import {Headers, Http, RequestOptions, Response} from '@angular/http';
import {AvailableOrder} from '../table-classes/available-order';
import {Observable} from 'rxjs/Observable';

@Injectable()
export class AvailableOrderService {

  avOrderUrl = 'http://localhost:9090/putavorder';
  getAvOrderUrl = 'http://localhost:9090/avorder';
  getAvOrdersUrl = 'http://localhost:9090/available_orders';

  constructor(private http: Http) { }

  addAvOrder(avOrder: AvailableOrder): Observable<number> {
    const cpHeaders = new Headers({ 'Content-Type': 'application/json' });
    const options = new RequestOptions({headers: cpHeaders});
    return this.http.post(this.avOrderUrl, avOrder, options)
      .map(success => success.status)
      .catch(this.handleError);
  }

  getOrdersByClientLogin(login: string) {
    return this.http.get(this.getAvOrderUrl + 'byclientlogin?login=' + login)
      .map(this.extractData)
      .catch(this.handleError);
  }

  getAllAvailableOrders() {
    return this.http.get(this.getAvOrdersUrl)
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
