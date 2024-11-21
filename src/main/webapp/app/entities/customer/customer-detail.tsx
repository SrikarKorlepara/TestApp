import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './customer.reducer';

export const CustomerDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const customerEntity = useAppSelector(state => state.customer.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="customerDetailsHeading">
          <Translate contentKey="ecommerceApp.customer.detail.title">Customer</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{customerEntity.id}</dd>
          <dt>
            <span id="firstName">
              <Translate contentKey="ecommerceApp.customer.firstName">First Name</Translate>
            </span>
          </dt>
          <dd>{customerEntity.firstName}</dd>
          <dt>
            <span id="lastName">
              <Translate contentKey="ecommerceApp.customer.lastName">Last Name</Translate>
            </span>
          </dt>
          <dd>{customerEntity.lastName}</dd>
          <dt>
            <span id="email">
              <Translate contentKey="ecommerceApp.customer.email">Email</Translate>
            </span>
          </dt>
          <dd>{customerEntity.email}</dd>
          <dt>
            <span id="phoneNumber">
              <Translate contentKey="ecommerceApp.customer.phoneNumber">Phone Number</Translate>
            </span>
          </dt>
          <dd>{customerEntity.phoneNumber}</dd>
          <dt>
            <Translate contentKey="ecommerceApp.customer.user">User</Translate>
          </dt>
          <dd>{customerEntity.user ? customerEntity.user.id : ''}</dd>
        </dl>
        <Button tag={Link} to="/customer" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/customer/${customerEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default CustomerDetail;
