import './home.scss';

import React from 'react';
import { Link } from 'react-router-dom';
import {translate, Translate, ValidatedField} from 'react-jhipster';
import {Row, Col, Alert, Button, Form} from 'reactstrap';

import {useAppDispatch, useAppSelector} from 'app/config/store';
import {useForm} from "react-hook-form";
import {login} from "app/shared/reducers/authentication";

export const Home = () => {
  const dispatch = useAppDispatch();
  const account = useAppSelector(state => state.authentication.account);
  const loginError = useAppSelector(state => state.authentication.loginError);

  const {
    handleSubmit,
    register,
    formState: { errors, touchedFields },
  } = useForm({ mode: 'onTouched' });

  const handleLogin = ({ username, password, rememberMe }) => dispatch(login(username, password, rememberMe));

  const handleLoginSubmit = e => {
    handleSubmit(handleLogin)(e);
  };

  return (
    <div className="container">
      <div className="row justify-content-center">
        {account?.login ? (
          <div className="">
            <h2>
              <Translate contentKey="home.title">Welcome</Translate> {account?.login? <span>{account.login}</span>:null} !
            </h2>
            <Alert color="success">
              <Translate contentKey="home.logged.message" interpolate={{ username: account.login }}>
                You are logged in as user {account.login}.
              </Translate>
            </Alert>
          </div>
        ) : (
          <div className="col-auto">
            <Form id="home-login"  onSubmit={handleLoginSubmit}>
              <div className="card bg-secondary mb-3">
                <div id="login-title" data-cy="loginTitle" className="card-header text-center">
                  <h3><Translate contentKey="login.title">Sign in</Translate></h3>
                </div>
                <div className="card-body">
                  <Row>
                    <Col md="12">
                      {loginError ? (
                        <Alert color="danger" data-cy="loginError">
                          <Translate contentKey="login.messages.error.authentication">
                            <strong>Failed to sign in!</strong> Please check your credentials and try again.
                          </Translate>
                        </Alert>
                      ) : null}
                    </Col>
                    <Col md="12">
                      <ValidatedField
                        name="username"
                        label={translate('global.form.username.label')}
                        placeholder={translate('global.form.username.placeholder')}
                        required
                        autoFocus
                        data-cy="username"
                        validate={{ required: 'Username cannot be empty!' }}
                        register={register}
                        error={errors.username}
                        isTouched={touchedFields.username}
                      />
                      <ValidatedField
                        name="password"
                        type="password"
                        label={translate('login.form.password')}
                        placeholder={translate('login.form.password.placeholder')}
                        required
                        data-cy="password"
                        validate={{ required: 'Password cannot be empty!' }}
                        register={register}
                        error={errors.password}
                        isTouched={touchedFields.password}
                      />
                      <ValidatedField
                        name="rememberMe"
                        type="checkbox"
                        check
                        label={translate('login.form.rememberme')}
                        value={true}
                        register={register}
                      />
                    </Col>
                  </Row>
                  <div className="mt-1">&nbsp;</div>
                  <Alert color="warning">
                    <Link to="/account/reset/request" data-cy="forgetYourPasswordSelector">
                      <Translate contentKey="login.password.forgot">Did you forget your password?</Translate>
                    </Link>
                  </Alert>
                </div>
                <div className="card-footer text-muted">
                  <Button color="primary" type="submit" data-cy="submit" block={true}>
                    <Translate contentKey="login.form.button">Sign in</Translate>
                  </Button>
                </div>
              </div>
            </Form>
          </div>
        )}
      </div>
    </div>
  );
};

export default Home;
