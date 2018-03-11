import { Component, OnInit } from '@angular/core';
import {FormControl, FormGroup, Validators} from '@angular/forms';
import {Router} from '@angular/router';
import {UserService} from '../../services/user.service';
import {User} from '../../table-classes/user';

@Component({
  selector: 'app-user-auth',
  templateUrl: './user-auth.component.html',
  styleUrls: ['./user-auth.component.css']
})
export class UserAuthComponent implements OnInit {
  statusCode: number;
  user: User;

  userAuthForm = new FormGroup({
    login: new FormControl('', Validators.required),
    password: new FormControl('', Validators.required),
  });

  constructor(private userService: UserService, private router: Router) { }

  ngOnInit() {
  }

  onUserFormSubmit() {
    if (this.userAuthForm.invalid) {
      return; // Validation failed, exit from method.
    }
    // Form is valid, now perform create
    this.preProcessConfigurations();
    const login = this.userAuthForm.get('login').value.trim();
    const password = this.userAuthForm.get('password').value.trim();
    this.userService.getUser(login, password).
      subscribe(data => {
        this.user = data;
        if (this.user.role === 1) {
          this.router.navigate(['/lkadmin/' + login + '/' + password]);
        }
        if (this.user.role === 2) {
          this.router.navigate(['/lkclient/' + login + '/' + password]);
        }
        if (this.user.role === 3) {
          this.router.navigate(['/lkworker/' + login + '/' + password]);
      }
      },
      errorCode => this.statusCode = errorCode
      );
  }
  // Perform preliminary processing configurations
  preProcessConfigurations() {
    this.statusCode = null;
  }
  // Go back from update to create
  backToCreateUser() {
    this.userAuthForm.reset();
  }
}
