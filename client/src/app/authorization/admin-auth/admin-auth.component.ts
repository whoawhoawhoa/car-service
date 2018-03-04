import { Component, OnInit } from '@angular/core';
import {FormControl, FormGroup, Validators} from '@angular/forms';
import {Admin} from '../../table-classes/admin';
import {AdminService} from '../../services/admin.service';
import {Router} from "@angular/router";

@Component({
  selector: 'app-admin-auth',
  templateUrl: './admin-auth.component.html',
  styleUrls: ['./admin-auth.component.css']
})
export class AdminAuthComponent implements OnInit {
  statusCode: number;
  admin: Admin;

  adminAuthForm = new FormGroup({
    login: new FormControl('', Validators.required),
    password: new FormControl('', Validators.required),
  });

  constructor(private adminAuthService: AdminService, private router: Router) { }

  ngOnInit() {
  }

  onAdminFormSubmit() {
    if (this.adminAuthForm.invalid) {
      return; // Validation failed, exit from method.
    }
    // Form is valid, now perform create
    this.preProcessConfigurations();
    const login = this.adminAuthForm.get('login').value.trim();
    const password = this.adminAuthForm.get('password').value.trim();
    this.adminAuthService.getAdminByLoginAndPassword(login, password)
      .subscribe(data => {
        this.admin = data;
        this.router.navigate(['/lkadmin/' + login + '/' + password]);
      },
          errorCode => this.statusCode = errorCode
      );
  }
  // Perform preliminary processing configurations
  preProcessConfigurations() {
    this.statusCode = null;
  }
  // Go back from update to create
  backToCreateAdmin() {
    this.adminAuthForm.reset();
  }
}
