import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IRegisterMessage } from 'app/shared/model/register-message.model';

@Component({
  selector: 'jhi-register-message-detail',
  templateUrl: './register-message-detail.component.html',
})
export class RegisterMessageDetailComponent implements OnInit {
  registerMessage: IRegisterMessage | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ registerMessage }) => (this.registerMessage = registerMessage));
  }

  previousState(): void {
    window.history.back();
  }
}
