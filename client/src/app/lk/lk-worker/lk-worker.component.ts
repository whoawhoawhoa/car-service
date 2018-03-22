import { Component, OnInit } from '@angular/core';
import {FormControl, FormGroup, Validators} from '@angular/forms';
import {Worker} from '../../table-classes/worker';
import {ActivatedRoute, Router} from '@angular/router';
import {WorkerService} from '../../services/worker.service';
import {User} from '../../table-classes/user';
import {UserService} from '../../services/user.service';

@Component({
  selector: 'app-lk-worker',
  templateUrl: './lk-worker.component.html',
  styleUrls: ['./lk-worker.component.css']
})
export class LkWorkerComponent implements OnInit {
  workerSource: Worker;
  userSource: User;
  statusCode: number;
  requestProcessing = false;
  workerIdToUpdate = null;
  processValidation = false;

  readyForm = new FormGroup({
    ready: new FormControl('')
  });

  workerForm = new FormGroup({
    login: new FormControl('', Validators.required),
    password: new FormControl('', Validators.required),
    name: new FormControl('', Validators.required),
    fName: new FormControl('', Validators.required),
    pnumber: new FormControl('', Validators.required),
    city: new FormControl('', Validators.required),
    status: new FormControl('', Validators.required),
    email: new FormControl('', Validators.required)
  });

  constructor(private workerService: WorkerService,
              private route: ActivatedRoute,
              private router: Router,
              private userService: UserService) { }

  ngOnInit() {
    this.getWorker(this.route.snapshot.paramMap.get('login'), this.route.snapshot.paramMap.get('password'));
    this.getUser(this.route.snapshot.paramMap.get('login'), this.route.snapshot.paramMap.get('password'));
    this.loadWorkerToEdit();
  }

  getUser(login: string, password: string) {
    this.userService.getUser(login, password)
      .subscribe(
        data => {this.userSource = data; },
        errorCode => this.statusCode);
  }

  getWorker(login: string, password: string) {
    this.workerService.getWorkerByLoginAndPassword(login, password)
      .subscribe(
        data => this.workerSource = data,
        errorCode => this.statusCode);
  }

  onWorkerFormSubmit() {
    this.processValidation = true;
    if (this.workerForm.invalid) {
      return; // Validation failed, exit from method.
    }
    // Form is valid, now perform update
    this.preProcessConfigurations();
    const login = this.workerForm.get('login').value;
    const password = this.workerForm.get('password').value;
    const name = this.workerForm.get('name').value;
    const fName = this.workerForm.get('fName').value;
    const pnumber = this.workerForm.get('pnumber').value;
    const city = this.workerForm.get('city').value;
    const status = this.workerForm.get('status').value;
    const email = this.workerForm.get('email').value;
    // Handle update worker
    const worker = new Worker(this.workerIdToUpdate, login, password, name, fName,
      pnumber, city, 0, status, this.userSource, email);
    this.workerService.updateWorker(worker)
      .subscribe(successCode => {
        this.statusCode = successCode;
        this.getWorker(login, password);
        this.workerSource = worker;
        this.router.navigate(['/lkworker/' + login + '/' + password]);
        this.loadWorkerToEdit();
        this.backToCreateWorker();
      }, errorCode => this.statusCode = errorCode);
  }

  loadWorkerToEdit() {
    this.preProcessConfigurations();
    if (this.workerSource == null) {
      this.workerService.getWorkerByLoginAndPassword(this.route.snapshot.paramMap.get('login'),
        this.route.snapshot.paramMap.get('password'))
        .subscribe(worker => {
            this.workerIdToUpdate = worker.id;
            this.workerForm.setValue({
              login: worker.login,
              password: worker.password,
              name: worker.name,
              fName: worker.fname,
              pnumber: worker.pnumber,
              city: worker.city,
              status: worker.status,
              email: worker.email});
            this.processValidation = true;
            this.requestProcessing = false;
            this.workerSource = worker;
          },
          errorCode =>  this.statusCode = errorCode);
    } else {
      this.workerService.getWorkerByLoginAndPassword(this.workerSource.login, this.workerSource.password)
        .subscribe(worker => {
            this.workerIdToUpdate = worker.id;
            this.workerForm.setValue({
              login: worker.login,
              password: worker.password,
              name: worker.name,
              fName: worker.fname,
              pnumber: worker.pnumber,
              city: worker.city,
              status: worker.status,
              email: worker.email});
            this.processValidation = true;
            this.requestProcessing = false;
          },
          errorCode =>  this.statusCode = errorCode);
    }
  }

  redirectToMain() {
    this.router.navigate(['/main/' + this.workerSource.login + '/' + this.workerSource.password]);
  }

  deleteWorker(workerLogin: string) {
    this.preProcessConfigurations();
    this.workerService.deleteWorkerByLogin(workerLogin)
      .subscribe(successCode => {
          this.statusCode = successCode;
        },
        errorCode => this.statusCode = errorCode);
  }

  updateStatus() {
    const ready = this.readyForm.get('ready').value;
    if (ready) {
      this.workerSource.status = 1;
      this.workerService.updateWorker(this.workerSource)
        .subscribe(sc => {
        this.statusCode = sc;
        this.backToCreateWorker();
        this.loadWorkerToEdit();
      }, errorCode =>
        this.statusCode = errorCode);
    } else {
      this.workerSource.status = 0;
      this.workerService.updateWorker(this.workerSource)
        .subscribe(sc => {
          this.statusCode = sc;
          this.backToCreateWorker();
          this.loadWorkerToEdit();
        }, errorCode =>
          this.statusCode = errorCode);
    }
  }

  preProcessConfigurations() {
    this.statusCode = null;
    this.requestProcessing = true;
  }


  backToCreateWorker() {
    this.workerIdToUpdate = null;
    this.workerForm.reset();
    this.processValidation = false;
  }

}
