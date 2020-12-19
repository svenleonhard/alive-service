import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { AliveServiceSharedModule } from 'app/shared/shared.module';
import { ObserveComponent } from './observe.component';
import { ObserveDetailComponent } from './observe-detail.component';
import { ObserveUpdateComponent } from './observe-update.component';
import { ObserveDeleteDialogComponent } from './observe-delete-dialog.component';
import { observeRoute } from './observe.route';

@NgModule({
  imports: [AliveServiceSharedModule, RouterModule.forChild(observeRoute)],
  declarations: [ObserveComponent, ObserveDetailComponent, ObserveUpdateComponent, ObserveDeleteDialogComponent],
  entryComponents: [ObserveDeleteDialogComponent],
})
export class AliveServiceObserveModule {}
