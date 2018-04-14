import {Component, Input, OnInit} from '@angular/core';
import {AvailableOrderService} from '../../../services/available-order.service';
import {ServiceService} from '../../../services/service.service';
import {AvailableOrder} from '../../../table-classes/available-order';
import {Service} from '../../../table-classes/service';
import {ClientOrdersComponent} from '../../lk-client/client-orders/client-orders.component';
import {WorkerOrdersComponent} from '../worker-orders/worker-orders.component';
import {ActivatedRoute} from '@angular/router';
@Component({
  selector: 'app-worker-available-orders',
  templateUrl: './worker-available-orders.component.html',
  styleUrls: ['./worker-available-orders.component.css']
})
export class WorkerAvailableOrdersComponent implements OnInit {

  @Input() login: string;
  @Input() id: number;
  sourceServices: Service[];
  sourceAvOrders: AvailableOrder[];
  statusCode: number;

  constructor(private avOrderService: AvailableOrderService,
              private serviceService: ServiceService,
              private clientOrdersComponent: ClientOrdersComponent,
              private workerOrdersComponent: WorkerOrdersComponent,
              private route: ActivatedRoute) {
    this.sourceAvOrders = [];
  }

  ngOnInit() {
    this.getAvOrders();
  }

  getAvOrders() {
    this.sourceAvOrders = [];
    this.avOrderService.getAllAvailableOrders()
      .subscribe(avOrders => {
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
    this.workerOrdersComponent.getAllOrders();
    this.workerOrdersComponent.getWorker(this.route.snapshot.paramMap.get('login'), this.route.snapshot.paramMap.get('password'));
      });
      });
  }

  accept(order: AvailableOrder) {
    if (order.workers === null) {
      order.workers = [];
    }
    order.workers.push(this.id);
    this.avOrderService.updateAvOrder(order)
      .subscribe(data =>  this.getAvOrders());
  }

  onCancel(order: AvailableOrder) {
    const index = order.workers.indexOf(this.id);
    order.workers.splice(index, 1);
    this.avOrderService.updateAvOrder(order)
      .subscribe(data =>  this.getAvOrders());
  }
}
