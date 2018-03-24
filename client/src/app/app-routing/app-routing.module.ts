import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import {ClientRegComponent} from '../registration/client-reg/client-reg.component';
import {WorkerRegComponent} from '../registration/worker-reg/worker-reg.component';
import {LkClientComponent} from '../lk/lk-client/lk-client.component';
import {LkWorkerComponent} from '../lk/lk-worker/lk-worker.component';
import {UserAuthComponent} from '../authorization/user-auth/user-auth.component';
import {LkAdminComponent} from '../lk/lk-admin/lk-admin.component';
import {MainPageComponent} from '../main-page/main-page.component';
import {StartPageComponent} from '../start-page/start-page.component';
import {WorkerOrdersComponent} from '../lk/lk-worker/worker-orders/worker-orders.component';
import {ClientOrdersComponent} from '../lk/lk-client/client-orders/client-orders.component';

const routes: Routes = [
  { path: 'lkadmin/:login/:password', component: LkAdminComponent},
  { path: 'start-page', component: StartPageComponent},
  { path: 'user-auth', component: UserAuthComponent },
  { path: 'client-reg', component: ClientRegComponent },
  { path: 'worker-reg', component: WorkerRegComponent },
  { path: 'lkworker/:login/:password', component: LkWorkerComponent},
  { path: 'lkclient/:login/:password', component: LkClientComponent},
  { path: 'client', component: ClientRegComponent },
  { path: 'worker', component: WorkerRegComponent },
  { path: 'main', component: MainPageComponent},
  { path: 'main/:login/:password', component: MainPageComponent},
  { path: '', redirectTo: 'start-page', pathMatch: 'full'},
  { path: 'worker-orders/:login/:password', component: WorkerOrdersComponent},
  { path: 'client-orders/:login/:password', component: ClientOrdersComponent}
];

@NgModule({
  imports: [ RouterModule.forRoot(routes) ],
  exports: [ RouterModule ]
})

export class AppRoutingModule { }
