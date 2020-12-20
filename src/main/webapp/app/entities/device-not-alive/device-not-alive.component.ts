import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { Subscription } from 'rxjs';
import { JhiEventManager, JhiParseLinks } from 'ng-jhipster';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IDeviceNotAlive } from 'app/shared/model/device-not-alive.model';

import { ITEMS_PER_PAGE } from 'app/shared/constants/pagination.constants';
import { DeviceNotAliveService } from './device-not-alive.service';
import { DeviceNotAliveDeleteDialogComponent } from './device-not-alive-delete-dialog.component';

@Component({
  selector: 'jhi-device-not-alive',
  templateUrl: './device-not-alive.component.html',
})
export class DeviceNotAliveComponent implements OnInit, OnDestroy {
  deviceNotAlives: IDeviceNotAlive[];
  eventSubscriber?: Subscription;
  itemsPerPage: number;
  links: any;
  page: number;
  predicate: string;
  ascending: boolean;

  constructor(
    protected deviceNotAliveService: DeviceNotAliveService,
    protected eventManager: JhiEventManager,
    protected modalService: NgbModal,
    protected parseLinks: JhiParseLinks
  ) {
    this.deviceNotAlives = [];
    this.itemsPerPage = ITEMS_PER_PAGE;
    this.page = 0;
    this.links = {
      last: 0,
    };
    this.predicate = 'id';
    this.ascending = true;
  }

  loadAll(): void {
    this.deviceNotAliveService
      .query({
        page: this.page,
        size: this.itemsPerPage,
        sort: this.sort(),
      })
      .subscribe((res: HttpResponse<IDeviceNotAlive[]>) => this.paginateDeviceNotAlives(res.body, res.headers));
  }

  reset(): void {
    this.page = 0;
    this.deviceNotAlives = [];
    this.loadAll();
  }

  loadPage(page: number): void {
    this.page = page;
    this.loadAll();
  }

  ngOnInit(): void {
    this.loadAll();
    this.registerChangeInDeviceNotAlives();
  }

  ngOnDestroy(): void {
    if (this.eventSubscriber) {
      this.eventManager.destroy(this.eventSubscriber);
    }
  }

  trackId(index: number, item: IDeviceNotAlive): number {
    // eslint-disable-next-line @typescript-eslint/no-unnecessary-type-assertion
    return item.id!;
  }

  registerChangeInDeviceNotAlives(): void {
    this.eventSubscriber = this.eventManager.subscribe('deviceNotAliveListModification', () => this.reset());
  }

  delete(deviceNotAlive: IDeviceNotAlive): void {
    const modalRef = this.modalService.open(DeviceNotAliveDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.deviceNotAlive = deviceNotAlive;
  }

  sort(): string[] {
    const result = [this.predicate + ',' + (this.ascending ? 'asc' : 'desc')];
    if (this.predicate !== 'id') {
      result.push('id');
    }
    return result;
  }

  protected paginateDeviceNotAlives(data: IDeviceNotAlive[] | null, headers: HttpHeaders): void {
    const headersLink = headers.get('link');
    this.links = this.parseLinks.parse(headersLink ? headersLink : '');
    if (data) {
      for (let i = 0; i < data.length; i++) {
        this.deviceNotAlives.push(data[i]);
      }
    }
  }
}
