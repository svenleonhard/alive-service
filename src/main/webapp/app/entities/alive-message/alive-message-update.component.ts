import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import * as moment from 'moment';
import { DATE_TIME_FORMAT } from 'app/shared/constants/input.constants';

import { IAliveMessage, AliveMessage } from 'app/shared/model/alive-message.model';
import { AliveMessageService } from './alive-message.service';
import { IUser } from 'app/core/user/user.model';
import { UserService } from 'app/core/user/user.service';

@Component({
  selector: 'jhi-alive-message-update',
  templateUrl: './alive-message-update.component.html',
})
export class AliveMessageUpdateComponent implements OnInit {
  isSaving = false;
  users: IUser[] = [];

  editForm = this.fb.group({
    id: [],
    sendtime: [],
    receivetime: [],
    retrycount: [],
    user: [],
  });

  constructor(
    protected aliveMessageService: AliveMessageService,
    protected userService: UserService,
    protected activatedRoute: ActivatedRoute,
    private fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ aliveMessage }) => {
      if (!aliveMessage.id) {
        const today = moment().startOf('day');
        aliveMessage.sendtime = today;
        aliveMessage.receivetime = today;
      }

      this.updateForm(aliveMessage);

      this.userService.query().subscribe((res: HttpResponse<IUser[]>) => (this.users = res.body || []));
    });
  }

  updateForm(aliveMessage: IAliveMessage): void {
    this.editForm.patchValue({
      id: aliveMessage.id,
      sendtime: aliveMessage.sendtime ? aliveMessage.sendtime.format(DATE_TIME_FORMAT) : null,
      receivetime: aliveMessage.receivetime ? aliveMessage.receivetime.format(DATE_TIME_FORMAT) : null,
      retrycount: aliveMessage.retrycount,
      user: aliveMessage.user,
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const aliveMessage = this.createFromForm();
    if (aliveMessage.id !== undefined) {
      this.subscribeToSaveResponse(this.aliveMessageService.update(aliveMessage));
    } else {
      this.subscribeToSaveResponse(this.aliveMessageService.create(aliveMessage));
    }
  }

  private createFromForm(): IAliveMessage {
    return {
      ...new AliveMessage(),
      id: this.editForm.get(['id'])!.value,
      sendtime: this.editForm.get(['sendtime'])!.value ? moment(this.editForm.get(['sendtime'])!.value, DATE_TIME_FORMAT) : undefined,
      receivetime: this.editForm.get(['receivetime'])!.value
        ? moment(this.editForm.get(['receivetime'])!.value, DATE_TIME_FORMAT)
        : undefined,
      retrycount: this.editForm.get(['retrycount'])!.value,
      user: this.editForm.get(['user'])!.value,
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IAliveMessage>>): void {
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
