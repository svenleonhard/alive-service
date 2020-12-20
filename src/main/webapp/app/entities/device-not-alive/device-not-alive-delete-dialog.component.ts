import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IDeviceNotAlive } from 'app/shared/model/device-not-alive.model';
import { DeviceNotAliveService } from './device-not-alive.service';

@Component({
  templateUrl: './device-not-alive-delete-dialog.component.html',
})
export class DeviceNotAliveDeleteDialogComponent {
  deviceNotAlive?: IDeviceNotAlive;

  constructor(
    protected deviceNotAliveService: DeviceNotAliveService,
    public activeModal: NgbActiveModal,
    protected eventManager: JhiEventManager
  ) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.deviceNotAliveService.delete(id).subscribe(() => {
      this.eventManager.broadcast('deviceNotAliveListModification');
      this.activeModal.close();
    });
  }
}
