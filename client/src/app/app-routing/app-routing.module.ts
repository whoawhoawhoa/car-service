import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import {ClientRegComponent} from '../registration/client-reg/client-reg.component';
import {WorkerRegComponent} from '../registration/worker-reg/worker-reg.component';
import {LkClientComponent} from '../lk/lk-client/lk-client.component';
import {LkWorkerComponent} from '../lk/lk-worker/lk-worker.component';
import {UserAuthComponent} from '../authorization/user-auth/user-auth.component';
import {LkAdminComponent} from '../lk/lk-admin/lk-admin.component';

const routes: Routes = [
  { path: 'lkadmin/:login/:password', component: LkAdminComponent},
  { path: 'user-auth', component: UserAuthComponent },
  { path: 'client-reg', component: ClientRegComponent },
  { path: 'worker-reg', component: WorkerRegComponent },
  { path: 'lkworker/:login/:password', component: LkWorkerComponent},
  { path: 'lkclient/:login/:password', component: LkClientComponent},
  { path: 'client', component: ClientRegComponent },
  { path: 'worker', component: WorkerRegComponent },
  { path: '', redirectTo: 'user-auth', pathMatch: 'full'}

];
@NgModule({
  imports: [ RouterModule.forRoot(routes) ],
  exports: [ RouterModule ]
})

export class AppRoutingModule { }
