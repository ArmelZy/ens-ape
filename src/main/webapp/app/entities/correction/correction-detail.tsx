import React, { useEffect } from 'react';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, UncontrolledTooltip, Row, Col } from 'reactstrap';
import { Translate, byteSize } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './correction.reducer';

export const CorrectionDetail = (props: RouteComponentProps<{ id: string }>) => {
  const dispatch = useAppDispatch();

  useEffect(() => {
    dispatch(getEntity(props.match.params.id));
  }, []);

  const correctionEntity = useAppSelector(state => state.correction.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="correctionDetailsHeading">
          <Translate contentKey="apeApp.correction.detail.title">Correction</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{correctionEntity.id}</dd>
          <dt>
            <span id="content">
              <Translate contentKey="apeApp.correction.content">Content</Translate>
            </span>
            <UncontrolledTooltip target="content">
              <Translate contentKey="apeApp.correction.help.content" />
            </UncontrolledTooltip>
          </dt>
          <dd>{correctionEntity.content}</dd>
          <dt>
            <Translate contentKey="apeApp.correction.exercise">Exercise</Translate>
          </dt>
          <dd>{correctionEntity.exercise ? correctionEntity.exercise.title : ''}</dd>
        </dl>
        <Button tag={Link} to="/correction" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/correction/${correctionEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default CorrectionDetail;
