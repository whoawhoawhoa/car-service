import {Component, OnInit} from '@angular/core';
import {ActivatedRoute, Router} from '@angular/router';
import {WorkerService} from '../../../services/worker.service';
import {Worker} from '../../../table-classes/worker';
import {FormControl, FormGroup, Validators} from '@angular/forms';
import {OrderEsService} from '../../../services/order-es.service';
import {OrderEs} from '../../../table-classes/order-es';
import {OrderService} from '../../../services/order.service';

@Component({
  selector: 'app-worker-orders',
  templateUrl: './worker-orders.component.html',
  styleUrls: ['./worker-orders.component.css']
})
export class WorkerOrdersComponent implements OnInit {
  allOrders: OrderEs[];
  workerSource: Worker;
  statusCode: number;
  money: number;

  filterForm = new FormGroup({
    serviceType: new FormControl('', Validators.required),
    price1: new FormControl('', Validators.required),
    price2: new FormControl('', Validators.required),
    fromDate: new FormControl('', Validators.required),
    toDate: new FormControl('', Validators.required),
  });

  readyForm = new FormGroup({
    ready: new FormControl('')
  });

  constructor( private orderEsService: OrderEsService, private orderService: OrderService,
              private route: ActivatedRoute, private workerService: WorkerService, private router: Router) { }

  ngOnInit() {
    this.getWorker(this.route.snapshot.paramMap.get('login'), this.route.snapshot.paramMap.get('password'));
    this.getAllOrders();
  }

  getWorker(login: string, password: string) {
    this.workerService.getWorkerByLoginAndPassword(login, password)
      .subscribe(
        data => {
          this.workerSource = data;
            this.getAmountOfMoney();
        },
        errorCode => this.statusCode);
  }


  getAllOrders() {
    this.orderService.getOrdersByWorkerLogin(this.route.snapshot.paramMap.get('login'))
      .subscribe(
        data => this.allOrders = data,
        errorCode =>  this.statusCode = errorCode);
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
        this.allOrders = data;
        this.getAmountOfMoney();
        this.redirectToOrders();
      }, errorCode =>
        this.statusCode = errorCode);
  }

  getAmountOfMoney() {
    this.money = 0;
    for (const order of this.allOrders){
      this.money = this.money + order.totalPrice;
    }
  }

  redirectToOrders() {
    this.router.navigate(['/worker-orders/' + this.workerSource.login + '/' + this.workerSource.password]);
  }

  redirectToLK() {
    this.router.navigate(['/lkworker/' + this.workerSource.login + '/' + this.workerSource.password]);
  }

  preProcessConfigurations() {
    this.statusCode = null;
  }

  updateStatus() {
    const ready = this.readyForm.get('ready').value;
    if (ready) {
      this.workerSource.status = 1;
      this.workerService.updateWorker(this.workerSource)
        .subscribe(sc => {
          this.statusCode = sc;
        }, errorCode =>
          this.statusCode = errorCode);
    } else {
      this.workerSource.status = 0;
      this.workerService.updateWorker(this.workerSource)
        .subscribe(sc => {
          this.statusCode = sc;
        }, errorCode =>
          this.statusCode = errorCode);
    }
  }

  executedAndCancelledOrders() {
    return this.allOrders.filter(
      order => order.status == 5 || order.status == 6);
  }

  paidOrders() {
    return this.allOrders.filter(
      order => order.status == 1 || order.status == 4 || order.status == 2 || order.status == 3);
  }
}
