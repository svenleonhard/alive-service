import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { AliveServiceSharedModule } from 'app/shared/shared.module';
import { AliveMessageComponent } from './alive-message.component';
import { AliveMessageDetailComponent } from './alive-message-detail.component';
import { AliveMessageUpdateComponent } from './alive-message-update.component';
import { AliveMessageDeleteDialogComponent } from './alive-message-delete-dialog.component';
import { aliveMessageRoute } from './alive-message.route';

@NgModule({
  imports: [AliveServiceSharedModule, RouterModule.forChild(aliveMessageRoute)],
  declarations: [AliveMessageComponent, AliveMessageDetailComponent, AliveMessageUpdateComponent, AliveMessageDeleteDialogComponent],
  entryComponents: [AliveMessageDeleteDialogComponent],
})
export class AliveServiceAliveMessageModule {}
