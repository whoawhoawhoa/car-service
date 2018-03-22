import {Client} from './client';
import {Worker} from './worker';
import {Car} from './car';

export class Order {
  constructor( public id: number, public clientMark: number, public workerMark: number,
               public orderDate: Date, public serviceType: string, public totalPrice: number,
               public status: number, public address: string, public commentary: string,
               public client: Client, public worker: Worker, public car: Car) {
  }
}
