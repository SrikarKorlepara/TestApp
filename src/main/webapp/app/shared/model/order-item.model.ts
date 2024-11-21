import { IProduct } from 'app/shared/model/product.model';
import { IOrder } from 'app/shared/model/order.model';

export interface IOrderItem {
  id?: number;
  quantity?: number;
  totalPrice?: number;
  product?: IProduct | null;
  order?: IOrder | null;
}

export const defaultValue: Readonly<IOrderItem> = {};
