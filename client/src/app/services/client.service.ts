import { Injectable } from '@angular/core';
import {Http, Response, Headers, RequestOptions} from '@angular/http';
import {Observable} from 'rxjs/Observable';
import 'rxjs/add/operator/map';
import 'rxjs/add/operator/catch';
import { Client } from '../table-classes/client';

@Injectable()
export class ClientService {
  allClientsUrl = 'http://localhost:9090/clients';
  clientUrl = 'http://localhost:9090/admin';
  defaultUrl = 'http://localhost:9090/';

  constructor(private http: Http) { }

  getAllClients(): Observable<Client[]> {
    return this.http.get(this.allClientsUrl)
      .map(this.extractData)
      .catch(this.handleError);
  }

  createClient(client: Client): Observable<number> {
    const cpHeaders = new Headers({ 'Content-Type': 'application/json' });
    const options = new RequestOptions({headers: cpHeaders});
    return this.http.post(this.clientUrl, client, options)
      .map(success => success.status)
      .catch(this.handleError);
  }

  getClientById(clientId: string): Observable<Client> {
    const cpHeaders = new Headers({ 'Content-Type': 'application/json' });
    const cpParams = new URLSearchParams();
    cpParams.set('id', clientId);
    const options = new RequestOptions({ headers: cpHeaders, params: cpParams });
    return this.http.get(this.clientUrl, options)
      .map(this.extractData)
      .catch(this.handleError);
  }

  getClientByLoginAndPassword(clientLogin: string, clientPassword: string): Observable<Client> {
    const cpHeaders = new Headers({ 'Content-Type': 'application/json' });
    const newUrl = this.defaultUrl + 'admin?login=' + clientLogin + '&password=' + clientPassword;
    const options = new RequestOptions({ headers: cpHeaders });
    return this.http.get(newUrl, options)
      .map(this.extractData)
      .catch(this.handleError);
  }

  updateClient(client: Client): Observable<number> {
    const cpHeaders = new Headers({ 'Content-Type': 'application/json' });
    const options = new RequestOptions({ headers: cpHeaders });
    return this.http.put(this.clientUrl, client, options)
      .map(success => success.status)
      .catch(this.handleError);
  }

  deleteClientById(clientId: string): Observable<number> {
    return this.http.delete(this.clientUrl + '?id=' + clientId)
      .map(success => success.status)
      .catch(this.handleError);
  }

  private extractData(res: Response) {
    return res.json();
  }

  private handleError (error: Response | any) {
    console.error(error.message || error);
    console.error(error.status);
    return Observable.throw(error.status);
  }
}
