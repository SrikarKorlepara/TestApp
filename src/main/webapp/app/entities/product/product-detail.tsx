import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './product.reducer';

export const ProductDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const productEntity = useAppSelector(state => state.product.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="productDetailsHeading">
          <Translate contentKey="ecommerceApp.product.detail.title">Product</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{productEntity.id}</dd>
          <dt>
            <span id="name">
              <Translate contentKey="ecommerceApp.product.name">Name</Translate>
            </span>
          </dt>
          <dd>{productEntity.name}</dd>
          <dt>
            <span id="description">
              <Translate contentKey="ecommerceApp.product.description">Description</Translate>
            </span>
          </dt>
          <dd>{productEntity.description}</dd>
          <dt>
            <span id="price">
              <Translate contentKey="ecommerceApp.product.price">Price</Translate>
            </span>
          </dt>
          <dd>{productEntity.price}</dd>
          <dt>
            <span id="quantity">
              <Translate contentKey="ecommerceApp.product.quantity">Quantity</Translate>
            </span>
          </dt>
          <dd>{productEntity.quantity}</dd>
          <dt>
            <Translate contentKey="ecommerceApp.product.category">Category</Translate>
          </dt>
          <dd>{productEntity.category ? productEntity.category.id : ''}</dd>
        </dl>
        <Button tag={Link} to="/product" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/product/${productEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default ProductDetail;
