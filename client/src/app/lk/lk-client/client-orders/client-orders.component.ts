import {Component, OnInit} from '@angular/core';
import {Order} from '../../../table-classes/order';
import {AvailableOrder} from '../../../table-classes/available-order';
import {ClientService} from '../../../services/client.service';
import {OrderService} from '../../../services/order.service';
import {ActivatedRoute, Router} from '@angular/router';
import {AvailableOrderService} from '../../../services/available-order.service';
import {Client} from '../../../table-classes/client';
import {MatDialog} from '@angular/material/dialog';
import {ClientPaymentComponent} from '../client-payment/client-payment.component';
import {OrderEsService} from '../../../services/order-es.service';
import {FormControl, FormGroup, Validators} from '@angular/forms';
import {OrderEs} from '../../../table-classes/order-es';

@Component({
  selector: 'app-client-orders',
  templateUrl: './client-orders.component.html',
  styleUrls: ['./client-orders.component.css']
})
export class ClientOrdersComponent implements OnInit {
  clientSource: Client;
  clientAvOrders: AvailableOrder[];
  clientOrders: Order[];
  clientFinishedOrders: OrderEs[];
  statusCode: number;
  money: number;

  filterForm = new FormGroup({
    serviceType: new FormControl('', Validators.required),
    price1: new FormControl('', Validators.required),
    price2: new FormControl('', Validators.required),
    fromDate: new FormControl('', Validators.required),
    toDate: new FormControl('', Validators.required),
  });

  constructor(private clientService: ClientService,
              private orderEsService: OrderEsService,
              private orderService: OrderService,
              private route: ActivatedRoute,
              private router: Router,
              private avOrderService: AvailableOrderService,
              private dialog: MatDialog) { }

  ngOnInit() {
    this.getClient(this.route.snapshot.paramMap.get('login'), this.route.snapshot.paramMap.get('password'));
  }

  getClient(login: string, password: string) {
    this.clientService.getClientByLoginAndPassword(login, password)
      .subscribe(
        data => {
          this.clientSource = data;
          this.getOrders();
          this.getFinishedOrders();
          this.getAvOrders();
          this.getAmountOfMoney();
          },
        errorCode => this.statusCode);
  }

  getAvOrders() {
    this.avOrderService.getAvOrdersByClientLogin(this.clientSource.login)
      .subscribe(avOrders => {
        this.clientAvOrders = avOrders;
        this.getOrders();
      });
  }

  onFilterFormSubmit() {
    this.preProcessConfigurations();
    const serviceType = this.filterForm.get('serviceType').value;
    let fromPrice = this.filterForm.get('price1').value;
    let toPrice = this.filterForm.get('price2').value;
    let fromDate = this.filterForm.get('fromDate').value;
    let toDate = this.filterForm.get('toDate').value;
    if (fromPrice == '') {
      fromPrice = '0';
    }
    if (toPrice == '') {
      toPrice = '99999999';
    }
    if (fromDate == '') {
      fromDate = '1900-01-01';
    }
    if (toDate == '') {
      toDate = '9999-01-01';
    }
    this.orderEsService.getOrderEsByWorkerFilter(this.route.snapshot.paramMap.get('login'), serviceType, fromPrice, toPrice, fromDate, toDate)
      .subscribe(data => {
        this.clientFinishedOrders = data;
        this.getAmountOfMoney();
        this.redirectToOrders();
      }, errorCode =>
        this.statusCode = errorCode);
  }

  getOrders() {
    this.orderService.getOrdersByClientLogin(this.clientSource.login)
      .subscribe(
        data => {
          this.clientOrders = data;
          this.checkForPayment();
        },
        errorCode => this.statusCode);
  }


  getFinishedOrders() {
    this.orderEsService.getOrderEsByClientLogin(this.clientSource.login)
      .subscribe(
        data => {
          this.clientFinishedOrders = data;
          this.checkForPayment();
        },
        errorCode => this.statusCode);
  }

  checkForPayment() {
    for (const order of this.clientOrders) {
      if (order.status == 0) {
        this.dialog.open(ClientPaymentComponent, {
          width: '400px',
          data: {
            order: order
          }
        });
      }
    }
  }

  getAmountOfMoney() {
    this.money = 0;
    for (const order of this.clientFinishedOrders){
      this.money = this.money + order.totalPrice;
    }
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

  preProcessConfigurations() {
    this.statusCode = null;
  }

}
