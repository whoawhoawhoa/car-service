import {Component, OnInit} from '@angular/core';
import {ActivatedRoute, Router} from '@angular/router';
import {FormControl, FormGroup, Validators} from '@angular/forms';
import {OrderEsService} from '../../../services/order-es.service';
import {OrderEs} from '../../../table-classes/order-es';
import {AdminService} from '../../../services/admin.service';
import {Admin} from '../../../table-classes/admin';
import {WorkerService} from '../../../services/worker.service';
import {ClientService} from '../../../services/client.service';
import {Client} from '../../../table-classes/client';
import {Worker} from '../../../table-classes/worker';

@Component({
  selector: 'app-all-orders',
  templateUrl: './all-orders.component.html',
  styleUrls: ['./all-orders.component.css']
})


export class AllOrdersComponent implements OnInit {
  allOrders: OrderEs[];
  allWorkers: Worker[];
  allClients: Client[];
  adminSource: Admin;
  processValidation = false;
  statusCode: number;

  filterForm = new FormGroup({
    serviceType: new FormControl('', Validators.required),
    carType: new FormControl('', Validators.required),
    clientLogin: new FormControl('', Validators.required),
    workerLogin: new FormControl('', Validators.required),
    price1: new FormControl('', Validators.required),
    price2: new FormControl('', Validators.required),
    fromDate: new FormControl('', Validators.required),
    toDate: new FormControl('', Validators.required),
    fromClientMark: new FormControl('', Validators.required),
    toClientMark: new FormControl('', Validators.required),
    fromWorkerMark: new FormControl('', Validators.required),
    toWorkerMark: new FormControl('', Validators.required)
  });

  constructor(private orderEsService: OrderEsService, private workerService: WorkerService, private clientService: ClientService,
              private route: ActivatedRoute, private adminService: AdminService, private router: Router) { }

  ngOnInit() {
    this.getAdmin(this.route.snapshot.paramMap.get('login'), this.route.snapshot.paramMap.get('password'));
    this.getAllOrders();
  }

  onFilterFormSubmit() {
    this.preProcessConfigurations();
    const serviceType = this.filterForm.get('serviceType').value;
    const carType = this.filterForm.get('carType').value;
    const clientLogin = this.filterForm.get('clientLogin').value;
    const workerLogin = this.filterForm.get('workerLogin').value;
    let fromPrice = this.filterForm.get('price1').value;
    let toPrice = this.filterForm.get('price2').value;
    let fromDate = this.filterForm.get('fromDate').value;
    let toDate = this.filterForm.get('toDate').value;
    let fromClientMark = this.filterForm.get('fromClientMark').value;
    let toClientMark = this.filterForm.get('toClientMark').value;
    let fromWorkerMark = this.filterForm.get('fromWorkerMark').value;
    let toWorkerMark = this.filterForm.get('toWorkerMark').value;
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
    if (fromClientMark == '') {
      fromClientMark = '1';
    }
    if (toClientMark == '') {
      toClientMark = '5';
    }
    if (fromWorkerMark == '') {
      fromWorkerMark = '1';
    }
    if (toWorkerMark == '') {
      toWorkerMark = '5';
    }
    this.orderEsService.getOrderEsByFilter(serviceType, carType, clientLogin, workerLogin,
      fromPrice, toPrice, fromDate, toDate, fromClientMark, toClientMark, fromWorkerMark, toWorkerMark)
      .subscribe(data => {
        this.allOrders = data;
        this.refresh();
      }, errorCode =>
        this.statusCode = errorCode);
  }

  getAdmin(login: string, password: string) {
    this.adminService.getAdminByLoginAndPassword(login, password)
      .subscribe(
        data => this.adminSource = data,
        errorCode => this.statusCode);
  }


  getAllOrders() {
    this.orderEsService.getAllOrders()
      .subscribe(
        data => this.allOrders = data,
        errorCode =>  this.statusCode = errorCode);
  }

  getAllWorkers() {
    this.workerService.getAllWorkers()
      .subscribe(
        data => this.allWorkers = data,
        errorCode =>  this.statusCode = errorCode);
  }

  getAllClients() {
    this.clientService.getAllClients()
      .subscribe(
        data => this.allClients = data,
        errorCode =>  this.statusCode = errorCode);
  }


  refresh() {
    this.router.navigate(['/all-orders/' + this.adminSource.login + '/' + this.adminSource.password]);
  }

  redirectToLK() {
    this.router.navigate(['/lkadmin/' + this.adminSource.login + '/' + this.adminSource.password]);
  }


  preProcessConfigurations() {
    this.statusCode = null;
  }

}
