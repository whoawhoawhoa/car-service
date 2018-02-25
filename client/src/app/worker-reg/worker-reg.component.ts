import { Component, OnInit } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import { WorkerService } from '../worker.service';
import { Worker } from '../worker';


@Component({
  selector: 'app-worker-reg',
  templateUrl: './worker-reg.component.html',
  styleUrls: ['./worker-reg.component.css']
})
export class WorkerRegComponent implements OnInit {
  statusCode: number;
  processValidation = false;

  workerForm = new FormGroup({
    login: new FormControl('', Validators.required),
    password: new FormControl('', Validators.required),
    name: new FormControl('', Validators.required),
    fname: new FormControl('', Validators.required),
    pnumber: new FormControl('', Validators.required),
    city: new FormControl('', Validators.required)
  });

  constructor(private workerService: WorkerService) { }

  ngOnInit() {
  }

  onWorkerFormSubmit() {
    this.processValidation = true;
    if (this.workerForm.invalid) {
      return; // Validation failed, exit from method.
    }
    // Form is valid, now perform create
    this.preProcessConfigurations();
    const login = this.workerForm.get('login').value.trim();
    const password = this.workerForm.get('password').value.trim();
    const name = this.workerForm.get('name').value.trim();
    const fname = this.workerForm.get('fname').value.trim();
    const pnumber = this.workerForm.get('pnumber').value.trim();
    const city = this.workerForm.get('city').value.trim();
    // Handle create worker
    const worker = new Worker(null, login, password, name, fname, pnumber, city,
      null, null, null, null, null);
    this.workerService.createWorker(worker)
      .subscribe(successCode => {
          this.statusCode = successCode;
          this.backToCreateWorker();
        },
        errorCode => this.statusCode = errorCode);
  }
  // Perform preliminary processing configurations
  preProcessConfigurations() {
    this.statusCode = null;
  }
  // Go back from update to create
  backToCreateWorker() {
    this.workerForm.reset();
    this.processValidation = false;
  }
}
