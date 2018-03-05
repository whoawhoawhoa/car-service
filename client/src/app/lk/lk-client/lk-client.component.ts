import {Component, OnInit} from '@angular/core';
import {FormControl, FormGroup, Validators} from '@angular/forms';
import {ActivatedRoute} from '@angular/router';
import {ClientService} from '../../services/client.service';
import {Client} from '../../table-classes/client';

import {Car} from '../../table-classes/car';
import {Order} from '../../table-classes/order';
import {OrderService} from '../../services/order.service';
import {CarService} from '../../services/car.service';
import {CarType} from '../../table-classes/car-type';
import {CarTypeService} from '../../services/car-type.service';

@Component({
  selector: 'app-lk-client',
  templateUrl: './lk-client.component.html',
  styleUrls: ['./lk-client.component.css']
})


export class LkClientComponent implements OnInit {

  clientSource: Client;
  clientCars: Car[];
  carSource: Car;
  carTypeSource: CarType;
  carIdToUpdate = null;
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
    city: new FormControl('', Validators.required)
  });

  carForm = new FormGroup({
    number: new FormControl('', Validators.required),
    brand: new FormControl('', Validators.required),
    colour: new FormControl('', Validators.required)
  });

  newCarForm = new FormGroup({
    number: new FormControl('', Validators.required),
    brand: new FormControl('', Validators.required),
    color: new FormControl('', Validators.required)
  });

  constructor(private clientService: ClientService, private carService: CarService,
              private orderService: OrderService, private carTypeService: CarTypeService, private route: ActivatedRoute) { }

  ngOnInit() {
    this.getClient(this.route.snapshot.paramMap.get('login'), this.route.snapshot.paramMap.get('password'));
    this.loadClientToEdit();


  }

  getClient(login: string, password: string) {
    this.clientService.getClientByLoginAndPassword(login, password)
      .subscribe(
        data => {this.clientSource = data;
          this.getCars();
          this.getOrders(); },
        errorCode => this.statusCode);
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
    // Handle update client
    const client = new Client(this.clientIdToUpdate, login, password, name, fName, pnumber, city, 0);
    this.clientService.updateClient(client)
      .subscribe(successCode => {
        this.statusCode = successCode;
        console.log(this.statusCode);
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
              city: client.city});
            this.processValidation = true;
            this.requestProcessing = false;
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
              city: client.city});
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
    console.log(this.clientSource.login);
    this.carService.getCarsByClientLogin(this.clientSource.login)
      .subscribe(
        data => this.clientCars = data,
        errorCode => this.statusCode);
  }

  onCarsFormSubmit() {
    this.processValidation = true;
    if (this.carForm.invalid) {
      return; // Validation failed, exit from method.
    }
    // Form is valid, now perform create or update
    this.preProcessConfigurations();
    let number = this.carForm.get('number').value;
    let brand = this.carForm.get('brand').value;
    let color = this.carForm.get('colour').value;
    if (this.carIdToUpdate === null) {
      // Handle create car
      let car = new Car(null, number, brand, color, this.clientSource, new CarType(2, 'SEDAN'));
      this.carService.createCar(car)
        .subscribe(successCode => {
            this.statusCode = successCode;
            this.getCars();
            this.backToCreateCar();
          },
          errorCode => this.statusCode = errorCode);
    } else {
      // Handle update car
      let car = new Car(this.carIdToUpdate, number, brand, color, this.clientSource, new CarType(2, 'SEDAN'));
      this.carService.updateCar(car)
        .subscribe(successCode => {
            this.statusCode = successCode;
            this.getCars();
            this.backToCreateCar();
          },
          errorCode => this.statusCode = errorCode);
    }
  }

  loadCarToEdit() {
    this.preProcessConfigurations();
    if (this.carSource == null) {
      this.carService.getCarById(this.route.snapshot.paramMap.get('id'))
        .subscribe(car => {
            this.carIdToUpdate = car.id;
            this.carForm.setValue({
              number: car.number,
              brand: car.brand,
              color: car.color});
            this.processValidation = true;
            this.requestProcessing = false;
          },
          errorCode =>  this.statusCode = errorCode);
    } else {
      this.carService.getCarById(this.carSource.id.toString())
        .subscribe(car => {
            this.carIdToUpdate = car.id;
            this.carForm.setValue({
              number: car.number,
              brand: car.brand,
              color: car.color});
            this.processValidation = true;
            this.requestProcessing = false;
          },
          errorCode =>  this.statusCode = errorCode);
    }
  }

  deleteCar(carId: string) {
    this.preProcessConfigurations();
    this.carService.deleteCarById(carId)
      .subscribe(successCode => {
          this.statusCode = successCode;
          this.getCars();
          this.backToCreateCar();
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
    // Handle create client
    const car = new Car(null, number, brand, color, this.clientSource, new CarType(1, 'SEDAN'));
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

  preProcessConfigurations() {
    this.statusCode = null;
    this.requestProcessing = true;
  }


  backToCreateClient() {
    this.clientIdToUpdate = null;
    this.clientForm.reset();
    this.processValidation = false;
  }

  backToCreateCar() {
    this.carIdToUpdate = null;
    this.carForm.reset();
    this.processValidation = false;
  }
}
