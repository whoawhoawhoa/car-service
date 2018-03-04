import { Injectable } from '@angular/core';
import {Http, Response} from "@angular/http";
import {Observable} from "rxjs/Observable";

@Injectable()
export class CarTypeService {

  carTypeUrl = 'http://localhost:9090/car_type';

  constructor(private http: Http) { }

  getCarTypeById(id: string)
  {
    return this.http.get(this.carTypeUrl + "?id=" + id)
      .map(this.extractData)
      .catch(this.handleError);
  }

  getCarTypeByType(type: string)
  {
    return this.http.get(this.carTypeUrl + "_type?type=" + type)
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
