import { Component, OnInit } from '@angular/core';
import {FormControl, FormGroup, Validators} from '@angular/forms';
import {AdminService} from '../../../services/admin.service';
import {Admin} from '../../../table-classes/admin';
import {Router} from '@angular/router';
import {User} from '../../../table-classes/user';
import {UserService} from '../../../services/user.service';

@Component({
  selector: 'app-admin-reg',
  templateUrl: './admin-reg.component.html',
  styleUrls: ['./admin-reg.component.css']
})
export class AdminRegComponent implements OnInit {
  statusCode: number;
  processValidation = false;

  adminForm = new FormGroup({
    login: new FormControl('', Validators.required),
    password: new FormControl('', Validators.required),
  });

  constructor(private adminService: AdminService, private userService: UserService) { }

  ngOnInit() {
  }

  onAdminFormSubmit() {
    this.processValidation = true;
    if (this.adminForm.invalid) {
      return; // Validation failed, exit from method.
    }
    // Form is valid, now perform create
    this.preProcessConfigurations();
    const login = this.adminForm.get('login').value.trim();
    const password = this.adminForm.get('password').value.trim();
    // Handle create admin
    const user = new User(null, login, password, 1);
    this.userService.createUser(user);
    const admin = new Admin(null, login, password, user);
    this.adminService.createAdmin(admin)
      .subscribe(successCode => {
          this.statusCode = successCode;
          this.backToCreateAdmin();
        },
        errorCode => this.statusCode = errorCode);
  }
  // Perform preliminary processing configurations
  preProcessConfigurations() {
    this.statusCode = null;
  }
  // Go back from update to create
  backToCreateAdmin() {
    this.adminForm.reset();
    this.processValidation = false;
  }
}
