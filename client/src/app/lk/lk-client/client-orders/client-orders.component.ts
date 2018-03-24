import { Component, OnInit } from '@angular/core';
import {Order} from '../../../table-classes/order';
import {AvailableOrder} from '../../../table-classes/available-order';
import {ClientService} from '../../../services/client.service';
import {OrderService} from '../../../services/order.service';
import {ActivatedRoute, Router} from '@angular/router';
import {AvailableOrderService} from '../../../services/available-order.service';
import {Client} from '../../../table-classes/client';

@Component({
  selector: 'app-client-orders',
  templateUrl: './client-orders.component.html',
  styleUrls: ['./client-orders.component.css']
})
export class ClientOrdersComponent implements OnInit {
  clientSource: Client;
  clientAvOrders: AvailableOrder[];
  clientOrders: Order[];
  statusCode: number;

  constructor(private clientService: ClientService,
              private orderService: OrderService,
              private route: ActivatedRoute,
              private router: Router,
              private avOrderService: AvailableOrderService) { }

  ngOnInit() {
    this.getClient(this.route.snapshot.paramMap.get('login'), this.route.snapshot.paramMap.get('password'));
  }

  getClient(login: string, password: string) {
    this.clientService.getClientByLoginAndPassword(login, password)
      .subscribe(
        data => {this.clientSource = data;
          this.getOrders();
          this.getAvOrders(); },
        errorCode => this.statusCode);
  }

  getAvOrders() {
    this.avOrderService.getAvOrdersByClientLogin(this.clientSource.login)
      .subscribe(avOrders => {
        this.clientAvOrders = avOrders;
      });
  }

  getOrders() {
    this.orderService.getOrderByClientLogin(this.clientSource.login)
      .subscribe(
        data => this.clientOrders = data,
        errorCode => this.statusCode);
  }

  redirectToMain() {
    this.router.navigate(['/main/' + this.clientSource.login + '/' + this.clientSource.password]);
  }

  redirectToLK() {
    this.router.navigate(['/lkclient/' + this.clientSource.login + '/' + this.clientSource.password]);
  }

  redirectToOrders() {
    this.router.navigate(['/client-orders/' + this.clientSource.login + '/' + this.clientSource.password]);
  }

}