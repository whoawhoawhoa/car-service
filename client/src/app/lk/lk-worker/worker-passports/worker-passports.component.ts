import {Component, Input, OnInit} from '@angular/core';
import {FormControl, FormGroup, Validators} from '@angular/forms';
import {Passport} from '../../../table-classes/passport';
import {WorkerService} from '../../../services/worker.service';
import {PassportService} from '../../../services/passport.service';
import {Worker} from '../../../table-classes/worker';

@Component({
  selector: 'app-worker-passports',
  templateUrl: './worker-passports.component.html',
  styleUrls: ['./worker-passports.component.css']
})
export class WorkerPassportsComponent implements OnInit {
  passport: Passport;
  statusCode: number;
  requestProcessing = false;
  processValidation = false;
  sourceWorker: Worker;
  comment: string;
  @Input() login: string;
  @Input() password: string;

  passportForm = new FormGroup({
    number: new FormControl('', Validators.required),
    issuedBy: new FormControl('', Validators.required)
  });

  constructor(private workerService: WorkerService, private passportService: PassportService) { }

  ngOnInit() {
    this.getWorker();
  }

  getWorker() {
    this.workerService.getWorkerByLoginAndPassword(this.login, this.password)
      .subscribe(
        data => {
          this.sourceWorker = data;
          if (this.sourceWorker.status === 4) {
            this.comment = 'Ваш паспорт не прошел проверку!';
          } else {
            this.sourceWorker.status = 3;
          }
          this.loadPassport(); },
            errorCode => this.statusCode);
  }

  onPassportFormSubmit() {
    this.processValidation = true;
    if (this.passportForm.invalid) {
      return; // Validation failed, exit from method.
    }
    this.preProcessConfigurations();
    const number = this.passportForm.get('number').value;
    const issuedBy = this.passportForm.get('issuedBy').value;
    if (this.passport != null) {
      this.passport.worker.status = 3;
      this.sourceWorker.status = 3;
      this.passport.number = number;
      this.passport.issuedBy = issuedBy;
      this.passport.worker = this.sourceWorker;
      this.workerService.updateWorker(this.sourceWorker)
        .subscribe(successCode => this.statusCode = successCode,
          errorCode => this.statusCode = errorCode);
      this.passportService.updatePassport(this.passport)
        .subscribe(successCode => {
            this.statusCode = successCode;
            this.loadPassport();
            this.backToCreatePassport();
          },
          errorCode => {
          this.statusCode = errorCode;
          this.sourceWorker.status = null;
          this.passport.worker.status = null;
        });
    } else {
      this.passport = new Passport(null, number, issuedBy, this.sourceWorker);
      this.passportService.createPassport(this.passport).subscribe(successCode => {
          this.statusCode = successCode;
          this.loadPassport();
          this.backToCreatePassport();
        },
        errorCode => {
        this.statusCode = errorCode;
        this.passport = null;
      });
    }
  }

  loadPassport() {
    if (this.passport == null) {
      this.passportService.getPassportByWorkerLogin(this.login)
        .subscribe(
          data => this.passport = data,
          errorCode => this.statusCode);
    }
    if (this.passport != null && this.sourceWorker.status !== 4) {
      this.comment = '';
    } else if (this.sourceWorker.status === 3) {
      this.comment = 'Проверяем ваши паспортные данные... Это может занять некоторое время!';
    } else {
      this.comment = 'Ваш паспорт не прошел проверку!';
    }
  }

  preProcessConfigurations() {
    this.statusCode = null;
    this.requestProcessing = true;
  }

  backToCreatePassport() {
    this.processValidation = false;
    this.passportForm.reset();
  }
}
