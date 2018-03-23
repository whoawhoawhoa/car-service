import {Component, Input, OnInit} from '@angular/core';
import {OrderService} from '../../../services/order.service';
import {Order} from '../../../table-classes/order';
import {ActivatedRoute, Router} from '@angular/router';
import {WorkerService} from '../../../services/worker.service';
import {Worker} from '../../../table-classes/worker';
import {FormControl, FormGroup} from '@angular/forms';

@Component({
  selector: 'app-worker-orders',
  templateUrl: './worker-orders.component.html',
  styleUrls: ['./worker-orders.component.css']
})
export class WorkerOrdersComponent implements OnInit {
  allOrders: Order[];
  workerSource: Worker;
  statusCode: number;


  readyForm = new FormGroup({
    ready: new FormControl('')
  });

  constructor(private orderService: OrderService, private route: ActivatedRoute, private workerService: WorkerService,
              private router: Router) { }

  ngOnInit() {
    this.getWorker(this.route.snapshot.paramMap.get('login'), this.route.snapshot.paramMap.get('password'));
    this.getAllOrders();
  }

  getWorker(login: string, password: string) {
    this.workerService.getWorkerByLoginAndPassword(login, password)
      .subscribe(
        data => this.workerSource = data,
        errorCode => this.statusCode);
  }


  getAllOrders() {
    this.orderService.getOrdersByWorkerLogin(this.route.snapshot.paramMap.get('login'))
      .subscribe(
        data => this.allOrders = data,
        errorCode =>  this.statusCode = errorCode);
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
}
