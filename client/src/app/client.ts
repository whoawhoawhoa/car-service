import {Car} from './car';
import {Order} from './order';
import {AvailableOrder} from './available-order';

export class Client {
  constructor( public id: number, public login: string, public password: string, public name: string, public fname: string,
              public phone_number: number, public city: string, public rating: number,
              public carSet: Car[] = null, public orderSet: Order[] = null,
              public availableOrderSet: AvailableOrder[] = null) {
  }
}
