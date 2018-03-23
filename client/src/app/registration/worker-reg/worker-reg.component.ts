import { Component, OnInit } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import { WorkerService } from '../../services/worker.service';
import { Worker } from '../../table-classes/worker';
import {Router} from '@angular/router';
import {UserService} from '../../services/user.service';
import {User} from '../../table-classes/user';


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
    city: new FormControl('', Validators.required),
    email: new FormControl('', Validators.required)
  });

  constructor(private router: Router, private workerService: WorkerService, private userService: UserService) { }

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
    const email = this.workerForm.get('email').value;
    // Handle create worker
    const user = new User(null, login, password, 3);
    this.userService.createUser(user);
    const worker = new Worker(null, login, password, name, fname, pnumber, city,
      null, 3, email, user);
    this.workerService.createWorker(worker)
      .subscribe(successCode => {
          this.statusCode = successCode;
          this.router.navigate(['/lkworker/' + login + '/' + password]);
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
