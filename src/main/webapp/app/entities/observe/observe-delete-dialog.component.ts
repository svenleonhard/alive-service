import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IObserve } from 'app/shared/model/observe.model';
import { ObserveService } from './observe.service';

@Component({
  templateUrl: './observe-delete-dialog.component.html',
})
export class ObserveDeleteDialogComponent {
  observe?: IObserve;

  constructor(protected observeService: ObserveService, public activeModal: NgbActiveModal, protected eventManager: JhiEventManager) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.observeService.delete(id).subscribe(() => {
      this.eventManager.broadcast('observeListModification');
      this.activeModal.close();
    });
  }
}
