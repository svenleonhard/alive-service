import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Routes, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { flatMap } from 'rxjs/operators';

import { Authority } from 'app/shared/constants/authority.constants';
import { UserRouteAccessService } from 'app/core/auth/user-route-access-service';
import { IObserve, Observe } from 'app/shared/model/observe.model';
import { ObserveService } from './observe.service';
import { ObserveComponent } from './observe.component';
import { ObserveDetailComponent } from './observe-detail.component';
import { ObserveUpdateComponent } from './observe-update.component';

@Injectable({ providedIn: 'root' })
export class ObserveResolve implements Resolve<IObserve> {
  constructor(private service: ObserveService, private router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IObserve> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        flatMap((observe: HttpResponse<Observe>) => {
          if (observe.body) {
            return of(observe.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new Observe());
  }
}

export const observeRoute: Routes = [
  {
    path: '',
    component: ObserveComponent,
    data: {
      authorities: [Authority.USER],
      pageTitle: 'aliveServiceApp.observe.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: ObserveDetailComponent,
    resolve: {
      observe: ObserveResolve,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'aliveServiceApp.observe.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: ObserveUpdateComponent,
    resolve: {
      observe: ObserveResolve,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'aliveServiceApp.observe.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: ObserveUpdateComponent,
    resolve: {
      observe: ObserveResolve,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'aliveServiceApp.observe.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
];
