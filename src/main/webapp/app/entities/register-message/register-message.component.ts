import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { Subscription } from 'rxjs';
import { JhiEventManager, JhiParseLinks } from 'ng-jhipster';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IRegisterMessage } from 'app/shared/model/register-message.model';

import { ITEMS_PER_PAGE } from 'app/shared/constants/pagination.constants';
import { RegisterMessageService } from './register-message.service';
import { RegisterMessageDeleteDialogComponent } from './register-message-delete-dialog.component';

@Component({
  selector: 'jhi-register-message',
  templateUrl: './register-message.component.html',
})
export class RegisterMessageComponent implements OnInit, OnDestroy {
  registerMessages: IRegisterMessage[];
  eventSubscriber?: Subscription;
  itemsPerPage: number;
  links: any;
  page: number;
  predicate: string;
  ascending: boolean;

  constructor(
    protected registerMessageService: RegisterMessageService,
    protected eventManager: JhiEventManager,
    protected modalService: NgbModal,
    protected parseLinks: JhiParseLinks
  ) {
    this.registerMessages = [];
    this.itemsPerPage = ITEMS_PER_PAGE;
    this.page = 0;
    this.links = {
      last: 0,
    };
    this.predicate = 'id';
    this.ascending = true;
  }

  loadAll(): void {
    this.registerMessageService
      .query({
        page: this.page,
        size: this.itemsPerPage,
        sort: this.sort(),
      })
      .subscribe((res: HttpResponse<IRegisterMessage[]>) => this.paginateRegisterMessages(res.body, res.headers));
  }

  reset(): void {
    this.page = 0;
    this.registerMessages = [];
    this.loadAll();
  }

  loadPage(page: number): void {
    this.page = page;
    this.loadAll();
  }

  ngOnInit(): void {
    this.loadAll();
    this.registerChangeInRegisterMessages();
  }

  ngOnDestroy(): void {
    if (this.eventSubscriber) {
      this.eventManager.destroy(this.eventSubscriber);
    }
  }

  trackId(index: number, item: IRegisterMessage): number {
    // eslint-disable-next-line @typescript-eslint/no-unnecessary-type-assertion
    return item.id!;
  }

  registerChangeInRegisterMessages(): void {
    this.eventSubscriber = this.eventManager.subscribe('registerMessageListModification', () => this.reset());
  }

  delete(registerMessage: IRegisterMessage): void {
    const modalRef = this.modalService.open(RegisterMessageDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.registerMessage = registerMessage;
  }

  sort(): string[] {
    const result = [this.predicate + ',' + (this.ascending ? 'asc' : 'desc')];
    if (this.predicate !== 'id') {
      result.push('id');
    }
    return result;
  }

  protected paginateRegisterMessages(data: IRegisterMessage[] | null, headers: HttpHeaders): void {
    const headersLink = headers.get('link');
    this.links = this.parseLinks.parse(headersLink ? headersLink : '');
    if (data) {
      for (let i = 0; i < data.length; i++) {
        this.registerMessages.push(data[i]);
      }
    }
  }
}
