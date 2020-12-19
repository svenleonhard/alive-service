import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Routes, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { flatMap } from 'rxjs/operators';

import { Authority } from 'app/shared/constants/authority.constants';
import { UserRouteAccessService } from 'app/core/auth/user-route-access-service';
import { IRegisterMessage, RegisterMessage } from 'app/shared/model/register-message.model';
import { RegisterMessageService } from './register-message.service';
import { RegisterMessageComponent } from './register-message.component';
import { RegisterMessageDetailComponent } from './register-message-detail.component';
import { RegisterMessageUpdateComponent } from './register-message-update.component';

@Injectable({ providedIn: 'root' })
export class RegisterMessageResolve implements Resolve<IRegisterMessage> {
  constructor(private service: RegisterMessageService, private router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IRegisterMessage> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        flatMap((registerMessage: HttpResponse<RegisterMessage>) => {
          if (registerMessage.body) {
            return of(registerMessage.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new RegisterMessage());
  }
}

export const registerMessageRoute: Routes = [
  {
    path: '',
    component: RegisterMessageComponent,
    data: {
      authorities: [Authority.USER],
      pageTitle: 'aliveServiceApp.registerMessage.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: RegisterMessageDetailComponent,
    resolve: {
      registerMessage: RegisterMessageResolve,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'aliveServiceApp.registerMessage.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: RegisterMessageUpdateComponent,
    resolve: {
      registerMessage: RegisterMessageResolve,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'aliveServiceApp.registerMessage.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: RegisterMessageUpdateComponent,
    resolve: {
      registerMessage: RegisterMessageResolve,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'aliveServiceApp.registerMessage.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
];
