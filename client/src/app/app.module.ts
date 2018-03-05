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
import { ClientAuthComponent } from './authorization/client-auth/client-auth.component';
import { WorkerAuthComponent } from './authorization/worker-auth/worker-auth.component';
import { LkWorkerComponent } from './lk/lk-worker/lk-worker.component';
import { LkClientComponent } from './lk/lk-client/lk-client.component';
import { AdminRegComponent } from './registration/admin-reg/admin-reg.component';
import { AdminAuthComponent } from './authorization/admin-auth/admin-auth.component';
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

@NgModule({
  declarations: [
    AppComponent,
    WorkerRegComponent,
    ClientRegComponent,
    ClientAuthComponent,
    WorkerAuthComponent,
    LkWorkerComponent,
    LkClientComponent,
    LkAdminComponent,
    AdminRegComponent,
    AdminAuthComponent,
    WorkerServicesComponent,
    WorkerOrdersComponent
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
