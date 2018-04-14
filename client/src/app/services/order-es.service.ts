import { Injectable } from '@angular/core';
import {Http, Response, Headers, RequestOptions} from '@angular/http';
import {Observable} from 'rxjs/Observable';
import 'rxjs/add/operator/map';
import 'rxjs/add/operator/catch';
import {OrderEs} from '../table-classes/order-es';

@Injectable()
export class OrderEsService {
  allOrdersUrl = 'http://localhost:9090/rest/search/all_order_es';
  orderUrl = 'http://localhost:9090/rest/search/order_es';
  orderByIdUrl = 'http://localhost:9090/rest/search/order_es_by_id';
  clientsOrdersUrl = 'http://localhost:9090/rest/search/order_es_by_client';
  workerOrdersUrl = 'http://localhost:9090/rest/search/order_es_by_worker';
  orderByFilterUrl = 'http://localhost:9090/rest/search/order_es_by_filter';
  orderByClientFilterUrl = 'http://localhost:9090/rest/search/order_es_by_client_filter';
  orderByWorkerFilterUrl = 'http://localhost:9090/rest/search/order_es_by_worker_filter';

  constructor(private http: Http) { }

  getOrderEsByClientLogin(clientLogin: string): Observable<OrderEs[]> {
    return this.http.get(this.clientsOrdersUrl + '?login=' + clientLogin)
      .map(this.extractData)
      .catch(this.handleError);
  }

  getAllOrders(): Observable<OrderEs[]> {
    return this.http.get(this.allOrdersUrl)
      .map(this.extractData)
      .catch(this.handleError);
  }

  createOrderEs(order: OrderEs): Observable<number> {
    const cpHeaders = new Headers({ 'Content-Type': 'application/json' });
    const options = new RequestOptions({headers: cpHeaders});
    return this.http.post(this.orderUrl, order, options)
      .map(success => success.status)
      .catch(this.handleError);
  }

  getOrderById(orderId: string): Observable<OrderEs> {
    return this.http.get(this.orderByIdUrl + '?id=' + orderId)
      .map(this.extractData)
      .catch(this.handleError);
  }

  getOrdersByWorkerLogin(workerLogin: string): Observable<OrderEs[]> {
    const cpHeaders = new Headers({ 'Content-Type': 'application/json' });
    const newUrl = this.workerOrdersUrl + '?login=' + workerLogin;
    const options = new RequestOptions({ headers: cpHeaders });
    return this.http.get(newUrl, options)
      .map(this.extractData)
      .catch(this.handleError);
  }

  updateOrder(order: OrderEs): Observable<number> {
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

  getOrderEsByFilter(serviceType: string, carType: string, clientLogin: string,
                     workerLogin: string, price1: number, price2: number,
                     fromDate: Date, toDate: Date, fromClientMark: number,
                     toClientMark: number, fromWorkerMark: number, toWorkerMark: number): Observable<OrderEs[]> {
    return this.http.get(this.orderByFilterUrl + '?serviceType=' + serviceType + '&' + 'carType=' + carType +
    '&' + 'clientLogin=' + clientLogin + '&' + 'workerLogin=' + workerLogin + '&' + 'price1=' + price1 + '&' +
    'price2=' + price2 + '&' + 'fromDate=' + fromDate + '&' + 'toDate=' + toDate + '&' + 'fromClientMark=' +
    fromClientMark + '&' + 'toClientMark=' + toClientMark + '&' + 'fromWorkerMark=' + fromWorkerMark + '&' +
    'toWorkerMark=' + toWorkerMark)
      .map(this.extractData)
      .catch(this.handleError);
  }

  getOrderEsByClientFilter(clientLogin: string, serviceType: string, price1: number, price2: number,
                     fromDate: Date, toDate: Date): Observable<OrderEs[]> {
    return this.http.get(this.orderByClientFilterUrl + '?clientLogin=' + clientLogin + '&'
      + 'serviceType=' + serviceType + '&'  + 'price1=' + price1 + '&' +
      'price2=' + price2 + '&' + 'fromDate=' + fromDate + '&' + 'toDate=' + toDate)
      .map(this.extractData)
      .catch(this.handleError);
  }

  getOrderEsByWorkerFilter(workerLogin: string, serviceType: string, price1: number, price2: number,
                           fromDate: Date, toDate: Date): Observable<OrderEs[]> {
    return this.http.get(this.orderByWorkerFilterUrl + '?workerLogin=' + workerLogin + '&'
      + 'serviceType=' + serviceType + '&'  + 'price1=' + price1 + '&' +
      'price2=' + price2 + '&' + 'fromDate=' + fromDate + '&' + 'toDate=' + toDate)
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
