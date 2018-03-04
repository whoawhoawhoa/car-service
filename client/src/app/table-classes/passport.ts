import {Worker} from './worker';

export class Passport {
  constructor( public id: number, public number: number, public issuedBy: string, public worker: Worker) {
  }
}
