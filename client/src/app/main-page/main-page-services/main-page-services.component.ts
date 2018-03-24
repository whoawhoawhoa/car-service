import {Component, Input, OnInit} from '@angular/core';
import {Car} from '../../table-classes/car';
import {FormControl, FormGroup, Validators} from '@angular/forms';
import {AvailableOrder} from '../../table-classes/available-order';
import {Client} from '../../table-classes/client';
import {AvailableOrderService} from '../../services/available-order.service';
import {PriceService} from '../../services/price.service';
import {Price} from '../../table-classes/price';

@Component({
  selector: 'app-main-page-services',
  templateUrl: './main-page-services.component.html',
  styleUrls: ['./main-page-services.component.css']
})
export class MainPageServicesComponent implements OnInit {

  @Input() sourceClient: Client;
  @Input() serviceType: string;
  @Input() cars: Car[];
  prices: Price[];
  sourceCars = [];
  processValidation = false;
  isOrdered = false;
  toContinue = false;
  isAlreadyOrdered = false;

  constructor(private avoOrderService: AvailableOrderService,
              private priceService: PriceService) { }

  finalOrderForm = new FormGroup({
    commentary: new FormControl(''),
    address: new FormControl('', Validators.required),
    car: new FormControl('', Validators.required)
  });

  ngOnInit() {
    this.setFinalOrderForm();
  }

  setFinalOrderForm() {
    this.priceService.getPricesByServiceType(this.serviceType)
      .subscribe(data => {
        this.prices = data;
        let counter = 0;
        for (let i = 0; i < this.cars.length; i++) {
          for (let j = 0; j < this.prices.length; j++) {
            if (this.cars[i].carType.carType === this.prices[j].carType.carType) {
              this.sourceCars[counter] = this.cars[i];
              counter++;
            }
          }
        }
      });
  }

  onFinalOrderFormSubmit() {
    this.isAlreadyOrdered = false;
    this.processValidation = true;
    if (this.finalOrderForm.invalid) {
      return; // Validation failed, exit from method.
    }
    const comment = this.finalOrderForm.get('commentary').value;
    const address = this.finalOrderForm.get('address').value;
    const carId = this.finalOrderForm.get('car').value;
    let car = new Car(null, null, null, null, null, null);
    for (let i = 0; i < this.cars.length; i++) {
      if (this.cars[i].id == carId) {
        car = this.cars[i];
      }
    }
    const avOrder = new AvailableOrder(null, null, this.serviceType, this.sourceClient, car, address, comment, null);
    this.avoOrderService.getAvOrdersByClientLogin(this.sourceClient.login)
      .subscribe(orders => {
        const currentOrders = orders;
        for (let i = 0; i < currentOrders.length; i++) {
          if (currentOrders[i].serviceType === avOrder.serviceType
            && currentOrders[i].car.id === avOrder.car.id) {
            this.isAlreadyOrdered = true;
            break;
          }
        }
        if (!this.isAlreadyOrdered) {
          this.avoOrderService.addAvOrder(avOrder)
            .subscribe(data => {
              this.isOrdered = true;
              this.toContinue = true;
            });
        }
      });
  }


}
