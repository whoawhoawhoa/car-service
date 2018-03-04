import {Client} from './client';
import {Car} from './car';

export class AvailableOrder {
  constructor( public id: number, public orderDate: Date, public serviceType: string,
               public client: Client, public car: Car) {
  }
}
