import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { AliveServiceSharedModule } from 'app/shared/shared.module';
import { DeviceNotAliveComponent } from './device-not-alive.component';
import { DeviceNotAliveDetailComponent } from './device-not-alive-detail.component';
import { DeviceNotAliveUpdateComponent } from './device-not-alive-update.component';
import { DeviceNotAliveDeleteDialogComponent } from './device-not-alive-delete-dialog.component';
import { deviceNotAliveRoute } from './device-not-alive.route';

@NgModule({
  imports: [AliveServiceSharedModule, RouterModule.forChild(deviceNotAliveRoute)],
  declarations: [
    DeviceNotAliveComponent,
    DeviceNotAliveDetailComponent,
    DeviceNotAliveUpdateComponent,
    DeviceNotAliveDeleteDialogComponent,
  ],
  entryComponents: [DeviceNotAliveDeleteDialogComponent],
})
export class AliveServiceDeviceNotAliveModule {}
