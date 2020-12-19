import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import * as moment from 'moment';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared/util/request-util';
import { IRegisterMessage } from 'app/shared/model/register-message.model';

type EntityResponseType = HttpResponse<IRegisterMessage>;
type EntityArrayResponseType = HttpResponse<IRegisterMessage[]>;

@Injectable({ providedIn: 'root' })
export class RegisterMessageService {
  public resourceUrl = SERVER_API_URL + 'api/register-messages';

  constructor(protected http: HttpClient) {}

  create(registerMessage: IRegisterMessage): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(registerMessage);
    return this.http
      .post<IRegisterMessage>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(registerMessage: IRegisterMessage): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(registerMessage);
    return this.http
      .put<IRegisterMessage>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<IRegisterMessage>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IRegisterMessage[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  protected convertDateFromClient(registerMessage: IRegisterMessage): IRegisterMessage {
    const copy: IRegisterMessage = Object.assign({}, registerMessage, {
      sendtime: registerMessage.sendtime && registerMessage.sendtime.isValid() ? registerMessage.sendtime.toJSON() : undefined,
      receivetime: registerMessage.receivetime && registerMessage.receivetime.isValid() ? registerMessage.receivetime.toJSON() : undefined,
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
      res.body.forEach((registerMessage: IRegisterMessage) => {
        registerMessage.sendtime = registerMessage.sendtime ? moment(registerMessage.sendtime) : undefined;
        registerMessage.receivetime = registerMessage.receivetime ? moment(registerMessage.receivetime) : undefined;
      });
    }
    return res;
  }
}
