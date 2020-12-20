import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import * as moment from 'moment';
import { DATE_TIME_FORMAT } from 'app/shared/constants/input.constants';

import { IDeviceNotAlive, DeviceNotAlive } from 'app/shared/model/device-not-alive.model';
import { DeviceNotAliveService } from './device-not-alive.service';
import { IUser } from 'app/core/user/user.model';
import { UserService } from 'app/core/user/user.service';

@Component({
  selector: 'jhi-device-not-alive-update',
  templateUrl: './device-not-alive-update.component.html',
})
export class DeviceNotAliveUpdateComponent implements OnInit {
  isSaving = false;
  users: IUser[] = [];

  editForm = this.fb.group({
    id: [],
    occured: [],
    confirmed: [],
    user: [],
  });

  constructor(
    protected deviceNotAliveService: DeviceNotAliveService,
    protected userService: UserService,
    protected activatedRoute: ActivatedRoute,
    private fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ deviceNotAlive }) => {
      if (!deviceNotAlive.id) {
        const today = moment().startOf('day');
        deviceNotAlive.occured = today;
      }

      this.updateForm(deviceNotAlive);

      this.userService.query().subscribe((res: HttpResponse<IUser[]>) => (this.users = res.body || []));
    });
  }

  updateForm(deviceNotAlive: IDeviceNotAlive): void {
    this.editForm.patchValue({
      id: deviceNotAlive.id,
      occured: deviceNotAlive.occured ? deviceNotAlive.occured.format(DATE_TIME_FORMAT) : null,
      confirmed: deviceNotAlive.confirmed,
      user: deviceNotAlive.user,
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const deviceNotAlive = this.createFromForm();
    if (deviceNotAlive.id !== undefined) {
      this.subscribeToSaveResponse(this.deviceNotAliveService.update(deviceNotAlive));
    } else {
      this.subscribeToSaveResponse(this.deviceNotAliveService.create(deviceNotAlive));
    }
  }

  private createFromForm(): IDeviceNotAlive {
    return {
      ...new DeviceNotAlive(),
      id: this.editForm.get(['id'])!.value,
      occured: this.editForm.get(['occured'])!.value ? moment(this.editForm.get(['occured'])!.value, DATE_TIME_FORMAT) : undefined,
      confirmed: this.editForm.get(['confirmed'])!.value,
      user: this.editForm.get(['user'])!.value,
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IDeviceNotAlive>>): void {
    result.subscribe(
      () => this.onSaveSuccess(),
      () => this.onSaveError()
    );
  }

  protected onSaveSuccess(): void {
    this.isSaving = false;
    this.previousState();
  }

  protected onSaveError(): void {
    this.isSaving = false;
  }

  trackById(index: number, item: IUser): any {
    return item.id;
  }
}
