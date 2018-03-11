import { Injectable } from '@angular/core';
import {Http, Response, Headers, RequestOptions} from '@angular/http';
import {Observable} from 'rxjs/Observable';
import 'rxjs/add/operator/map';
import 'rxjs/add/operator/catch';
import {Admin} from '../table-classes/admin';

@Injectable()
export class AdminService {
  allAdminsUrl = 'http://localhost:9090/clients';
  adminUrl = 'http://localhost:9090/admin';
  defaultUrl = 'http://localhost:9090/';

  constructor(private http: Http) { }

  getAllAdmins(): Observable<Admin[]> {
    return this.http.get(this.allAdminsUrl)
      .map(this.extractData)
      .catch(this.handleError);
  }

  getAdmin(login: string, password: string): Observable<Admin> {
    return this.http.get(this.adminUrl + '?login=' + login + '&password=' + password)
      .map(this.extractData)
      .catch(this.handleError);

  }

  createAdmin(admin: Admin): Observable<number> {
    const cpHeaders = new Headers({ 'Content-Type': 'application/json' });
    const options = new RequestOptions({headers: cpHeaders});
    return this.http.post(this.adminUrl, admin, options)
      .map(success => success.status)
      .catch(this.handleError);
  }

  getAdminById(adminId: string): Observable<Admin> {
    const cpHeaders = new Headers({ 'Content-Type': 'application/json' });
    const cpParams = new URLSearchParams();
    cpParams.set('id', adminId);
    const options = new RequestOptions({ headers: cpHeaders, params: cpParams });
    return this.http.get(this.adminUrl, options)
      .map(this.extractData)
      .catch(this.handleError);
  }

  getAdminByLoginAndPassword(adminLogin: string, adminPassword: string): Observable<Admin> {
    const cpHeaders = new Headers({ 'Content-Type': 'application/json' });
    const newUrl = this.defaultUrl + 'admin?login=' + adminLogin + '&password=' + adminPassword;
    const options = new RequestOptions({ headers: cpHeaders });
    return this.http.get(newUrl, options)
      .map(this.extractData)
      .catch(this.handleError);
  }

  updateAdmin(admin: Admin): Observable<number> {
    const cpHeaders = new Headers({ 'Content-Type': 'application/json' });
    const options = new RequestOptions({ headers: cpHeaders });
    return this.http.put(this.adminUrl, admin, options)
      .map(success => success.status)
      .catch(this.handleError);
  }


  deleteAdminById(adminId: string): Observable<number> {
    return this.http.delete(this.adminUrl + '?id=' + adminId)
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
