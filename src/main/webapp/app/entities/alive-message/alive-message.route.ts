import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Routes, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { flatMap } from 'rxjs/operators';

import { Authority } from 'app/shared/constants/authority.constants';
import { UserRouteAccessService } from 'app/core/auth/user-route-access-service';
import { IAliveMessage, AliveMessage } from 'app/shared/model/alive-message.model';
import { AliveMessageService } from './alive-message.service';
import { AliveMessageComponent } from './alive-message.component';
import { AliveMessageDetailComponent } from './alive-message-detail.component';
import { AliveMessageUpdateComponent } from './alive-message-update.component';

@Injectable({ providedIn: 'root' })
export class AliveMessageResolve implements Resolve<IAliveMessage> {
  constructor(private service: AliveMessageService, private router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IAliveMessage> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        flatMap((aliveMessage: HttpResponse<AliveMessage>) => {
          if (aliveMessage.body) {
            return of(aliveMessage.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new AliveMessage());
  }
}

export const aliveMessageRoute: Routes = [
  {
    path: '',
    component: AliveMessageComponent,
    data: {
      authorities: [Authority.USER],
      pageTitle: 'aliveServiceApp.aliveMessage.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: AliveMessageDetailComponent,
    resolve: {
      aliveMessage: AliveMessageResolve,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'aliveServiceApp.aliveMessage.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: AliveMessageUpdateComponent,
    resolve: {
      aliveMessage: AliveMessageResolve,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'aliveServiceApp.aliveMessage.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: AliveMessageUpdateComponent,
    resolve: {
      aliveMessage: AliveMessageResolve,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'aliveServiceApp.aliveMessage.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
];
