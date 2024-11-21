import dayjs from 'dayjs';
import { ICustomer } from 'app/shared/model/customer.model';

export interface IOrder {
  id?: number;
  orderDate?: dayjs.Dayjs;
  status?: string;
  totalAmount?: number;
  customer?: ICustomer | null;
}

export const defaultValue: Readonly<IOrder> = {};
