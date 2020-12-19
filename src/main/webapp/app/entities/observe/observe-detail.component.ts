import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IObserve } from 'app/shared/model/observe.model';

@Component({
  selector: 'jhi-observe-detail',
  templateUrl: './observe-detail.component.html',
})
export class ObserveDetailComponent implements OnInit {
  observe: IObserve | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ observe }) => (this.observe = observe));
  }

  previousState(): void {
    window.history.back();
  }
}
