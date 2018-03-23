import {Component, OnInit} from '@angular/core';
import {FormControl, FormGroup, Validators} from '@angular/forms';
import {ActivatedRoute, Router} from '@angular/router';
import {ClientService} from '../../services/client.service';
import {Client} from '../../table-classes/client';

import {Car} from '../../table-classes/car';
import {Order} from '../../table-classes/order';
import {OrderService} from '../../services/order.service';
import {CarService} from '../../services/car.service';
import {CarType} from '../../table-classes/car-type';
import {CarTypeService} from '../../services/car-type.service';
import {User} from '../../table-classes/user';
import {UserService} from '../../services/user.service';
import {AvailableOrder} from '../../table-classes/available-order';
import {AvailableOrderService} from '../../services/available-order.service';

@Component({
  selector: 'app-lk-client',
  templateUrl: './lk-client.component.html',
  styleUrls: ['./lk-client.component.css']
})


export class LkClientComponent implements OnInit {
  clientSource: Client;
  userSource: User;
  clientCars: Car[];
  carTypes: CarType[];
  carIdToUpdate = null;
  clientAvOrders: AvailableOrder[];
  clientOrders: Order[];
  statusCode: number;
  requestProcessing = false;
  clientIdToUpdate = null;
  processValidation = false;

  clientForm = new FormGroup({
    login: new FormControl('', Validators.required),
    password: new FormControl('', Validators.required),
    name: new FormControl('', Validators.required),
    fName: new FormControl('', Validators.required),
    pnumber: new FormControl('', Validators.required),
    city: new FormControl('', Validators.required),
    email: new FormControl('', Validators.required)
  });

  newCarForm = new FormGroup({
    number: new FormControl('', Validators.required),
    brand: new FormControl('', Validators.required),
    color: new FormControl('', Validators.required),
    carType: new FormControl('', Validators.required)
  });

  constructor(private clientService: ClientService,
              private carService: CarService,
              private userService: UserService,
              private orderService: OrderService,
              private carTypeService: CarTypeService,
              private route: ActivatedRoute,
              private router: Router,
              private avOrderService: AvailableOrderService) { }

  ngOnInit() {
    this.carTypeService.getAllCarTypes()
      .subscribe(data => this.carTypes = data);
    this.getClient(this.route.snapshot.paramMap.get('login'), this.route.snapshot.paramMap.get('password'));
    this.getUser(this.route.snapshot.paramMap.get('login'), this.route.snapshot.paramMap.get('password'));
    this.loadClientToEdit();
  }

  redirectToMain() {
    this.router.navigate(['/main/' + this.clientSource.login + '/' + this.clientSource.password]);
  }

  getUser(login: string, password: string) {
    this.userService.getUser(login, password)
      .subscribe(
        data => {this.userSource = data; },
        errorCode => this.statusCode);
  }

  getClient(login: string, password: string) {
    this.clientService.getClientByLoginAndPassword(login, password)
      .subscribe(
        data => {this.clientSource = data;
          this.getCars();
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

  onClientFormSubmit() {
    this.processValidation = true;
    if (this.clientForm.invalid) {
      return; // Validation failed, exit from method.
    }
    // Form is valid, now perform update
    this.preProcessConfigurations();
    const login = this.clientForm.get('login').value;
    const password = this.clientForm.get('password').value;
    const name = this.clientForm.get('name').value;
    const fName = this.clientForm.get('fName').value;
    const pnumber = this.clientForm.get('pnumber').value;
    const city = this.clientForm.get('city').value;
    const email = this.clientForm.get('email').value;
    // Handle update client
    const client = new Client(this.clientIdToUpdate, login, password, name, fName, pnumber, city, null, this.userSource, email);
    this.clientService.updateClient(client)
      .subscribe(successCode => {
        this.statusCode = successCode;
        this.getClient(login, password);
        this.clientSource = client;
        this.loadClientToEdit();
        this.backToCreateClient();
      }, errorCode =>
        this.statusCode = errorCode);
  }

  loadClientToEdit() {
    this.preProcessConfigurations();
    if (this.clientSource == null) {
      this.clientService.getClientByLoginAndPassword(this.route.snapshot.paramMap.get('login'),
        this.route.snapshot.paramMap.get('password'))
        .subscribe(client => {
            this.clientIdToUpdate = client.id;
            this.clientForm.setValue({
              login: client.login,
              password: client.password,
              name: client.name,
              fName: client.fname,
              pnumber: client.pnumber,
              city: client.city,
              email: client.email});
            this.processValidation = true;
            this.requestProcessing = false;
            this.clientSource = client;
          },
          errorCode =>  this.statusCode = errorCode);
    } else {
      this.clientService.getClientByLoginAndPassword(this.clientSource.login, this.clientSource.password)
        .subscribe(client => {
            this.clientIdToUpdate = client.id;
            this.clientForm.setValue({
              login: client.login,
              password: client.password,
              name: client.name,
              fName: client.fname,
              pnumber: client.pnumber,
              city: client.city,
              email: client.email});
            this.processValidation = true;
            this.requestProcessing = false;
          },
          errorCode =>  this.statusCode = errorCode);
    }
  }


  deleteClient(clientLogin: string) {
    this.preProcessConfigurations();
    this.clientService.deleteClientByLogin(clientLogin)
      .subscribe(successCode => {
          this.statusCode = successCode;
        },
        errorCode => this.statusCode = errorCode);
  }

  getCars() {
    this.carService.getCarsByClientLogin(this.clientSource.login)
      .subscribe(
        data => this.clientCars = data,
        errorCode => this.statusCode);
  }


  deleteCar(carId: string) {
    this.preProcessConfigurations();
    this.carService.deleteCarById(carId)
      .subscribe(successCode => {
          this.statusCode = successCode;
          this.getCars();
          this.backToCreateClient();
        },
        errorCode => this.statusCode = errorCode);
  }

  onNewCarFormSubmit() {
    this.processValidation = true;
    if (this.newCarForm.invalid) {
      return; // Validation failed, exit from method.
    }
    // Form is valid, now perform create
    this.preProcessConfigurations();
    const number = this.newCarForm.get('number').value.trim();
    const brand = this.newCarForm.get('brand').value.trim();
    const color = this.newCarForm.get('color').value.trim();
    let cartype = this.newCarForm.get('carType').value.trim();
    for (const a of this.carTypes) {
      if (a.id == cartype) {
        cartype = a;
      }
    }
    // Handle create client
    const car = new Car(null, number, brand, color, this.clientSource, cartype);
    this.carService.createCar(car)
      .subscribe(successCode => {
          this.statusCode = successCode;
          this.getCars();
          this.backToCreateClient();
        },
        errorCode => this.statusCode = errorCode);
  }

  getOrders() {
    this.orderService.getOrderByClientLogin(this.clientSource.login)
      .subscribe(
        data => this.clientOrders = data,
        errorCode => this.statusCode);
  }

  refreshAvOrder()
  {
    this.avOrderService.getAllAvailableOrders()
      .subscribe(data => {
        this.clientAvOrders = data;
      })
  }

  preProcessConfigurations() {
    this.statusCode = null;
    this.requestProcessing = true;
  }

  backToCreateClient() {
    this.carIdToUpdate = null;
    this.newCarForm.reset();
    this.clientIdToUpdate = null;
    this.processValidation = false;
  }


}
