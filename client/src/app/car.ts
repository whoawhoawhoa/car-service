import {Client} from './client';
import {CarType} from './car-type';
import {Order} from './order';
import {AvailableOrder} from './available-order';

export class Car {
  constructor( public id: number, public number: string, public brand: string, public color: string,
               public client: Client, public carType: CarType) {
  }
}
