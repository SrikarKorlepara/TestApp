import { ICategory } from 'app/shared/model/category.model';

export interface IProduct {
  id?: number;
  name?: string;
  description?: string | null;
  price?: number;
  quantity?: number;
  category?: ICategory | null;
}

export const defaultValue: Readonly<IProduct> = {};
