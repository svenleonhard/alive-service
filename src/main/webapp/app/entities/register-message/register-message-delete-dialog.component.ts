import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IRegisterMessage } from 'app/shared/model/register-message.model';
import { RegisterMessageService } from './register-message.service';

@Component({
  templateUrl: './register-message-delete-dialog.component.html',
})
export class RegisterMessageDeleteDialogComponent {
  registerMessage?: IRegisterMessage;

  constructor(
    protected registerMessageService: RegisterMessageService,
    public activeModal: NgbActiveModal,
    protected eventManager: JhiEventManager
  ) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.registerMessageService.delete(id).subscribe(() => {
      this.eventManager.broadcast('registerMessageListModification');
      this.activeModal.close();
    });
  }
}
