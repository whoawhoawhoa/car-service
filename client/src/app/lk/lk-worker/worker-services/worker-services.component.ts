import {Component, Input, OnInit} from '@angular/core';
import {Service} from '../../../table-classes/service';
import {ServiceService} from '../../../services/service.service';
import {FormControl, FormGroup, Validators} from '@angular/forms';
import {Price} from '../../../table-classes/price';
import {PriceService} from '../../../services/price.service';
import {Worker} from '../../../table-classes/worker';
import {WorkerService} from '../../../services/worker.service';
import {ActivatedRoute} from '@angular/router';

@Component({
  selector: 'app-worker-services',
  templateUrl: './worker-services.component.html',
  styleUrls: ['./worker-services.component.css']
})
export class WorkerServicesComponent implements OnInit {
  allServices: Service[];
  allPrices: Price[];
  service: Service;
  statusCode: number;
  requestProcessing = false;
  processValidation = false;
  workerSource: Worker;

  serviceForm = new FormGroup({
    coefficient: new FormControl('', Validators.required),
    price: new FormControl('', Validators.required)
  });

  constructor(private serviceService: ServiceService, private priceService: PriceService, private route: ActivatedRoute,
              private workerService: WorkerService) {}

  ngOnInit() {
    this.getWorker(this.route.snapshot.paramMap.get('login'), this.route.snapshot.paramMap.get('password'));
    this.getAllServices();
    this.getAllPrices();
  }

  getWorker(login: string, password: string) {
    this.workerService.getWorkerByLoginAndPassword(login, password)
      .subscribe(
        data => this.workerSource = data,
        errorCode => this.statusCode);
  }

  getAllServices() {
    this.serviceService.getServicesByWorkerLogin(this.route.snapshot.paramMap.get('login'))
      .subscribe(
        data => this.allServices = data,
        errorCode =>  this.statusCode = errorCode);
  }

  getAllPrices() {
    this.priceService.getPrices()
      .subscribe(
        data => {this.allPrices = data;
        },
        errorCode =>  this.statusCode = errorCode);
  }

  onServiceFormSubmit() {
    this.processValidation = true;
    if (this.serviceForm.invalid) {
      return; // Validation failed, exit from method.
    }
    this.preProcessConfigurations();
    const coef = this.serviceForm.get('coefficient').value;
    let pric = this.serviceForm.get('price').value;
    for (const a of this.allPrices) {
      if (a.id == pric) {
        pric = a;
      }
    }
    this.service = new Service(null, coef / pric.price, this.workerSource, pric);
    this.serviceService.createService(this.service).
      subscribe(successCode => {
        this.statusCode = successCode;
        this.getAllServices();
        this.backToCreateService();
      },
      errorCode => this.statusCode = errorCode);
    this.getAllServices();
  }

  deleteService(serviceId: string) {
    this.preProcessConfigurations();
    this.serviceService.deleteServiceById(serviceId).
    subscribe(successCode => {
        this.statusCode = successCode;
        this.getAllServices();
      },
      errorCode => this.statusCode = errorCode);
    this.getAllServices();
    this.backToCreateService();
  }

  preProcessConfigurations() {
    this.statusCode = null;
    this.requestProcessing = true;
  }

  backToCreateService() {
    this.service = null;
    this.serviceForm.reset();
    this.processValidation = false;
  }
}
