import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import * as moment from 'moment';
import { DATE_TIME_FORMAT } from 'app/shared/constants/input.constants';

import { IRegisterMessage, RegisterMessage } from 'app/shared/model/register-message.model';
import { RegisterMessageService } from './register-message.service';
import { IUser } from 'app/core/user/user.model';
import { UserService } from 'app/core/user/user.service';

@Component({
  selector: 'jhi-register-message-update',
  templateUrl: './register-message-update.component.html',
})
export class RegisterMessageUpdateComponent implements OnInit {
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
    protected registerMessageService: RegisterMessageService,
    protected userService: UserService,
    protected activatedRoute: ActivatedRoute,
    private fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ registerMessage }) => {
      if (!registerMessage.id) {
        const today = moment().startOf('day');
        registerMessage.sendtime = today;
        registerMessage.receivetime = today;
      }

      this.updateForm(registerMessage);

      this.userService.query().subscribe((res: HttpResponse<IUser[]>) => (this.users = res.body || []));
    });
  }

  updateForm(registerMessage: IRegisterMessage): void {
    this.editForm.patchValue({
      id: registerMessage.id,
      sendtime: registerMessage.sendtime ? registerMessage.sendtime.format(DATE_TIME_FORMAT) : null,
      receivetime: registerMessage.receivetime ? registerMessage.receivetime.format(DATE_TIME_FORMAT) : null,
      retrycount: registerMessage.retrycount,
      user: registerMessage.user,
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const registerMessage = this.createFromForm();
    if (registerMessage.id !== undefined) {
      this.subscribeToSaveResponse(this.registerMessageService.update(registerMessage));
    } else {
      this.subscribeToSaveResponse(this.registerMessageService.create(registerMessage));
    }
  }

  private createFromForm(): IRegisterMessage {
    return {
      ...new RegisterMessage(),
      id: this.editForm.get(['id'])!.value,
      sendtime: this.editForm.get(['sendtime'])!.value ? moment(this.editForm.get(['sendtime'])!.value, DATE_TIME_FORMAT) : undefined,
      receivetime: this.editForm.get(['receivetime'])!.value
        ? moment(this.editForm.get(['receivetime'])!.value, DATE_TIME_FORMAT)
        : undefined,
      retrycount: this.editForm.get(['retrycount'])!.value,
      user: this.editForm.get(['user'])!.value,
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IRegisterMessage>>): void {
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
