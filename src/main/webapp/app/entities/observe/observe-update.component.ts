import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';

import { IObserve, Observe } from 'app/shared/model/observe.model';
import { ObserveService } from './observe.service';
import { IUser } from 'app/core/user/user.model';
import { UserService } from 'app/core/user/user.service';

@Component({
  selector: 'jhi-observe-update',
  templateUrl: './observe-update.component.html',
})
export class ObserveUpdateComponent implements OnInit {
  isSaving = false;
  users: IUser[] = [];
  startdateDp: any;

  editForm = this.fb.group({
    id: [],
    description: [],
    startdate: [],
    user: [],
  });

  constructor(
    protected observeService: ObserveService,
    protected userService: UserService,
    protected activatedRoute: ActivatedRoute,
    private fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ observe }) => {
      this.updateForm(observe);

      this.userService.query().subscribe((res: HttpResponse<IUser[]>) => (this.users = res.body || []));
    });
  }

  updateForm(observe: IObserve): void {
    this.editForm.patchValue({
      id: observe.id,
      description: observe.description,
      startdate: observe.startdate,
      user: observe.user,
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const observe = this.createFromForm();
    if (observe.id !== undefined) {
      this.subscribeToSaveResponse(this.observeService.update(observe));
    } else {
      this.subscribeToSaveResponse(this.observeService.create(observe));
    }
  }

  private createFromForm(): IObserve {
    return {
      ...new Observe(),
      id: this.editForm.get(['id'])!.value,
      description: this.editForm.get(['description'])!.value,
      startdate: this.editForm.get(['startdate'])!.value,
      user: this.editForm.get(['user'])!.value,
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IObserve>>): void {
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
