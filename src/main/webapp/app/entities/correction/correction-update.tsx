import React, { useState, useEffect } from 'react';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col, FormText, UncontrolledTooltip } from 'reactstrap';
import { isNumber, Translate, translate, ValidatedField, ValidatedForm } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { IExercise } from 'app/shared/model/exercise.model';
import { getEntities as getExercises } from 'app/entities/exercise/exercise.reducer';
import { ICorrection } from 'app/shared/model/correction.model';
import { getEntity, updateEntity, createEntity, reset } from './correction.reducer';

export const CorrectionUpdate = (props: RouteComponentProps<{ id: string }>) => {
  const dispatch = useAppDispatch();

  const [isNew] = useState(!props.match.params || !props.match.params.id);

  const exercises = useAppSelector(state => state.exercise.entities);
  const correctionEntity = useAppSelector(state => state.correction.entity);
  const loading = useAppSelector(state => state.correction.loading);
  const updating = useAppSelector(state => state.correction.updating);
  const updateSuccess = useAppSelector(state => state.correction.updateSuccess);
  const handleClose = () => {
    props.history.push('/correction' + props.location.search);
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(props.match.params.id));
    }

    dispatch(getExercises({}));
  }, []);

  useEffect(() => {
    if (updateSuccess) {
      handleClose();
    }
  }, [updateSuccess]);

  const saveEntity = values => {
    const entity = {
      ...correctionEntity,
      ...values,
      exercise: exercises.find(it => it.id.toString() === values.exercise.toString()),
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
          ...correctionEntity,
          exercise: correctionEntity?.exercise?.id,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="apeApp.correction.home.createOrEditLabel" data-cy="CorrectionCreateUpdateHeading">
            <Translate contentKey="apeApp.correction.home.createOrEditLabel">Create or edit a Correction</Translate>
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
                  id="correction-id"
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField
                label={translate('apeApp.correction.content')}
                id="correction-content"
                name="content"
                data-cy="content"
                type="textarea"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                }}
              />
              <UncontrolledTooltip target="contentLabel">
                <Translate contentKey="apeApp.correction.help.content" />
              </UncontrolledTooltip>
              <ValidatedField
                id="correction-exercise"
                name="exercise"
                data-cy="exercise"
                label={translate('apeApp.correction.exercise')}
                type="select"
                required
              >
                <option value="" key="0" />
                {exercises
                  ? exercises.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.title}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <FormText>
                <Translate contentKey="entity.validation.required">This field is required.</Translate>
              </FormText>
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/correction" replace color="info">
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

export default CorrectionUpdate;
