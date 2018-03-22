import {Component, Input, OnInit} from '@angular/core';
import {AvailableOrderService} from '../../../services/available-order.service';
import {ServiceService} from '../../../services/service.service';
import {AvailableOrder} from '../../../table-classes/available-order';
import {Service} from '../../../table-classes/service';

@Component({
  selector: 'app-worker-available-orders',
  templateUrl: './worker-available-orders.component.html',
  styleUrls: ['./worker-available-orders.component.css']
})
export class WorkerAvailableOrdersComponent implements OnInit {

  @Input() login: string;
  @Input() id: number;
  avOrders: AvailableOrder[];
  sourceServices: Service[];
  sourceAvOrders: AvailableOrder[];

  constructor(private avOrderService: AvailableOrderService,
              private serviceService: ServiceService) {
    this.sourceAvOrders = [];
  }

  ngOnInit() {
    this.getAvOrders();
  }

  getAvOrders() {
    this.sourceAvOrders = [];
    this.avOrders = [];
    this.avOrderService.getAllAvailableOrders()
      .subscribe(avOrders => {
    this.avOrders = avOrders;
    this.serviceService.getServicesByWorkerLogin(this.login)
      .subscribe(services => {
    this.sourceServices = services;
    for (let i = 0; i < this.sourceServices.length; i++) {
      for (let j = 0; j < avOrders.length; j++) {
        if (avOrders[j].serviceType == this.sourceServices[i].price.serviceType
          && avOrders[j].car.carType.carType == this.sourceServices[i].price.carType.carType) {
          this.sourceAvOrders.push(avOrders[j]);
        }
      }
    }
      });
      });
  }

  accept(order: AvailableOrder) {
    if (order.workers === null) {
      order.workers = [];
    }
    order.workers.push(this.id);
    this.avOrderService.updateAvOrder(order)
      .subscribe();
    this.getAvOrders();
  }
}
