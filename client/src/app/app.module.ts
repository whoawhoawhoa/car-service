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
import {MainPageComponent} from './main-page/main-page.component';
import { MainPageServicesComponent } from './main-page/main-page-services/main-page-services.component';
import { WorkerAvailableOrdersComponent } from './lk/lk-worker/worker-available-orders/worker-available-orders.component';
import { WorkerPassportsComponent } from './lk/lk-worker/worker-passports/worker-passports.component';
import { CheckPassportsComponent } from './lk/lk-admin/check-passports/check-passports.component';
import { ClientAvailableOrderComponent } from './lk/lk-client/client-available-order/client-available-order.component';
import { StartPageComponent } from './start-page/start-page.component';
import {AccordionConfig, AccordionModule, CarouselModule} from 'ngx-bootstrap';
import {ClientOrdersComponent} from './lk/lk-client/client-orders/client-orders.component';
import {OrderEsService} from './services/order-es.service';
import {AllOrdersComponent} from './lk/lk-admin/all-orders/all-orders.component';
import {BrowserAnimationsModule} from '@angular/platform-browser/animations';
import {ClientPaymentComponent} from './lk/lk-client/client-payment/client-payment.component';
import {WorkerOrdersFinalComponent} from './lk/lk-worker/worker-orders-final/worker-orders-final.component';
import {ClientOrdersFinalComponent} from './lk/lk-client/client-orders-final/client-orders-final.component';
import {MatDialogModule} from '@angular/material/dialog';


@NgModule({
  declarations: [
    StartPageComponent,
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
    CheckPassportsComponent,
    ClientAvailableOrderComponent,
    ClientOrdersComponent,
    ClientOrdersFinalComponent,
    WorkerOrdersFinalComponent,
    ClientPaymentComponent,
    AllOrdersComponent
  ],
  entryComponents: [
    ClientPaymentComponent
  ],
  imports: [
    BrowserModule,
    HttpModule,
    ReactiveFormsModule,
    AppRoutingModule,
    FormsModule,
    CarouselModule,
    AccordionModule,
    MatDialogModule,
    BrowserAnimationsModule
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
    ServiceService,
    ClientOrdersComponent,
    WorkerOrdersComponent,
    OrderEsService,
    AccordionConfig
  ],
  bootstrap: [
    AppComponent
  ]
})
export class AppModule { }
