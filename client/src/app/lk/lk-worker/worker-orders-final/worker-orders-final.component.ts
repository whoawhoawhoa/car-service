import {Component, Input, OnInit} from '@angular/core';
import {Order} from '../../../table-classes/order';
import {OrderService} from '../../../services/order.service';
import {WorkerOrdersComponent} from '../worker-orders/worker-orders.component';
import {FormControl, FormGroup, Validators} from '@angular/forms';
import {ClientService} from '../../../services/client.service';
import {OrderEsService} from '../../../services/order-es.service';

@Component({
  selector: 'app-worker-orders-final',
  templateUrl: './worker-orders-final.component.html',
  styleUrls: ['./worker-orders-final.component.css']
})
export class WorkerOrdersFinalComponent implements OnInit {

  @Input() order: Order;

  ratingForm = new FormGroup({
    rating: new FormControl('', Validators.required)
  });


  constructor(private orderService: OrderService,
              private orderEsService: OrderEsService,
              private workerOrdersComponent: WorkerOrdersComponent,
              private clientService: ClientService) { }

  ngOnInit() {
  }

  onWorkerFinished() {
    this.order.status = 2;
    this.orderService.updateOrder(this.order)
      .subscribe();
  }

  check() {
    this.orderService.getOrderById('' + this.order.id)
      .subscribe(data => {
        if (data.status == 5) {
          alert('Этот заказ был отменен!');
          this.workerOrdersComponent.getAllOrders();
      }
        this.order = data;
      });
  }

  onCancel() {
    this.order.status = 5;
    this.orderService.updateOrder(this.order)
      .subscribe(data => this.workerOrdersComponent.getAllOrders());
  }

  onRatingFormSubmit() {
    this.order.status = 6;
    this.order.workerMark = this.ratingForm.get('rating').value;
    this.orderEsService.createOrderEs(this.order)
      .subscribe(data => {
        this.orderService.updateOrder(this.order)
          .subscribe(succesata => {
            this.workerOrdersComponent.getAllOrders();
            this.order.client.rating = -1;
            this.clientService.updateClient(this.order.client)
              .subscribe();
          });
      });
  }

}
