import {Component, Input, OnInit} from '@angular/core';
import {Order} from "../../../table-classes/order";
import {OrderService} from "../../../services/order.service";
import {WorkerService} from "../../../services/worker.service";
import {ClientAvailableOrderComponent} from "../client-available-order/client-available-order.component";
import {ClientOrdersComponent} from "../client-orders/client-orders.component";
import {FormControl, FormGroup, Validators} from "@angular/forms";

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
              private workerService: WorkerService,
              private clientAvOrders: ClientOrdersComponent) { }

  ngOnInit() {
  }

  onClientCommit()
  {
    this.order.status = 3;
    this.orderService.updateOrder(this.order)
      .subscribe(data => this.clientAvOrders.getOrders());
  }

  refresh()
  {
    this.orderService.getOrderById(""+this.order.id)
      .subscribe(order => {
        this.order = order;
        this.clientAvOrders.getOrders();
      });
  }

  onCancel()
  {
    this.order.status = 5;
    this.orderService.updateOrder(this.order)
      .subscribe(data => this.clientAvOrders.getOrders());
  }

  onRatingFormSubmit()
  {
    this.order.status = 4;
    this.order.clientMark = this.ratingForm.get('rating').value;
    this.orderService.updateOrder(this.order)
      .subscribe(data => {
        this.clientAvOrders.getOrders();
        this.orderService.getOrdersByWorkerLogin(this.order.worker.login)
          .subscribe(data => {
            let sum = 0, counter = 0;
            for (let i = 0; i < data.length; i++)
            {
              if(data[i].clientMark != 0) {
                sum += data[i].clientMark;
                counter++;
              }
            }
            if(sum != 0 && counter != 0) {
              sum = sum/counter;
              this.order.worker.rating = sum;
              this.workerService.updateWorker(this.order.worker)
                .subscribe();
            }
          });
      });
  }

}
