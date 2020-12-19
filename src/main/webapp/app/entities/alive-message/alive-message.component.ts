import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { Subscription } from 'rxjs';
import { JhiEventManager, JhiParseLinks } from 'ng-jhipster';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IAliveMessage } from 'app/shared/model/alive-message.model';

import { ITEMS_PER_PAGE } from 'app/shared/constants/pagination.constants';
import { AliveMessageService } from './alive-message.service';
import { AliveMessageDeleteDialogComponent } from './alive-message-delete-dialog.component';

@Component({
  selector: 'jhi-alive-message',
  templateUrl: './alive-message.component.html',
})
export class AliveMessageComponent implements OnInit, OnDestroy {
  aliveMessages: IAliveMessage[];
  eventSubscriber?: Subscription;
  itemsPerPage: number;
  links: any;
  page: number;
  predicate: string;
  ascending: boolean;

  constructor(
    protected aliveMessageService: AliveMessageService,
    protected eventManager: JhiEventManager,
    protected modalService: NgbModal,
    protected parseLinks: JhiParseLinks
  ) {
    this.aliveMessages = [];
    this.itemsPerPage = ITEMS_PER_PAGE;
    this.page = 0;
    this.links = {
      last: 0,
    };
    this.predicate = 'id';
    this.ascending = true;
  }

  loadAll(): void {
    this.aliveMessageService
      .query({
        page: this.page,
        size: this.itemsPerPage,
        sort: this.sort(),
      })
      .subscribe((res: HttpResponse<IAliveMessage[]>) => this.paginateAliveMessages(res.body, res.headers));
  }

  reset(): void {
    this.page = 0;
    this.aliveMessages = [];
    this.loadAll();
  }

  loadPage(page: number): void {
    this.page = page;
    this.loadAll();
  }

  ngOnInit(): void {
    this.loadAll();
    this.registerChangeInAliveMessages();
  }

  ngOnDestroy(): void {
    if (this.eventSubscriber) {
      this.eventManager.destroy(this.eventSubscriber);
    }
  }

  trackId(index: number, item: IAliveMessage): number {
    // eslint-disable-next-line @typescript-eslint/no-unnecessary-type-assertion
    return item.id!;
  }

  registerChangeInAliveMessages(): void {
    this.eventSubscriber = this.eventManager.subscribe('aliveMessageListModification', () => this.reset());
  }

  delete(aliveMessage: IAliveMessage): void {
    const modalRef = this.modalService.open(AliveMessageDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.aliveMessage = aliveMessage;
  }

  sort(): string[] {
    const result = [this.predicate + ',' + (this.ascending ? 'asc' : 'desc')];
    if (this.predicate !== 'id') {
      result.push('id');
    }
    return result;
  }

  protected paginateAliveMessages(data: IAliveMessage[] | null, headers: HttpHeaders): void {
    const headersLink = headers.get('link');
    this.links = this.parseLinks.parse(headersLink ? headersLink : '');
    if (data) {
      for (let i = 0; i < data.length; i++) {
        this.aliveMessages.push(data[i]);
      }
    }
  }
}
