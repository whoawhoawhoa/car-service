import {Component, OnInit} from '@angular/core';
import {LkClientService} from '../lk-client.service';
import {Client} from '../client';
import {FormControl, FormGroup, Validators} from '@angular/forms';

@Component({
  selector: 'app-lk-client',
  templateUrl: './lk-client.component.html',
  styleUrls: ['./lk-client.component.css']
})


export class LkClientComponent implements OnInit {

  clientSource: Client;
  statusCode: number;
  requestProcessing = false;
  articleIdToUpdate = null;
  processValidation = false;

  clientForm = new FormGroup({
    login: new FormControl('', Validators.required),
    password: new FormControl('', Validators.required),
    name: new FormControl('', Validators.required),
    fName: new FormControl('', Validators.required),
    phoneNumber: new FormControl('', Validators.required),
    city: new FormControl('', Validators.required)
  });

  constructor(private clientService: LkClientService) { }

  ngOnInit() {
    this.getClient();
    this.loadClientToEdit();
  }

  getClient() {
    this.clientService.getClient()
      .subscribe(
        data => this.clientSource = data,
        errorCode => this.statusCode);
  }

  getClientById()
  {
    this.clientService.getClientById(1 + '')
      .subscribe(
        data => this.clientSource = data,
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
    const phoneNumber = this.clientForm.get('phoneNumber').value;
    const city = this.clientForm.get('city').value;
    // Handle update article
    const client = new Client(this.articleIdToUpdate, login, password, name, fName, phoneNumber, city, 0);
    this.clientService.updateClient(client)
      .subscribe(successCode => {
        this.statusCode = successCode;
        this.getClient();
        this.loadClientToEdit();
        this.backToCreateClient();
        }, errorCode => this.statusCode = errorCode);
  }

  loadClientToEdit() {
  this.preProcessConfigurations();
  this.clientService.getClient()
    .subscribe(client => {
        this.articleIdToUpdate = client.id;
        this.clientForm.setValue({
          login: client.login,
          password: client.password,
          name: client.name,
          fName: client.fname,
          phoneNumber: client.phone_number,
          city: client.city});
        this.processValidation = true;
        this.requestProcessing = false;
      },
      errorCode =>  this.statusCode = errorCode);
  }

  deleteClient(clientId: string) {
    this.preProcessConfigurations();
    this.clientService.deleteClientById(clientId)
      .subscribe(successCode => {
          this.statusCode = successCode;
        },
        errorCode => this.statusCode = errorCode);
  }

  preProcessConfigurations() {
    this.statusCode = null;
    this.requestProcessing = true;
  }


  backToCreateClient() {
    this.articleIdToUpdate = null;
    this.clientForm.reset();
    this.processValidation = false;
  }

}
