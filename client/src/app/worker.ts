import {Service} from './service';
import {Passport} from './passport';
import {Order} from './order';

export class Worker {
  constructor(public id: number, public login: string, public password: string, public name: string, public fname: string,
              public phone_number: number, public city: string, public rating: number, public status: number,
              public serviceSet: Service[], public passportSet: Passport[], public orderSet: Order[]) {
  }
}
