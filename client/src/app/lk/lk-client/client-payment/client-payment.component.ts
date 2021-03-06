import {AfterViewInit, Component, OnInit, Inject} from '@angular/core';
import {OrderService} from '../../../services/order.service';
import {MAT_DIALOG_DATA, MatDialogRef} from '@angular/material/dialog';
import {WorkerService} from '../../../services/worker.service';
import {OrderEsService} from '../../../services/order-es.service';

@Component({
  selector: 'app-client-payment',
  templateUrl: './client-payment.component.html',
  styleUrls: ['./client-payment.component.css']
})
export class ClientPaymentComponent implements OnInit, AfterViewInit {
  statusCode: number;

  constructor(private orderService: OrderService,
              private orderEsService: OrderEsService,
              public dialogRef: MatDialogRef<ClientPaymentComponent>,
              private workerService: WorkerService,
              @Inject(MAT_DIALOG_DATA) public data: any) {
    dialogRef.disableClose = true;
  }

  ngAfterViewInit() {
  }
  ngOnInit() {
  }

  doPayment() {
    this.statusCode = null;
    this.data.order.status = 1;
    this.data.order.worker.status = 2;
    this.orderService.updateOrder(this.data.order)
      .subscribe(successCode => {
          this.workerService.updateWorker(this.data.order.worker)
            .subscribe();
          this.statusCode = successCode;
          this.dialogRef.close();
        },
        error => this.statusCode);
  }

  rejectOrder() {
    this.statusCode = null;
    this.data.order.status = 5;
    this.orderEsService.createOrderEs(this.data.order)
      .subscribe(successCode => {
          this.orderService.updateOrder(this.data.order)
            .subscribe(success => {
                this.statusCode = successCode;
                this.dialogRef.close();
              },
              error => this.statusCode); });
  }
}
