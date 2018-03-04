import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import {ClientRegComponent} from '../registration/client-reg/client-reg.component';
import {WorkerRegComponent} from '../registration/worker-reg/worker-reg.component';
import {ClientAuthComponent} from '../authorization/client-auth/client-auth.component';
import {WorkerAuthComponent} from '../authorization/worker-auth/worker-auth.component';
import {LkClientComponent} from '../lk/lk-client/lk-client.component';
import {LkWorkerComponent} from '../lk/lk-worker/lk-worker.component';
import {AdminAuthComponent} from '../authorization/admin-auth/admin-auth.component';
import {AdminRegComponent} from '../registration/admin-reg/admin-reg.component';
import {LkAdminComponent} from "../lk-admin/lk-admin.component";

const routes: Routes = [
  { path: 'lkadmin/:login/:password', component: LkAdminComponent},
  { path: 'client-auth', component: ClientAuthComponent },
  { path: 'worker-auth', component: WorkerAuthComponent },
  { path: 'admin-auth', component: AdminAuthComponent },
  { path: 'client-reg', component: ClientRegComponent },
  { path: 'worker-reg', component: WorkerRegComponent },
  { path: 'admin-reg', component: AdminRegComponent },
  { path: 'lkworker/:login/:password', component: LkWorkerComponent},
  { path: 'lkclient/:login/:password', component: LkClientComponent},
  { path: 'client', component: ClientRegComponent },
  { path: 'worker', component: WorkerRegComponent },
  { path: '', redirectTo: 'worker-auth', pathMatch: 'full'}

];

@NgModule({
  imports: [ RouterModule.forRoot(routes) ],
  exports: [ RouterModule ]
})

export class AppRoutingModule { }
