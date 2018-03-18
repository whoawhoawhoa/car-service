import {Component, OnInit} from '@angular/core';
import {Passport} from '../../../table-classes/passport';
import {PassportService} from '../../../services/passport.service';
import {WorkerService} from '../../../services/worker.service';

@Component({
  selector: 'app-check-passports',
  templateUrl: './check-passports.component.html',
  styleUrls: ['./check-passports.component.css']
})
export class CheckPassportsComponent implements OnInit {
  passportSet: Passport[];
  statusCode: number;
  requestProcessing = false;
  processValidation = false;

  constructor(private passportService: PassportService, private workerService: WorkerService) { }

  ngOnInit() {
    this.loadPassports();
  }

  loadPassports() {
    this.passportService.getPassportByWorkerStatus('3')
      .subscribe(
        data => this.passportSet = data,
        errorCode => this.statusCode);
  }

  rejectPassport(passport: Passport) {
    passport.worker.status = 4;
    this.workerService.updateWorker(passport.worker).subscribe(successCode => {
      this.statusCode = successCode;
      this.passportService.updatePassport(passport)
        .subscribe(sc => {
          this.statusCode = sc;
          this.loadPassports();
          this.backToCreatePassport();
        }, errorCode =>
          this.statusCode = errorCode);
    }, errorCode =>
      this.statusCode = errorCode);
  }

  acceptPassport(passport: Passport) {
    passport.worker.status = 0;
    this.workerService.updateWorker(passport.worker).subscribe(successCode => {
      this.statusCode = successCode;
      this.passportService.updatePassport(passport)
        .subscribe(sc => {
          this.statusCode = sc;
          this.loadPassports();
          this.backToCreatePassport();
        }, errorCode =>
          this.statusCode = errorCode);
    }, errorCode =>
      this.statusCode = errorCode);
  }

  preProcessConfigurations() {
    this.statusCode = null;
    this.requestProcessing = true;
  }

  backToCreatePassport() {
    this.processValidation = false;
  }
}
