import { Moment } from 'moment';
import { IUser } from 'app/core/user/user.model';

export interface IRegisterMessage {
  id?: number;
  sendtime?: Moment;
  receivetime?: Moment;
  retrycount?: number;
  user?: IUser;
}

export class RegisterMessage implements IRegisterMessage {
  constructor(public id?: number, public sendtime?: Moment, public receivetime?: Moment, public retrycount?: number, public user?: IUser) {}
}
