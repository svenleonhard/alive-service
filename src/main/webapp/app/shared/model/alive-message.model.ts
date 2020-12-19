import { Moment } from 'moment';
import { IUser } from 'app/core/user/user.model';

export interface IAliveMessage {
  id?: number;
  sendtime?: Moment;
  receivetime?: Moment;
  retrycount?: number;
  user?: IUser;
}

export class AliveMessage implements IAliveMessage {
  constructor(public id?: number, public sendtime?: Moment, public receivetime?: Moment, public retrycount?: number, public user?: IUser) {}
}
