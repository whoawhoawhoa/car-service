import {Worker} from './worker';
import {Price} from './price';

export class Service {
  constructor( public id: number, public coef: number, public worker: Worker, public price: Price) {
  }
}
