import { Component, OnInit } from '@angular/core';
import {FormControl, FormGroup, Validators} from '@angular/forms';
import {LkWorkerService} from '../lk-worker.service';
import {Worker} from '../worker';

@Component({
  selector: 'app-lk-worker',
  templateUrl: './lk-worker.component.html',
  styleUrls: ['./lk-worker.component.css']
})
export class LkWorkerComponent implements OnInit {

  workerSource: Worker;
  statusCode: number;
  requestProcessing = false;
  articleIdToUpdate = null;
  processValidation = false;

  workerForm = new FormGroup({
    login: new FormControl('', Validators.required),
    password: new FormControl('', Validators.required),
    name: new FormControl('', Validators.required),
    fName: new FormControl('', Validators.required),
    phoneNumber: new FormControl('', Validators.required),
    city: new FormControl('', Validators.required),
    status: new FormControl('', Validators.required)
  });

  constructor(private workerService: LkWorkerService) { }

  ngOnInit() {
    this.getWorker();
    this.loadWorkerToEdit();
  }

  getWorker() {
    this.workerService.getWorker()
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
    const phoneNumber = this.workerForm.get('phoneNumber').value;
    const city = this.workerForm.get('city').value;
    const status = this.workerForm.get('status').value;
    // Handle update article
    const worker = new Worker(this.articleIdToUpdate, login, password, name, fName, phoneNumber, city, 0, status);
    this.workerService.updateWorker(worker)
      .subscribe(successCode => {
        this.statusCode = successCode;
        this.getWorker();
        this.loadWorkerToEdit();
        this.backToCreateArticle();
      }, errorCode => this.statusCode = errorCode);
  }

  loadWorkerToEdit() {
    this.preProcessConfigurations();
    this.workerService.getWorker()
      .subscribe(worker => {
          this.articleIdToUpdate = worker.id;
          this.workerForm.setValue({
            login: worker.login,
            password: worker.password,
            name: worker.name,
            fName: worker.fname,
            phoneNumber: worker.phone_number,
            city: worker.city,
            status: worker.status});
          this.processValidation = true;
          this.requestProcessing = false;
        },
        errorCode =>  this.statusCode = errorCode);
  }

  deleteWorker(workerId: string) {
    this.preProcessConfigurations();
    this.workerService.deleteWorkerById(workerId)
      .subscribe(successCode => {
          this.statusCode = successCode;
        },
        errorCode => this.statusCode = errorCode);
  }

  preProcessConfigurations() {
    this.statusCode = null;
    this.requestProcessing = true;
  }


  backToCreateArticle() {
    this.articleIdToUpdate = null;
    this.workerForm.reset();
    this.processValidation = false;
  }

}
