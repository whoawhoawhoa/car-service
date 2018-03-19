import {Component, Input, OnInit} from '@angular/core';
import {AvailableOrder} from '../../../table-classes/available-order';
import {WorkerService} from '../../../services/worker.service';
import {Worker} from '../../../table-classes/worker';
import {Service} from '../../../table-classes/service';
import {ServiceService} from '../../../services/service.service';
import {Order} from '../../../table-classes/order';
import {OrderService} from '../../../services/order.service';
import {AvailableOrderService} from '../../../services/available-order.service';

@Component({
  selector: 'app-client-available-order',
  templateUrl: './client-available-order.component.html',
  styleUrls: ['./client-available-order.component.css']
})
export class ClientAvailableOrderComponent implements OnInit {
  @Input() avOrder: AvailableOrder;
  workers: Worker[];
  services: Service[];

  constructor(private workerService: WorkerService,
              private serviceService: ServiceService,
              private orderService: OrderService,
              private avOrderService: AvailableOrderService) { }

  ngOnInit() {
    this.loadWorkerList();
  }

  loadWorkerList() {
    this.workers = [];
    this.services = [];
    this.workerService.getWorkersByIds(this.avOrder.workers)
      .subscribe(data => {
        this.workers = data;
        for (const id of this.avOrder.workers) {
          this.serviceService.getServicesForAvOrder(id, this.avOrder.serviceType,
            this.avOrder.car.carType.carType)
            .subscribe(new_data => {
              this.services.push(new_data[0]);
            });
        }
      });
  }

  refreshStatus() {
    this.loadWorkerList();
  }

  choose(worker: Worker, service: Service) {
    const order = new Order(null, null, null, this.avOrder.orderDate, this.avOrder.serviceType,
      service.price.price * service.coef, 0, this.avOrder.address, this.avOrder.commentary,
      this.avOrder.client, worker, this.avOrder.car);
    this.orderService.createOrder(order)
      .subscribe(successCode => {
        this.avOrderService.deleteAvOrderById(this.avOrder.id)
          .subscribe(success => {
            this.services = [];
            this.workers = [];
            this.avOrder = null;
          });
      });
  }
}
