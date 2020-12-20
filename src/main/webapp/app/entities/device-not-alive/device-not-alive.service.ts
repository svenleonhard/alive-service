import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import * as moment from 'moment';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared/util/request-util';
import { IDeviceNotAlive } from 'app/shared/model/device-not-alive.model';

type EntityResponseType = HttpResponse<IDeviceNotAlive>;
type EntityArrayResponseType = HttpResponse<IDeviceNotAlive[]>;

@Injectable({ providedIn: 'root' })
export class DeviceNotAliveService {
  public resourceUrl = SERVER_API_URL + 'api/device-not-alives';

  constructor(protected http: HttpClient) {}

  create(deviceNotAlive: IDeviceNotAlive): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(deviceNotAlive);
    return this.http
      .post<IDeviceNotAlive>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(deviceNotAlive: IDeviceNotAlive): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(deviceNotAlive);
    return this.http
      .put<IDeviceNotAlive>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<IDeviceNotAlive>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IDeviceNotAlive[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  protected convertDateFromClient(deviceNotAlive: IDeviceNotAlive): IDeviceNotAlive {
    const copy: IDeviceNotAlive = Object.assign({}, deviceNotAlive, {
      occured: deviceNotAlive.occured && deviceNotAlive.occured.isValid() ? deviceNotAlive.occured.toJSON() : undefined,
    });
    return copy;
  }

  protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
    if (res.body) {
      res.body.occured = res.body.occured ? moment(res.body.occured) : undefined;
    }
    return res;
  }

  protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
    if (res.body) {
      res.body.forEach((deviceNotAlive: IDeviceNotAlive) => {
        deviceNotAlive.occured = deviceNotAlive.occured ? moment(deviceNotAlive.occured) : undefined;
      });
    }
    return res;
  }
}
