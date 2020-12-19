import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import * as moment from 'moment';

import { DATE_FORMAT } from 'app/shared/constants/input.constants';
import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared/util/request-util';
import { IObserve } from 'app/shared/model/observe.model';

type EntityResponseType = HttpResponse<IObserve>;
type EntityArrayResponseType = HttpResponse<IObserve[]>;

@Injectable({ providedIn: 'root' })
export class ObserveService {
  public resourceUrl = SERVER_API_URL + 'api/observes';

  constructor(protected http: HttpClient) {}

  create(observe: IObserve): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(observe);
    return this.http
      .post<IObserve>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(observe: IObserve): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(observe);
    return this.http
      .put<IObserve>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<IObserve>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IObserve[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  protected convertDateFromClient(observe: IObserve): IObserve {
    const copy: IObserve = Object.assign({}, observe, {
      startdate: observe.startdate && observe.startdate.isValid() ? observe.startdate.format(DATE_FORMAT) : undefined,
    });
    return copy;
  }

  protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
    if (res.body) {
      res.body.startdate = res.body.startdate ? moment(res.body.startdate) : undefined;
    }
    return res;
  }

  protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
    if (res.body) {
      res.body.forEach((observe: IObserve) => {
        observe.startdate = observe.startdate ? moment(observe.startdate) : undefined;
      });
    }
    return res;
  }
}
