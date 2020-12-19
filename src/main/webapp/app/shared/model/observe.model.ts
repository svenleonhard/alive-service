import { Moment } from 'moment';
import { IUser } from 'app/core/user/user.model';

export interface IObserve {
  id?: number;
  description?: string;
  startdate?: Moment;
  user?: IUser;
}

export class Observe implements IObserve {
  constructor(public id?: number, public description?: string, public startdate?: Moment, public user?: IUser) {}
}
