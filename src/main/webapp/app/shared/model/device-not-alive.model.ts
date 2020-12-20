import { Moment } from 'moment';
import { IUser } from 'app/core/user/user.model';

export interface IDeviceNotAlive {
  id?: number;
  occured?: Moment;
  confirmed?: boolean;
  user?: IUser;
}

export class DeviceNotAlive implements IDeviceNotAlive {
  constructor(public id?: number, public occured?: Moment, public confirmed?: boolean, public user?: IUser) {
    this.confirmed = this.confirmed || false;
  }
}
