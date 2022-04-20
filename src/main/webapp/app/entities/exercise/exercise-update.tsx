import React, { useState, useEffect } from 'react';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col, FormText, UncontrolledTooltip } from 'reactstrap';
import { isNumber, Translate, translate, ValidatedField, ValidatedForm } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { ICourse } from 'app/shared/model/course.model';
import { getEntities as getCourses } from 'app/entities/course/course.reducer';
import { ICorrection } from 'app/shared/model/correction.model';
import { getEntities as getCorrections } from 'app/entities/correction/correction.reducer';
import { IExercise } from 'app/shared/model/exercise.model';
import { getEntity, updateEntity, createEntity, reset } from './exercise.reducer';

export const ExerciseUpdate = (props: RouteComponentProps<{ id: string }>) => {
  const dispatch = useAppDispatch();

  const [isNew] = useState(!props.match.params || !props.match.params.id);

  const courses = useAppSelector(state => state.course.entities);
  const corrections = useAppSelector(state => state.correction.entities);
  const exerciseEntity = useAppSelector(state => state.exercise.entity);
  const loading = useAppSelector(state => state.exercise.loading);
  const updating = useAppSelector(state => state.exercise.updating);
  const updateSuccess = useAppSelector(state => state.exercise.updateSuccess);
  const handleClose = () => {
    props.history.push('/exercise' + props.location.search);
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(props.match.params.id));
    }

    dispatch(getCourses({}));
    dispatch(getCorrections({}));
  }, []);

  useEffect(() => {
    if (updateSuccess) {
      handleClose();
    }
  }, [updateSuccess]);

  const saveEntity = values => {
    const entity = {
      ...exerciseEntity,
      ...values,
      course: courses.find(it => it.id.toString() === values.course.toString()),
    };

    if (isNew) {
      dispatch(createEntity(entity));
    } else {
      dispatch(updateEntity(entity));
    }
  };

  const defaultValues = () =>
    isNew
      ? {}
      : {
          ...exerciseEntity,
          course: exerciseEntity?.course?.id,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="apeApp.exercise.home.createOrEditLabel" data-cy="ExerciseCreateUpdateHeading">
            <Translate contentKey="apeApp.exercise.home.createOrEditLabel">Create or edit a Exercise</Translate>
          </h2>
        </Col>
      </Row>
      <Row className="justify-content-center">
        <Col md="8">
          {loading ? (
            <p>Loading...</p>
          ) : (
            <ValidatedForm defaultValues={defaultValues()} onSubmit={saveEntity}>
              {!isNew ? (
                <ValidatedField
                  name="id"
                  required
                  readOnly
                  id="exercise-id"
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField
                label={translate('apeApp.exercise.title')}
                id="exercise-title"
                name="title"
                data-cy="title"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                }}
              />
              <UncontrolledTooltip target="titleLabel">
                <Translate contentKey="apeApp.exercise.help.title" />
              </UncontrolledTooltip>
              <ValidatedField
                label={translate('apeApp.exercise.content')}
                id="exercise-content"
                name="content"
                data-cy="content"
                type="textarea"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                }}
              />
              <UncontrolledTooltip target="contentLabel">
                <Translate contentKey="apeApp.exercise.help.content" />
              </UncontrolledTooltip>
              <ValidatedField
                label={translate('apeApp.exercise.mark')}
                id="exercise-mark"
                name="mark"
                data-cy="mark"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                  min: { value: 0, message: translate('entity.validation.min', { min: 0 }) },
                  validate: v => isNumber(v) || translate('entity.validation.number'),
                }}
              />
              <UncontrolledTooltip target="markLabel">
                <Translate contentKey="apeApp.exercise.help.mark" />
              </UncontrolledTooltip>
              <ValidatedField
                id="exercise-course"
                name="course"
                data-cy="course"
                label={translate('apeApp.exercise.course')}
                type="select"
                required
              >
                <option value="" key="0" />
                {courses
                  ? courses.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.title}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <FormText>
                <Translate contentKey="entity.validation.required">This field is required.</Translate>
              </FormText>
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/exercise" replace color="info">
                <FontAwesomeIcon icon="arrow-left" />
                &nbsp;
                <span className="d-none d-md-inline">
                  <Translate contentKey="entity.action.back">Back</Translate>
                </span>
              </Button>
              &nbsp;
              <Button color="primary" id="save-entity" data-cy="entityCreateSaveButton" type="submit" disabled={updating}>
                <FontAwesomeIcon icon="save" />
                &nbsp;
                <Translate contentKey="entity.action.save">Save</Translate>
              </Button>
            </ValidatedForm>
          )}
        </Col>
      </Row>
    </div>
  );
};

export default ExerciseUpdate;
