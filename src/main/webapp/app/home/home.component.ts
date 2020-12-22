import { Component, OnDestroy, OnInit } from '@angular/core';
import { Subscription } from 'rxjs';

import { LoginModalService } from 'app/core/login/login-modal.service';
import { AccountService } from 'app/core/auth/account.service';
import { Account } from 'app/core/user/account.model';
import { ObserveService } from 'app/entities/observe/observe.service';
import { RegisterMessageService } from 'app/entities/register-message/register-message.service';
import { AliveMessageService } from 'app/entities/alive-message/alive-message.service';
import { DeviceNotAliveService } from 'app/entities/device-not-alive/device-not-alive.service';

@Component({
  selector: 'jhi-home',
  templateUrl: './home.component.html',
  styleUrls: ['home.scss'],
})
export class HomeComponent implements OnInit, OnDestroy {
  account: Account | null = null;
  authSubscription?: Subscription;

  device = '';
  registered = '';
  status = '';
  lastReport = '';
  userId = -1;
  statusColorClass = 'text-warning';
  statusTranslate = '';

  constructor(
    private accountService: AccountService,
    private loginModalService: LoginModalService,
    private observeService: ObserveService,
    private registerService: RegisterMessageService,
    private deviceNotAliveService: DeviceNotAliveService,
    private aliveMessage: AliveMessageService
  ) {}

  ngOnInit(): void {
    this.authSubscription = this.accountService.getAuthenticationState().subscribe(account => (this.account = account));

    this.observeService.query().subscribe(res => {
      if (res.body && res.body.length > 0) {
        const observe = res.body[0];
        if (observe.description) {
          this.device = observe.description;
        } else {
          // this.translateService.get('home.noDevice').subscribe(text => (this.device = text));
        }
      }
    });

    this.registerService.query().subscribe(res => {
      if (res.body && res.body.length > 0) {
        const registerMessage = res.body[0];
        if (registerMessage) {
          this.registered = String(registerMessage.receivetime);
        }
      } else {
        // this.translateService.get('home.noRegister').subscribe(text => (this.registered = text));
      }
    });

    this.deviceNotAliveService.query().subscribe(res => {
      if (res.body && res.body.length === 0) {
        // this.translateService.get('home.status.alive').subscribe(text => (this.status = text));
        this.statusColorClass = 'text-success';
        this.statusTranslate = 'home.status.alive';
      } else if (res.body && res.body.length === 1) {
        // this.translateService.get('home.status.warning').subscribe(text => (this.status = text));
        this.statusTranslate = 'home.status.warning';
        this.statusColorClass = 'text-warning';
      } else {
        // this.translateService.get('home.status.error').subscribe(text => (this.status = text));
        this.statusColorClass = 'text-danger';
        this.statusTranslate = 'home.status.error';
      }
    });

    this.aliveMessage.query().subscribe(res => {
      if (res.body && res.body.length > 0) {
        const lastAliveMessage = res.body[0];
        if (lastAliveMessage) {
          this.lastReport = String(lastAliveMessage.receivetime);
        } else {
          // this.translateService.get('home.noMessage').subscribe(text => (this.status = text));
        }
      } else {
        // this.translateService.get('home.noMessage').subscribe(text => (this.status = text));
      }
    });
  }

  isAuthenticated(): boolean {
    return this.accountService.isAuthenticated();
  }

  login(): void {
    this.loginModalService.open();
  }

  ngOnDestroy(): void {
    if (this.authSubscription) {
      this.authSubscription.unsubscribe();
    }
  }
}
