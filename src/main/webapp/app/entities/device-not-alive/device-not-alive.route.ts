import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Routes, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { flatMap } from 'rxjs/operators';

import { Authority } from 'app/shared/constants/authority.constants';
import { UserRouteAccessService } from 'app/core/auth/user-route-access-service';
import { IDeviceNotAlive, DeviceNotAlive } from 'app/shared/model/device-not-alive.model';
import { DeviceNotAliveService } from './device-not-alive.service';
import { DeviceNotAliveComponent } from './device-not-alive.component';
import { DeviceNotAliveDetailComponent } from './device-not-alive-detail.component';
import { DeviceNotAliveUpdateComponent } from './device-not-alive-update.component';

@Injectable({ providedIn: 'root' })
export class DeviceNotAliveResolve implements Resolve<IDeviceNotAlive> {
  constructor(private service: DeviceNotAliveService, private router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IDeviceNotAlive> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        flatMap((deviceNotAlive: HttpResponse<DeviceNotAlive>) => {
          if (deviceNotAlive.body) {
            return of(deviceNotAlive.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new DeviceNotAlive());
  }
}

export const deviceNotAliveRoute: Routes = [
  {
    path: '',
    component: DeviceNotAliveComponent,
    data: {
      authorities: [Authority.USER],
      pageTitle: 'aliveServiceApp.deviceNotAlive.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: DeviceNotAliveDetailComponent,
    resolve: {
      deviceNotAlive: DeviceNotAliveResolve,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'aliveServiceApp.deviceNotAlive.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: DeviceNotAliveUpdateComponent,
    resolve: {
      deviceNotAlive: DeviceNotAliveResolve,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'aliveServiceApp.deviceNotAlive.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: DeviceNotAliveUpdateComponent,
    resolve: {
      deviceNotAlive: DeviceNotAliveResolve,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'aliveServiceApp.deviceNotAlive.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
];
