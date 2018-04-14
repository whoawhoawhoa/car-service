import {Component, Input, OnInit} from '@angular/core';
import {Order} from '../../../table-classes/order';
import {OrderService} from '../../../services/order.service';
import {WorkerService} from '../../../services/worker.service';
import {ClientOrdersComponent} from '../client-orders/client-orders.component';
import {FormControl, FormGroup, Validators} from '@angular/forms';
import {OrderEsService} from '../../../services/order-es.service';

@Component({
  selector: 'app-client-orders-final',
  templateUrl: './client-orders-final.component.html',
  styleUrls: ['./client-orders-final.component.css']
})
export class ClientOrdersFinalComponent implements OnInit {

  @Input() order: Order;

  ratingForm = new FormGroup({
    rating: new FormControl('', Validators.required)
  });

  constructor(private orderService: OrderService,
              private orderEsService: OrderEsService,
              private workerService: WorkerService,
              private clientAvOrders: ClientOrdersComponent) { }

  ngOnInit() {
  }

  onClientCommit() {
    this.order.status = 3;
    this.orderService.updateOrder(this.order)
      .subscribe(data => this.clientAvOrders.getOrders());
  }

  refresh() {
    this.orderService.getOrderById('' + this.order.id)
      .subscribe(order => {
        this.order = order;
        this.clientAvOrders.getOrders();
      });
  }

  onCancel() {
    this.order.status = 5;
    this.orderEsService.createOrderEs(this.order)
      .subscribe(successCode => {
        this.orderService.updateOrder(this.order)
          .subscribe(data => this.clientAvOrders.getOrders());
      });
  }

  onRatingFormSubmit() {
    this.order.status = 4;
    this.order.clientMark = this.ratingForm.get('rating').value;
    this.orderService.updateOrder(this.order)
      .subscribe(data => {
        this.clientAvOrders.getOrders();
        this.order.worker.rating = -1;
        this.workerService.updateWorker(this.order.worker)
          .subscribe();
      });
  }

}
