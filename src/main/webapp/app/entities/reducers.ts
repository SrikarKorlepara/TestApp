import product from 'app/entities/product/product.reducer';
import category from 'app/entities/category/category.reducer';
import customer from 'app/entities/customer/customer.reducer';
import order from 'app/entities/order/order.reducer';
import orderItem from 'app/entities/order-item/order-item.reducer';
/* jhipster-needle-add-reducer-import - JHipster will add reducer here */

const entitiesReducers = {
  product,
  category,
  customer,
  order,
  orderItem,
  /* jhipster-needle-add-reducer-combine - JHipster will add reducer here */
};

export default entitiesReducers;
