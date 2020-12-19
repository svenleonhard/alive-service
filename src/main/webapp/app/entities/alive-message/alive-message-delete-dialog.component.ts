import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IAliveMessage } from 'app/shared/model/alive-message.model';
import { AliveMessageService } from './alive-message.service';

@Component({
  templateUrl: './alive-message-delete-dialog.component.html',
})
export class AliveMessageDeleteDialogComponent {
  aliveMessage?: IAliveMessage;

  constructor(
    protected aliveMessageService: AliveMessageService,
    public activeModal: NgbActiveModal,
    protected eventManager: JhiEventManager
  ) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.aliveMessageService.delete(id).subscribe(() => {
      this.eventManager.broadcast('aliveMessageListModification');
      this.activeModal.close();
    });
  }
}
