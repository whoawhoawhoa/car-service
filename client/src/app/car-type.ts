import {Price} from './price';
import {Car} from './car';

export class CarType {
  constructor( public id: number, public carType: string, public priceSet: Price[], public carSet: Car[]) {
  }
}