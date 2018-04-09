import {AfterViewInit, Component, OnInit, Inject} from '@angular/core';
import {OrderService} from '../../../services/order.service';
import {MAT_DIALOG_DATA, MatDialogRef} from '@angular/material';


@Component({
  selector: 'app-client-payment',
  templateUrl: './client-payment.component.html',
  styleUrls: ['./client-payment.component.css']
})
export class ClientPaymentComponent implements OnInit, AfterViewInit {
  statusCode: number;

  constructor(private orderService: OrderService,
              public dialogRef: MatDialogRef<ClientPaymentComponent>,
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
    this.orderService.updateOrder(this.data.order)
      .subscribe(successCode => {
          this.statusCode = successCode;
          this.dialogRef.close();
        },
        error => this.statusCode);
  }

  rejectOrder() {
    this.statusCode = null;
    this.data.order.status = 5;
    this.orderService.updateOrder(this.data.order)
      .subscribe(successCode => {
          this.statusCode = successCode;
          this.dialogRef.close();
        },
          error => this.statusCode);
  }
}
