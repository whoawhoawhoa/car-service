import { Component, OnInit } from '@angular/core';
import {ActivatedRoute, Router} from '@angular/router';
import {ClientService} from '../services/client.service';
import {Client} from '../table-classes/client';
import {UserService} from '../services/user.service';
import {User} from '../table-classes/user';
import {WorkerService} from '../services/worker.service';
import {Worker} from '../table-classes/worker';
import {Car} from '../table-classes/car';
import {CarService} from '../services/car.service';
import {CarType} from '../table-classes/car-type';
import {CarTypeService} from '../services/car-type.service';
import {PriceService} from '../services/price.service';
import {FormControl, FormGroup, Validators} from '@angular/forms';
import {AvailableOrderService} from '../services/available-order.service';

@Component({
  selector: 'app-main-page',
  templateUrl: './main-page.component.html',
  styleUrls: ['./main-page.component.css']
})
export class MainPageComponent implements OnInit {
  role: number;
  sourceUser: User;
  sourceClient: Client;
  sourceWorker: Worker;
  isUserReceived = false;
  isClient = false;
  processValidation = false;
  priceNames = [];
  cars: Car[];
  carTypes: CarType[];
  statusCode: number;
  showOrderForm = false;
  orderedServiceType = '';
  showService = true;

  constructor(private clientService: ClientService,
              private workerService: WorkerService,
              private route: ActivatedRoute,
              private router: Router,
              private userService: UserService,
              private priceService: PriceService,
              private carService: CarService,
              private carTypeService: CarTypeService,
              private avOrderService: AvailableOrderService) {
    this.carTypes = [];
  }

  orderForm = new FormGroup({
    serviceType: new FormControl('', Validators.required)
  });

  ngOnInit() {
    this.getUser();
  }

  getUser() {
   const login = this.route.snapshot.paramMap.get('login');
   const password = this.route.snapshot.paramMap.get('password');
   if (login == null || password == null) {
     return;
   } else {
     this.userService.getUser(login, password)
       .subscribe(data => {
         this.sourceUser = data;
         this.isUserReceived = true;
         this.role = this.sourceUser.role;
         if (this.role == 2) {
           this.clientService
             .getClientByLoginAndPassword(this.sourceUser.login, this.sourceUser.password)
             .subscribe(new_data => {this.sourceClient = new_data;
                                     this.isClient = true;
                                     this.getCars(); });
         }
         if (this.role == 3) {
           this.workerService
             .getWorkerByLoginAndPassword(this.sourceUser.login, this.sourceUser.password)
             .subscribe(new_data => {this.sourceWorker = new_data; });
         }
       });
   }
  }

  redirectToLK() {
    if (this.role == 2) {
      this.router.navigate(['lkclient/' + this.sourceUser.login + '/' + this.sourceUser.password]);
    }
    if (this.role == 3) {
      this.router.navigate(['lkworker/' + this.sourceUser.login + '/' + this.sourceUser.password]);
    }
  }

  getCars() {
    this.carService.getCarsByClientLogin(this.sourceClient.login)
      .subscribe(cars => {
        this.cars = cars;
        this.getCarTypes();
      });
  }

  getCarTypes() {
    const carTypeNames = [];
    for (let i = 0; i < this.cars.length; i++) {
      carTypeNames[i] = this.cars[i].carType.carType;
    }
    carTypeNames.sort();

    for (let i = 1; i < carTypeNames.length; i++) {
      if (carTypeNames[i] == carTypeNames[i - 1]) {
        carTypeNames.splice(i, 1);
      }
    }

    this.getPrices(carTypeNames);
  }


  getPrices(carTypes: string[]) {
    for (let i = 0; i < carTypes.length; i++) {
      this.priceService.getPricesByCarType(carTypes[i])
        .subscribe(prices => {
          const temporaryArray = prices;
          for (let j = 0; j < temporaryArray.length; j++) {
            this.priceNames.push(temporaryArray[j].serviceType);
          }
          this.priceNames.sort();
          let counter = 1;

          for (let q = 1, k = 1; q < this.priceNames.length; ++q) {
            if (this.priceNames[q] !== this.priceNames[q - 1]) {
              this.priceNames[k++] = this.priceNames[q];
              counter++;
            }
          }
          this.priceNames.length = counter;
        });
    }
  }

  onPriceFormSubmit() {
    this.processValidation = true;
    if (this.orderForm.invalid) {
      return; // Validation failed, exit from method.
    }
    this.showService = false;
    this.showOrderForm = true;
    this.orderedServiceType = this.orderForm.get('serviceType').value;
  }

}
