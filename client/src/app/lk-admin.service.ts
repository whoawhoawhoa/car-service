import { Injectable } from '@angular/core';
import {Client} from "./client";
import {Headers, Http, RequestOptions, Response} from "@angular/http";
import {Observable} from "rxjs/Observable";
import {Admin} from "./admin";

@Injectable()
export class LkAdminService {

  adminUrl = 'http://localhost:9090/admin';
  updateAdminUrl = 'http://localhost:9090/admin';

  constructor(private http: Http) { }

  getAdmin(login: string, password: string): Observable<Client> {
    return this.http.get(this.adminUrl + "?login=" + login + "&password=" + password)
      .map(this.extractData)
      .catch(this.handleError);

  }

  getAdminById(adminId: string): Observable<Client> {
    let cpHeaders = new Headers({ 'Content-Type': 'application/json' });
    let cpParams = new URLSearchParams();
    cpParams.set('id', adminId);
    let options = new RequestOptions({ headers: cpHeaders, params: cpParams });
    return this.http.get(this.adminUrl, options)
      .map(this.extractData)
      .catch(this.handleError);
  }

  updateAdmin(admin: Admin):Observable<number> {
    let cpHeaders = new Headers({ 'Content-Type': 'application/json' });
    let options = new RequestOptions({ headers: cpHeaders });
    return this.http.put(this.updateAdminUrl, admin, options)
      .map(success => success.status)
      .catch(this.handleError);
  }

  deleteAdminById(adminId: string): Observable<number> {
    return this.http.delete(this.adminUrl + "?id=" + adminId)
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
