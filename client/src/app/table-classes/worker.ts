import {User} from './user';

export class Worker {
  constructor(public id: number,
              public login: string,
              public password: string,
              public name: string,
              public fname: string,
              public pnumber: number,
              public city: string,
              public rating: number,
              public status: number,
              public user: User,
              public email: string) {
  }
}
