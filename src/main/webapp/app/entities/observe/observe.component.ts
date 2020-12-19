import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { Subscription } from 'rxjs';
import { JhiEventManager, JhiParseLinks } from 'ng-jhipster';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IObserve } from 'app/shared/model/observe.model';

import { ITEMS_PER_PAGE } from 'app/shared/constants/pagination.constants';
import { ObserveService } from './observe.service';
import { ObserveDeleteDialogComponent } from './observe-delete-dialog.component';

@Component({
  selector: 'jhi-observe',
  templateUrl: './observe.component.html',
})
export class ObserveComponent implements OnInit, OnDestroy {
  observes: IObserve[];
  eventSubscriber?: Subscription;
  itemsPerPage: number;
  links: any;
  page: number;
  predicate: string;
  ascending: boolean;

  constructor(
    protected observeService: ObserveService,
    protected eventManager: JhiEventManager,
    protected modalService: NgbModal,
    protected parseLinks: JhiParseLinks
  ) {
    this.observes = [];
    this.itemsPerPage = ITEMS_PER_PAGE;
    this.page = 0;
    this.links = {
      last: 0,
    };
    this.predicate = 'id';
    this.ascending = true;
  }

  loadAll(): void {
    this.observeService
      .query({
        page: this.page,
        size: this.itemsPerPage,
        sort: this.sort(),
      })
      .subscribe((res: HttpResponse<IObserve[]>) => this.paginateObserves(res.body, res.headers));
  }

  reset(): void {
    this.page = 0;
    this.observes = [];
    this.loadAll();
  }

  loadPage(page: number): void {
    this.page = page;
    this.loadAll();
  }

  ngOnInit(): void {
    this.loadAll();
    this.registerChangeInObserves();
  }

  ngOnDestroy(): void {
    if (this.eventSubscriber) {
      this.eventManager.destroy(this.eventSubscriber);
    }
  }

  trackId(index: number, item: IObserve): number {
    // eslint-disable-next-line @typescript-eslint/no-unnecessary-type-assertion
    return item.id!;
  }

  registerChangeInObserves(): void {
    this.eventSubscriber = this.eventManager.subscribe('observeListModification', () => this.reset());
  }

  delete(observe: IObserve): void {
    const modalRef = this.modalService.open(ObserveDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.observe = observe;
  }

  sort(): string[] {
    const result = [this.predicate + ',' + (this.ascending ? 'asc' : 'desc')];
    if (this.predicate !== 'id') {
      result.push('id');
    }
    return result;
  }

  protected paginateObserves(data: IObserve[] | null, headers: HttpHeaders): void {
    const headersLink = headers.get('link');
    this.links = this.parseLinks.parse(headersLink ? headersLink : '');
    if (data) {
      for (let i = 0; i < data.length; i++) {
        this.observes.push(data[i]);
      }
    }
  }
}
