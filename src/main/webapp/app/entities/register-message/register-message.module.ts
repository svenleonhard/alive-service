import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { AliveServiceSharedModule } from 'app/shared/shared.module';
import { RegisterMessageComponent } from './register-message.component';
import { RegisterMessageDetailComponent } from './register-message-detail.component';
import { RegisterMessageUpdateComponent } from './register-message-update.component';
import { RegisterMessageDeleteDialogComponent } from './register-message-delete-dialog.component';
import { registerMessageRoute } from './register-message.route';

@NgModule({
  imports: [AliveServiceSharedModule, RouterModule.forChild(registerMessageRoute)],
  declarations: [
    RegisterMessageComponent,
    RegisterMessageDetailComponent,
    RegisterMessageUpdateComponent,
    RegisterMessageDeleteDialogComponent,
  ],
  entryComponents: [RegisterMessageDeleteDialogComponent],
})
export class AliveServiceRegisterMessageModule {}
