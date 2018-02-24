import {Service} from './service';
import {CarType} from './car-type';

export class Price {
  constructor( public id: number, public serviceType: string, public price: number,
               public carType: CarType, public serviceSet: Service[]) {
  }
}
