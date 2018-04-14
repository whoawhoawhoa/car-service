import { Component, OnInit } from '@angular/core';
import {FormControl, FormGroup, Validators} from '@angular/forms';
import {ActivatedRoute, Router} from '@angular/router';
import {Admin} from '../../table-classes/admin';
import {Price} from '../../table-classes/price';
import {PriceService} from '../../services/price.service';
import {CarType} from '../../table-classes/car-type';
import {CarTypeService} from '../../services/car-type.service';
import {AdminService} from '../../services/admin.service';
import {UserService} from '../../services/user.service';
import {User} from '../../table-classes/user';

@Component({
  selector: 'app-lk-admin',
  templateUrl: './lk-admin.component.html',
  styleUrls: ['./lk-admin.component.css']
})
export class LkAdminComponent implements OnInit {
  statusCodeRefresh: number;
  statusCode: number;
  statusCodeCarType: number;
  statusCodePrice: number;
  carTypes?: CarType[];
  carTypeSource?: CarType;
  prices: Price[];
  adminSource: Admin;
  userSource: User;
  requestProcessing = false;
  adminIdToUpdate = null;
  carTypeIdToUpdate = null;
  processValidation = false;
  processValidationRefresh = false;
  processValidationPrice = false;
  processValidationCarType = false;
  priceIdToUpdate = null;

  adminForm = new FormGroup({
    login: new FormControl('', Validators.required),
    password: new FormControl('', Validators.required)
  });

  priceForm = new FormGroup({
    serviceType: new FormControl('', Validators.required),
    price: new FormControl('', Validators.required),
    carType: new FormControl('', Validators.required)
  });

  carTypeForm = new FormGroup(
    {
      carType: new FormControl('', Validators.required)
    }
  );

  constructor(private carTypeService: CarTypeService, private priceService: PriceService,
              private adminService: AdminService, private route: ActivatedRoute,
              private router: Router, private userService: UserService) { }

  ngOnInit() {
    this.carTypeService.getAllCarTypes()
      .subscribe(data => this.carTypes = data);
    this.getPrices();
    this.getAdmin(this.route.snapshot.paramMap.get('login'), this.route.snapshot.paramMap.get('password'));
    this.getUser(this.route.snapshot.paramMap.get('login'), this.route.snapshot.paramMap.get('password'));
    this.loadAdminToEdit();
  }

  getAdmin(login: string, password: string) {
    this.adminService.getAdmin(login, password)
      .subscribe(
        data => this.adminSource = data,
        errorCode => this.statusCode);
  }

  getUser(login: string, password: string) {
    this.userService.getUser(login, password)
      .subscribe(
        data => {this.userSource = data; },
        errorCode => this.statusCode);
  }

  onAdminFormSubmit() {
    this.processValidationRefresh = true;
    if (this.adminForm.invalid) {
      return; // Validation failed, exit from method.
    }
    // Form is valid, now perform update
    this.preProcessConfigurations();
    const login = this.adminForm.get('login').value;
    const password = this.adminForm.get('password').value;
    // Handle update client
    const admin = new Admin(this.adminIdToUpdate, login, password, this.userSource);
    this.adminService.updateAdmin(admin)
      .subscribe(successCode => {
        this.statusCodeRefresh = successCode;
        this.getAdmin(login, password);
        this.adminSource = admin;
        this.router.navigate(['lkadmin/' + login + '/' + password]);
        this.loadAdminToEdit();
        this.backToCreateAdmin();
      }, errorCode =>
        this.statusCodeRefresh = errorCode);
  }

  loadAdminToEdit() {
    this.preProcessConfigurations();
    if (this.adminSource == null) {
      this.adminService.getAdmin(this.route.snapshot.paramMap.get('login'), this.route.snapshot.paramMap.get('password'))
        .subscribe(admin => {
            this.adminIdToUpdate = admin.id;
            this.adminForm.setValue({
              login: admin.login,
              password: admin.password});
            this.processValidationRefresh = true;
            this.requestProcessing = false;
          },
          errorCode =>  this.statusCodeRefresh = errorCode);
    } else {
      this.adminService.getAdmin(this.adminSource.login, this.adminSource.password)
        .subscribe(admin => {
            this.adminIdToUpdate = admin.id;
            this.adminForm.setValue({
              login: admin.login,
              password: admin.password });
            this.processValidationRefresh = true;
            this.requestProcessing = false;
          },
          errorCode =>  this.statusCodeRefresh = errorCode);
    }
  }

  deleteAdmin(adminId: string) {
    this.preProcessConfigurations();
    if (this.adminSource.id !== 1) {
      this.adminService.deleteAdminById(adminId)
        .subscribe(successCode => {
            this.statusCodeRefresh = successCode;
          },
          errorCode => this.statusCodeRefresh = errorCode);
    } else {
      alert('You can\'t delete default admin');
    }
  }

  getPrices() {
    this.priceService.getPrices()
      .subscribe(
        data => this.prices = data,
        errorCode => this.statusCode);
  }

  // Handle create and update admin
  onPriceFormSubmit() {
    this.carTypeSource = this.carTypes[0];
    this.processValidationPrice = true;
    if (this.priceForm.invalid) {
      return; // Validation failed, exit from method.
    }
    // Form is valid, now perform create or update
    this.preProcessConfigurations();
    const serviceType = this.priceForm.get('serviceType').value;
    const priceField = this.priceForm.get('price').value;
    const carType = this.priceForm.get('carType').value;
    this.carTypeSource.id = carType;
    if (this.priceIdToUpdate === null) {
      // Handle create admin
      const price = new Price(null, serviceType, priceField, this.carTypeSource);
      this.priceService.createPrice(price)
        .subscribe(successCode => {
            this.statusCodePrice = successCode;
            this.getPrices();
            this.backToCreatePrice();
          },
          errorCode => this.statusCodePrice = errorCode);
    } else {
      // Handle update admin
      const price = new Price(this.priceIdToUpdate, serviceType, priceField, this.carTypeSource);
      this.priceService.updatePrice(price)
        .subscribe(successCode => {
            this.statusCodePrice = successCode;
            this.getPrices();
            this.backToCreatePrice();
          },
          errorCode => this.statusCodePrice = errorCode);
    }
  }
   // Load admin by id to edit
  loadPriceToEdit(id: string) {
    this.carTypeSource = this.carTypes[0];
    this.preProcessConfigurations();
    this.priceService.getPriceById(id)
      .subscribe(price => {
          this.priceIdToUpdate = price.id;
          this.priceForm.setValue({ serviceType: price.serviceType, price: price.price, carType: price.carType.id });
          this.processValidationPrice = true;
          this.requestProcessing = false;
        },
        errorCode =>  this.statusCodePrice = errorCode);
  }
  // Delete admin
  deletePrice(priceId: string) {
    this.preProcessConfigurations();
    this.priceService.deletePriceById(priceId)
      .subscribe(successCode => {
          this.statusCode = successCode;
          this.getPrices();
          this.backToCreatePrice();
        },
        errorCode => this.statusCodePrice = errorCode);
  }

  getCarTypes() {
    this.carTypeService.getAllCarTypes()
      .subscribe(
        data => this.carTypes = data,
        errorCode => this.statusCodeCarType);
  }

  // Handle create and update admin
  onCarTypeFormSubmit() {
    this.processValidationCarType = true;
    if (this.carTypeForm.invalid) {
      return; // Validation failed, exit from method.
    }
    // Form is valid, now perform create or update
    this.preProcessConfigurations();
    const carTypeField = this.carTypeForm.get('carType').value;
    if (this.carTypeIdToUpdate === null) {
      // Handle create admin
      const carType = new CarType(null, carTypeField);
      this.carTypeService.createCarType(carType)
        .subscribe(successCode => {
            this.statusCodeCarType = successCode;
            this.getCarTypes();
            this.backToCreateCarType();
          },
          errorCode => this.statusCodeCarType = errorCode);
    } else {
      // Handle update admin
      const carType = new CarType(this.carTypeIdToUpdate, carTypeField);
      this.carTypeService.updateCarType(carType)
        .subscribe(successCode => {
            this.statusCode = successCode;
            this.getPrices();
            this.getCarTypes();
            this.backToCreateCarType();
          },
          errorCode => this.statusCodeCarType = errorCode);
    }
  }
  // Load admin by id to edit
  loadCarTypeToEdit(id: string) {
    this.preProcessConfigurations();
    this.carTypeService.getCarTypeById(id)
      .subscribe(carType => {
          this.carTypeIdToUpdate = carType.id;
          this.carTypeForm.setValue({ carType: carType.carType });
          this.processValidationCarType = true;
          this.requestProcessing = false;
        },
        errorCode =>  this.statusCodeCarType = errorCode);
  }
  // Delete admin
  deleteCarType(carTypeId: string) {
    this.preProcessConfigurations();
    this.carTypeService.deleteCarTypeById(carTypeId)
      .subscribe(successCode => {
          this.statusCode = successCode;
          this.getPrices();
          this.getCarTypes();
          this.backToCreateCarType();
        },
        errorCode => this.statusCodeCarType = errorCode);
  }

  redirectToOrders() {
    this.router.navigate(['/all-orders/' + this.adminSource.login + '/' + this.adminSource.password]);
  }


  preProcessConfigurations() {
    this.statusCode = null;
    this.statusCodeCarType = null;
    this.statusCodePrice = null;
    this.statusCodeRefresh = null;
    this.requestProcessing = true;
  }

  backToCreatePrice() {
    this.priceIdToUpdate = null;
    this.priceForm.reset();
    this.processValidationPrice = false;
  }


  backToCreateAdmin() {
    this.adminIdToUpdate = null;
    this.adminForm.reset();
    this.processValidationRefresh = false;
  }

  backToCreateCarType() {
    this.carTypeIdToUpdate = null;
    this.carTypeForm.reset();
    this.processValidationCarType = false;
  }
}
