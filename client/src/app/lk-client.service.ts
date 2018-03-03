import { Injectable } from '@angular/core';
import {Observable} from "rxjs/Observable";
import {Client} from "./client";
import 'rxjs/add/operator/map';
import 'rxjs/add/operator/catch';
import { Http, Response, Headers, RequestOptions } from '@angular/http';


@Injectable()
export class LkClientService {

  clientUrl = 'http://localhost:9090/client';
  updateClientUrl = 'http://localhost:9090/updateClient';

  constructor(private http: Http) { }

  getClient(login: string, password: string): Observable<Client> {
    return this.http.get(this.clientUrl + "?login=" + login + "&password=" + password)
      .map(this.extractData)
      .catch(this.handleError);

  }

  getClientById(clientId: string): Observable<Client> {
    let cpHeaders = new Headers({ 'Content-Type': 'application/json' });
    let cpParams = new URLSearchParams();
    cpParams.set('id', clientId);
    let options = new RequestOptions({ headers: cpHeaders, params: cpParams });
    return this.http.get(this.clientUrl, options)
      .map(this.extractData)
      .catch(this.handleError);
  }

  updateClient(client: Client):Observable<number> {
    let cpHeaders = new Headers({ 'Content-Type': 'application/json' });
    let options = new RequestOptions({ headers: cpHeaders });
    return this.http.put(this.updateClientUrl, client, options)
      .map(success => success.status)
      .catch(this.handleError);
  }

  deleteClientById(clientId: string): Observable<number> {
    return this.http.delete(this.clientUrl + "?id=" + clientId)
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
