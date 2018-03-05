import { Injectable } from '@angular/core';
import {Http, Response, Headers, RequestOptions} from '@angular/http';
import {Observable} from 'rxjs/Observable';
import 'rxjs/add/operator/map';
import 'rxjs/add/operator/catch';
import {Order} from '../table-classes/order';

@Injectable()
export class OrderService {

  clientsOrdersUrl = 'http://localhost:9090/orderbyclient';
  defaultUrl = 'http://localhost:9090/';

  constructor(private http: Http) { }

  getOrderByClientLogin(clientLogin: string): Observable<Order[]> {
    return this.http.get(this.clientsOrdersUrl + '?login=' + clientLogin)
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
