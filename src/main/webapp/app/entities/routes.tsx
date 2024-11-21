import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import Product from './product';
import Category from './category';
import Customer from './customer';
import Order from './order';
import OrderItem from './order-item';
/* jhipster-needle-add-route-import - JHipster will add routes here */

export default () => {
  return (
    <div>
      <ErrorBoundaryRoutes>
        {/* prettier-ignore */}
        <Route path="product/*" element={<Product />} />
        <Route path="category/*" element={<Category />} />
        <Route path="customer/*" element={<Customer />} />
        <Route path="order/*" element={<Order />} />
        <Route path="order-item/*" element={<OrderItem />} />
        {/* jhipster-needle-add-route-path - JHipster will add routes here */}
      </ErrorBoundaryRoutes>
    </div>
  );
};
