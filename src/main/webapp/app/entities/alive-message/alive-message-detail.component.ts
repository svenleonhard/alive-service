import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IAliveMessage } from 'app/shared/model/alive-message.model';

@Component({
  selector: 'jhi-alive-message-detail',
  templateUrl: './alive-message-detail.component.html',
})
export class AliveMessageDetailComponent implements OnInit {
  aliveMessage: IAliveMessage | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ aliveMessage }) => (this.aliveMessage = aliveMessage));
  }

  previousState(): void {
    window.history.back();
  }
}
