import {Component, Input, OnInit} from '@angular/core';
import {OrderService} from '../../../services/order.service';
import {Order} from '../../../table-classes/order';

@Component({
  selector: 'app-worker-orders',
  templateUrl: './worker-orders.component.html',
  styleUrls: ['./worker-orders.component.css']
})
export class WorkerOrdersComponent implements OnInit {
  allOrders: Order[];
  @Input() login: string;
  @Input() password: string;
  statusCode: number;

  constructor(private orderService: OrderService) { }

  ngOnInit() {
    this.getAllOrders();
  }

  getAllOrders() {
    this.orderService.getOrdersByWorkerLogin(this.login)
      .subscribe(
        data => this.allOrders = data,
        errorCode =>  this.statusCode = errorCode);
  }
}
