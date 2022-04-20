import React, { useEffect } from 'react';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, UncontrolledTooltip, Row, Col } from 'reactstrap';
import { Translate, byteSize } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './exercise.reducer';

export const ExerciseDetail = (props: RouteComponentProps<{ id: string }>) => {
  const dispatch = useAppDispatch();

  useEffect(() => {
    dispatch(getEntity(props.match.params.id));
  }, []);

  const exerciseEntity = useAppSelector(state => state.exercise.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="exerciseDetailsHeading">
          <Translate contentKey="apeApp.exercise.detail.title">Exercise</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{exerciseEntity.id}</dd>
          <dt>
            <span id="title">
              <Translate contentKey="apeApp.exercise.title">Title</Translate>
            </span>
            <UncontrolledTooltip target="title">
              <Translate contentKey="apeApp.exercise.help.title" />
            </UncontrolledTooltip>
          </dt>
          <dd>{exerciseEntity.title}</dd>
          <dt>
            <span id="content">
              <Translate contentKey="apeApp.exercise.content">Content</Translate>
            </span>
            <UncontrolledTooltip target="content">
              <Translate contentKey="apeApp.exercise.help.content" />
            </UncontrolledTooltip>
          </dt>
          <dd>{exerciseEntity.content}</dd>
          <dt>
            <span id="mark">
              <Translate contentKey="apeApp.exercise.mark">Mark</Translate>
            </span>
            <UncontrolledTooltip target="mark">
              <Translate contentKey="apeApp.exercise.help.mark" />
            </UncontrolledTooltip>
          </dt>
          <dd>{exerciseEntity.mark}</dd>
          <dt>
            <Translate contentKey="apeApp.exercise.course">Course</Translate>
          </dt>
          <dd>{exerciseEntity.course ? exerciseEntity.course.title : ''}</dd>
        </dl>
        <Button tag={Link} to="/exercise" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/exercise/${exerciseEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default ExerciseDetail;
