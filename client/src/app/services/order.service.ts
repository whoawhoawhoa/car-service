import { Injectable } from '@angular/core';
import {Http, Response, Headers, RequestOptions} from '@angular/http';
import {Observable} from 'rxjs/Observable';
import 'rxjs/add/operator/map';
import 'rxjs/add/operator/catch';
import {Order} from '../table-classes/order';

@Injectable()
export class OrderService {
  allOrdersUrl = 'http://localhost:9090/services';
  orderUrl = 'http://localhost:9090/service';
  clientsOrdersUrl = 'http://localhost:9090/orderbyclient';
  defaultUrl = 'http://localhost:9090/';

  constructor(private http: Http) { }

  getOrderByClientLogin(clientLogin: string): Observable<Order[]> {
    return this.http.get(this.clientsOrdersUrl + '?login=' + clientLogin)
      .map(this.extractData)
      .catch(this.handleError);
  }

  getAllOrders(): Observable<Order[]> {
    return this.http.get(this.allOrdersUrl)
      .map(this.extractData)
      .catch(this.handleError);
  }

  createOrder(order: Order): Observable<number> {
    const cpHeaders = new Headers({ 'Content-Type': 'application/json' });
    const options = new RequestOptions({headers: cpHeaders});
    return this.http.post(this.orderUrl, order, options)
      .map(success => success.status)
      .catch(this.handleError);
  }

  getOrderById(orderId: string): Observable<Order> {
    const cpHeaders = new Headers({ 'Content-Type': 'application/json' });
    const cpParams = new URLSearchParams();
    cpParams.set('id', orderId);
    const options = new RequestOptions({ headers: cpHeaders, params: cpParams });
    return this.http.get(this.orderUrl, options)
      .map(this.extractData)
      .catch(this.handleError);
  }

  getOrdersByWorkerLogin(workerLogin: string): Observable<Order[]> {
    const cpHeaders = new Headers({ 'Content-Type': 'application/json' });
    const newUrl = this.defaultUrl + 'serviceWorker?login=' + workerLogin;
    const options = new RequestOptions({ headers: cpHeaders });
    return this.http.get(newUrl, options)
      .map(this.extractData)
      .catch(this.handleError);
  }

  updateOrder(order: Order): Observable<number> {
    const cpHeaders = new Headers({ 'Content-Type': 'application/json' });
    const options = new RequestOptions({ headers: cpHeaders });
    return this.http.put(this.orderUrl, order, options)
      .map(success => success.status)
      .catch(this.handleError);
  }

  deleteOrderById(orderId: string): Observable<number> {
    return this.http.delete(this.orderUrl + '?id=' + orderId)
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
