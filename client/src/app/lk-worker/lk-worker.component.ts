import { Component, OnInit } from '@angular/core';
import {FormControl, FormGroup, Validators} from "@angular/forms";
import {LkWorkerService} from "../lk-worker.service";
import {Worker} from "../worker";
import {ActivatedRoute} from "@angular/router";

@Component({
  selector: 'app-lk-worker',
  templateUrl: './lk-worker.component.html',
  styleUrls: ['./lk-worker.component.css']
})
export class LkWorkerComponent implements OnInit {

  workerSource: Worker;
  statusCode: number;
  requestProcessing = false;
  workerIdToUpdate = null;
  processValidation = false;

  workerForm = new FormGroup({
    login: new FormControl('', Validators.required),
    password: new FormControl('', Validators.required),
    name: new FormControl('', Validators.required),
    fName: new FormControl('', Validators.required),
    pnumber: new FormControl('', Validators.required),
    city: new FormControl('', Validators.required),
    status: new FormControl('', Validators.required)
  });

  constructor(private workerService: LkWorkerService, private route: ActivatedRoute) { }

  ngOnInit() {
    this.getWorker(this.route.snapshot.paramMap.get('login'), this.route.snapshot.paramMap.get('password'));
    this.loadWorkerToEdit();
  }

  getWorker(login : string, password: string) {
    this.workerService.getWorker(login, password)
      .subscribe(
        data => this.workerSource = data,
        errorCode => this.statusCode);
  }

  onWorkerFormSubmit() {
    this.processValidation = true;
    if (this.workerForm.invalid) {
      return; //Validation failed, exit from method.
    }
    //Form is valid, now perform update
    this.preProcessConfigurations();
    let login = this.workerForm.get('login').value;
    let password = this.workerForm.get('password').value;
    let name = this.workerForm.get('name').value;
    let fName = this.workerForm.get('fName').value;
    let pnumber = this.workerForm.get('pnumber').value;
    let city = this.workerForm.get('city').value;
    let status = this.workerForm.get('status').value;
    //Handle update article
    let worker= new Worker(this.workerIdToUpdate, login, password, name, fName, pnumber, city, 0, status);
    this.workerService.updateWorker(worker)
      .subscribe(successCode => {
        this.statusCode = successCode;
        this.getWorker(login, password);
        this.workerSource = worker;
        this.loadWorkerToEdit();
        this.backToCreateArticle();
      }, errorCode => this.statusCode = errorCode);
  }

  loadWorkerToEdit() {
    this.preProcessConfigurations();
    if(this.workerSource == null)
    {
      this.workerService.getWorker(this.route.snapshot.paramMap.get('login'), this.route.snapshot.paramMap.get('password'))
        .subscribe(worker => {
            this.workerIdToUpdate = worker.id;
            this.workerForm.setValue({
              login: worker.login,
              password: worker.password,
              name: worker.name,
              fName: worker.fname,
              pnumber: worker.pnumber,
              city: worker.city,
              status: worker.status});
            this.processValidation = true;
            this.requestProcessing = false;
          },
          errorCode =>  this.statusCode = errorCode);
    }
    else
    {
      this.workerService.getWorker(this.workerSource.login, this.workerSource.password)
        .subscribe(worker => {
            this.workerIdToUpdate = worker.id;
            this.workerForm.setValue({
              login: worker.login,
              password: worker.password,
              name: worker.name,
              fName: worker.fname,
              pnumber: worker.pnumber,
              city: worker.city,
              status: worker.status});
            this.processValidation = true;
            this.requestProcessing = false;
          },
          errorCode =>  this.statusCode = errorCode);
    }
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
    this.workerIdToUpdate = null;
    this.workerForm.reset();
    this.processValidation = false;
  }

}
