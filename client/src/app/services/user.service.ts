
import {Injectable} from '@angular/core';
import {Http, Response, Headers, RequestOptions} from '@angular/http';
import {User} from '../table-classes/user';
import {Observable} from 'rxjs/Observable';

@Injectable()
export class UserService {
  allUsersUrl = 'http://localhost:9090/users';
  userUrl = 'http://localhost:9090/user';

  constructor(private http: Http) { }

  getAllUsers(): Observable<User[]> {
    return this.http.get(this.allUsersUrl)
      .map(this.extractData)
      .catch(this.handleError);
  }

  createUser(user: User): Observable<number> {
    const cpHeaders = new Headers({ 'Content-Type': 'application/json' });
    const options = new RequestOptions({headers: cpHeaders});
    return this.http.post(this.userUrl, user, options)
      .map(success => success.status)
      .catch(this.handleError);
  }

  updateUser(user: User): Observable<number> {
    const cpHeaders = new Headers({ 'Content-Type': 'application/json' });
    const options = new RequestOptions({ headers: cpHeaders });
    return this.http.put(this.userUrl, user, options)
      .map(success => success.status)
      .catch(this.handleError);
  }

  deleteUser(userLogin: string): Observable<number> {
    return this.http.delete(this.userUrl + '?login=' + userLogin)
      .map(success => success.status)
      .catch(this.handleError);
  }

  getUser(login: string, password: string): Observable<User> {
    return this.http.get(this.userUrl + '?login=' + login + '&password=' + password)
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
