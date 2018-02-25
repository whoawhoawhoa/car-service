import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';

import { AppComponent } from './app.component';
import { WorkerRegComponent } from './worker-reg/worker-reg.component';
import { WorkerService } from './worker.service';
import { ClientRegComponent } from './client-reg/client-reg.component';
import { ClientService } from './client.service';
import { ReactiveFormsModule } from '@angular/forms';
import { HttpModule } from '@angular/http';

import {AppRoutingModule} from './app-routing/app-routing.module';
import { AuthorizeService } from './authorize.service';
import { ClientAuthComponent } from './client-auth/client-auth.component';
import { WorkerAuthComponent } from './worker-auth/worker-auth.component';

@NgModule({
  declarations: [
    AppComponent,
    WorkerRegComponent,
    ClientRegComponent,
    ClientAuthComponent,
    WorkerAuthComponent
  ],
  imports: [
    BrowserModule,
    HttpModule,
    ReactiveFormsModule,
    AppRoutingModule
  ],
  providers: [
    WorkerService,
    ClientService,
    AuthorizeService
  ],
  bootstrap: [
    AppComponent
  ]
})
export class AppModule { }
