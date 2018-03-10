import { Component, OnInit } from '@angular/core';
import {FormControl, FormGroup, Validators} from '@angular/forms';
import {ClientService} from '../../services/client.service';
import {Client} from '../../table-classes/client';
import {Router} from '@angular/router';
import {UserService} from '../../services/user.service';
import {User} from '../../table-classes/user';

@Component({
  selector: 'app-client-reg',
  templateUrl: './client-reg.component.html',
  styleUrls: ['./client-reg.component.css']
})
export class ClientRegComponent implements OnInit {
  statusCode: number;
  processValidation = false;

  clientForm = new FormGroup({
    login: new FormControl('', Validators.required),
    password: new FormControl('', Validators.required),
    name: new FormControl('', Validators.required),
    fname: new FormControl('', Validators.required),
    pnumber: new FormControl('', Validators.required),
    city: new FormControl('', Validators.required)
  });

  constructor(private clientService: ClientService, private userService: UserService, private router: Router) { }

  ngOnInit() {
  }

  onClientFormSubmit() {
    this.processValidation = true;
    if (this.clientForm.invalid) {
      return; // Validation failed, exit from method.
    }
    // Form is valid, now perform create
    this.preProcessConfigurations();
    const login = this.clientForm.get('login').value.trim();
    const password = this.clientForm.get('password').value.trim();
    const name = this.clientForm.get('name').value.trim();
    const fname = this.clientForm.get('fname').value.trim();
    const pnumber = this.clientForm.get('pnumber').value.trim();
    const city = this.clientForm.get('city').value.trim();
    // Handle create client
    const user = new User(null, login, password, 2);
    this.userService.createUser(user);
    const client = new Client(null, login, password, name, fname, pnumber, city, null, user);
    this.clientService.createClient(client)
      .subscribe(successCode => {
          this.statusCode = successCode;
          this.router.navigate(['/lkclient/' + login + '/' + password]);
          this.backToCreateClient();
        },
        errorCode => this.statusCode = errorCode);
  }
  // Perform preliminary processing configurations
  preProcessConfigurations() {
    this.statusCode = null;
  }
  // Go back from update to create
  backToCreateClient() {
    this.clientForm.reset();
    this.processValidation = false;
  }
}
