import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';
import { FormsModule } from '@angular/forms';

import { AppComponent } from './app.component';
import { WorkerRegComponent } from './registration/worker-reg/worker-reg.component';
import { WorkerService } from './services/worker.service';
import { ClientRegComponent } from './registration/client-reg/client-reg.component';
import { ClientService } from './services/client.service';
import { ReactiveFormsModule } from '@angular/forms';
import { HttpModule } from '@angular/http';

import { AppRoutingModule } from './app-routing/app-routing.module';
import { LkWorkerComponent } from './lk/lk-worker/lk-worker.component';
import { LkClientComponent } from './lk/lk-client/lk-client.component';
import { AdminRegComponent } from './lk/lk-admin/admin-reg/admin-reg.component';
import {AdminService} from './services/admin.service';
import {AvailableOrderService} from './services/available-order.service';
import {CarService} from './services/car.service';
import {CarTypeService} from './services/car-type.service';
import {OrderService} from './services/order.service';
import {PassportService} from './services/passport.service';
import {PriceService} from './services/price.service';
import {ServiceService} from './services/service.service';
import { WorkerServicesComponent } from './lk/lk-worker/worker-services/worker-services.component';
import { WorkerOrdersComponent } from './lk/lk-worker/worker-orders/worker-orders.component';
import {LkAdminComponent} from './lk/lk-admin/lk-admin.component';
import {UserAuthComponent} from './authorization/user-auth/user-auth.component';
import {UserService} from './services/user.service';
import {MainPageComponent} from "./main-page/main-page.component";
import { MainPageServicesComponent } from './main-page/main-page-services/main-page-services.component';
import { WorkerAvailableOrdersComponent } from './lk/lk-worker/worker-available-orders/worker-available-orders.component';
import { WorkerPassportsComponent } from './lk/lk-worker/worker-passports/worker-passports.component';
import { CheckPassportsComponent } from './lk/lk-admin/check-passports/check-passports.component';

@NgModule({
  declarations: [
    AppComponent,
    UserAuthComponent,
    WorkerRegComponent,
    ClientRegComponent,
    LkWorkerComponent,
    LkClientComponent,
    LkAdminComponent,
    AdminRegComponent,
    WorkerServicesComponent,
    WorkerOrdersComponent,
    MainPageComponent,
    MainPageServicesComponent,
    WorkerAvailableOrdersComponent,
    WorkerPassportsComponent,
    CheckPassportsComponent
  ],
  imports: [
    BrowserModule,
    HttpModule,
    ReactiveFormsModule,
    AppRoutingModule,
    FormsModule
  ],
  providers: [
    WorkerService,
    UserService,
    ClientService,
    AdminService,
    AvailableOrderService,
    CarService,
    CarTypeService,
    OrderService,
    PassportService,
    PriceService,
    ServiceService
  ],
  bootstrap: [
    AppComponent
  ]
})
export class AppModule { }
