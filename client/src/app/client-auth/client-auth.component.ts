import { Component, OnInit } from '@angular/core';
import {FormControl, FormGroup, Validators} from '@angular/forms';
import {Client} from '../client';
import {AuthorizeService} from '../authorize.service';

@Component({
  selector: 'app-client-auth',
  templateUrl: './client-auth.component.html',
  styleUrls: ['./client-auth.component.css']
})
export class ClientAuthComponent implements OnInit {
  statusCode: number;
  client: Client;

  clientAuthForm = new FormGroup({
    login: new FormControl('', Validators.required),
    password: new FormControl('', Validators.required),
  });

  constructor(private clientAuthService: AuthorizeService) { }

  ngOnInit() {
  }

  onClientFormSubmit() {
    if (this.clientAuthForm.invalid) {
      return; // Validation failed, exit from method.
    }
    // Form is valid, now perform create
    this.preProcessConfigurations();
    const login = this.clientAuthForm.get('login').value.trim();
    const password = this.clientAuthForm.get('password').value.trim();
    this.clientAuthService.checkAuthC(login, password)
      .subscribe(data => this.client = data,
        errorCode => this.statusCode = errorCode);
  }
  // Perform preliminary processing configurations
  preProcessConfigurations() {
    this.statusCode = null;
  }
  // Go back from update to create
  backToCreateClient() {
    this.clientAuthForm.reset();
  }
}
