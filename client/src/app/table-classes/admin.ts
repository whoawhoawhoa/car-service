import {User} from './user';

export class Admin {
  constructor(public id: number, public login: string, public password: string, public user: User) {
  }
}
