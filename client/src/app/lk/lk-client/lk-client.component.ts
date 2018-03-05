import {Component, OnInit} from '@angular/core';
import {FormControl, FormGroup, Validators} from '@angular/forms';
import {ActivatedRoute, Router} from '@angular/router';
import {ClientService} from '../../services/client.service';
import {Client} from '../../table-classes/client';

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
    pnumber: new FormControl('', Validators.required),
    city: new FormControl('', Validators.required)
  });

  constructor(private clientService: ClientService, private route: ActivatedRoute, private router: Router) { }

  ngOnInit() {
    this.getClient(this.route.snapshot.paramMap.get('login'), this.route.snapshot.paramMap.get('password'));
    this.loadClientToEdit();
  }

  getClient(login: string, password: string) {
    this.clientService.getClientByLoginAndPassword(login, password)
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
    const pnumber = this.clientForm.get('pnumber').value;
    const city = this.clientForm.get('city').value;
    // Handle update client
    const client = new Client(this.articleIdToUpdate, login, password, name, fName, pnumber, city, 0);
    this.clientService.updateClient(client)
      .subscribe(successCode => {
        this.statusCode = successCode;
        console.log(this.statusCode);
        this.getClient(login, password);
        this.clientSource = client;
        this.router.navigate(['lkclient/' + login + '/' + password]);
        this.loadClientToEdit();
        this.backToCreateClient();
      }, errorCode =>
        this.statusCode = errorCode);
  }

  loadClientToEdit() {
    this.preProcessConfigurations();
    if (this.clientSource == null) {
      this.clientService.getClientByLoginAndPassword(this.route.snapshot.paramMap.get('login'),
        this.route.snapshot.paramMap.get('password'))
        .subscribe(client => {
            this.articleIdToUpdate = client.id;
            this.clientForm.setValue({
              login: client.login,
              password: client.password,
              name: client.name,
              fName: client.fname,
              pnumber: client.pnumber,
              city: client.city});
            this.processValidation = true;
            this.requestProcessing = false;
          },
          errorCode =>  this.statusCode = errorCode);
    } else {
      this.clientService.getClientByLoginAndPassword(this.clientSource.login, this.clientSource.password)
        .subscribe(client => {
            this.articleIdToUpdate = client.id;
            this.clientForm.setValue({
              login: client.login,
              password: client.password,
              name: client.name,
              fName: client.fname,
              pnumber: client.pnumber,
              city: client.city});
            this.processValidation = true;
            this.requestProcessing = false;
          },
          errorCode =>  this.statusCode = errorCode);
    }
  }

  deleteClient(clientLogin: string) {
    this.preProcessConfigurations();
    this.clientService.deleteClientByLogin(clientLogin)
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
