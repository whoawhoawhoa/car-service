import { Component, OnInit } from '@angular/core';
import {FormControl, FormGroup, Validators} from '@angular/forms';
import {Worker} from '../worker';
import {AuthorizeService} from '../authorize.service';
import {Router} from "@angular/router";

@Component({
  selector: 'app-worker-auth',
  templateUrl: './worker-auth.component.html',
  styleUrls: ['./worker-auth.component.css']
})
export class WorkerAuthComponent implements OnInit {
  statusCode: number;
  worker: Worker;

  workerAuthForm = new FormGroup({
    login: new FormControl('', Validators.required),
    password: new FormControl('', Validators.required),
  });

  constructor(private workerAuthService: AuthorizeService, private router: Router) { }

  ngOnInit() {
  }

  onWorkerFormSubmit() {
    if (this.workerAuthForm.invalid) {
      return; // Validation failed, exit from method.
    }
    // Form is valid, now perform create
    this.preProcessConfigurations();
    const login = this.workerAuthForm.get('login').value.trim();
    const password = this.workerAuthForm.get('password').value.trim();
    this.workerAuthService.getWorkerByLoginAndPassword(login, password)
      .subscribe(data => {this.worker = data;
    this.router.navigate(['/lkworker/' + login + '/' + password]); },
        errorCode => this.statusCode = errorCode);
  }
  // Perform preliminary processing configurations
  preProcessConfigurations() {
    this.statusCode = null;
  }
  // Go back from update to create
  backToCreateWorker() {
    this.workerAuthForm.reset();
  }
}
