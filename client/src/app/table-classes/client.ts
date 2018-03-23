import {User} from './user';

export class Client {
  constructor( public id: number, public login: string, public password: string, public name: string, public fname: string,
              public pnumber: number, public city: string, public rating: number,
               public email: string, public user: User) {
  }
}
