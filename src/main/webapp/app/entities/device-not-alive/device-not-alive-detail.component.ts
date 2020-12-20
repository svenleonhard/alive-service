import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IDeviceNotAlive } from 'app/shared/model/device-not-alive.model';

@Component({
  selector: 'jhi-device-not-alive-detail',
  templateUrl: './device-not-alive-detail.component.html',
})
export class DeviceNotAliveDetailComponent implements OnInit {
  deviceNotAlive: IDeviceNotAlive | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ deviceNotAlive }) => (this.deviceNotAlive = deviceNotAlive));
  }

  previousState(): void {
    window.history.back();
  }
}
