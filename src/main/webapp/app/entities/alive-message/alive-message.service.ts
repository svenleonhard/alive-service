import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import * as moment from 'moment';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared/util/request-util';
import { IAliveMessage } from 'app/shared/model/alive-message.model';

type EntityResponseType = HttpResponse<IAliveMessage>;
type EntityArrayResponseType = HttpResponse<IAliveMessage[]>;

@Injectable({ providedIn: 'root' })
export class AliveMessageService {
  public resourceUrl = SERVER_API_URL + 'api/alive-messages';

  constructor(protected http: HttpClient) {}

  create(aliveMessage: IAliveMessage): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(aliveMessage);
    return this.http
      .post<IAliveMessage>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(aliveMessage: IAliveMessage): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(aliveMessage);
    return this.http
      .put<IAliveMessage>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<IAliveMessage>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IAliveMessage[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  protected convertDateFromClient(aliveMessage: IAliveMessage): IAliveMessage {
    const copy: IAliveMessage = Object.assign({}, aliveMessage, {
      sendtime: aliveMessage.sendtime && aliveMessage.sendtime.isValid() ? aliveMessage.sendtime.toJSON() : undefined,
      receivetime: aliveMessage.receivetime && aliveMessage.receivetime.isValid() ? aliveMessage.receivetime.toJSON() : undefined,
    });
    return copy;
  }

  protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
    if (res.body) {
      res.body.sendtime = res.body.sendtime ? moment(res.body.sendtime) : undefined;
      res.body.receivetime = res.body.receivetime ? moment(res.body.receivetime) : undefined;
    }
    return res;
  }

  protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
    if (res.body) {
      res.body.forEach((aliveMessage: IAliveMessage) => {
        aliveMessage.sendtime = aliveMessage.sendtime ? moment(aliveMessage.sendtime) : undefined;
        aliveMessage.receivetime = aliveMessage.receivetime ? moment(aliveMessage.receivetime) : undefined;
      });
    }
    return res;
  }
}
