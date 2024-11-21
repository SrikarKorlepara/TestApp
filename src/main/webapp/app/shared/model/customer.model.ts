import { IUser } from 'app/shared/model/user.model';

export interface ICustomer {
  id?: number;
  firstName?: string;
  lastName?: string;
  email?: string;
  phoneNumber?: string | null;
  user?: IUser | null;
}

export const defaultValue: Readonly<ICustomer> = {};
