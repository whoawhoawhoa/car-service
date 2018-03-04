import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import {ClientRegComponent} from '../client-reg/client-reg.component';
import {WorkerRegComponent} from '../worker-reg/worker-reg.component';
import {ClientAuthComponent} from '../client-auth/client-auth.component';
import {WorkerAuthComponent} from '../worker-auth/worker-auth.component';
import {LkClientComponent} from "../lk-client/lk-client.component";
import {LkWorkerComponent} from "../lk-worker/lk-worker.component";
import {LkAdminComponent} from "../lk-admin/lk-admin.component";

const routes: Routes = [
  { path: 'lkadmin/:login/:password', component: LkAdminComponent},
  { path: 'client-auth', component: ClientAuthComponent },
  { path: 'worker-auth', component: WorkerAuthComponent },
  { path: 'client', component: ClientRegComponent },
  { path: 'worker', component: WorkerRegComponent },
  { path: 'lkworker/:login/:password', component: LkWorkerComponent},
  { path: 'lkclient/:login/:password', component: LkClientComponent},
  { path: '', redirectTo: 'worker-auth', pathMatch: 'full'}

];

@NgModule({
  imports: [ RouterModule.forRoot(routes) ],
  exports: [ RouterModule ]
})

export class AppRoutingModule { }
