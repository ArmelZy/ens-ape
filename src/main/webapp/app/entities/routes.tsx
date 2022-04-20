import React from 'react';
import { Switch } from 'react-router-dom';
import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import Course from './course';
import Exercise from './exercise';
import Correction from './correction';
/* jhipster-needle-add-route-import - JHipster will add routes here */

export default ({ match }) => {
  return (
    <div>
      <Switch>
        {/* prettier-ignore */}
        <ErrorBoundaryRoute path={`${match.url}course`} component={Course} />
        <ErrorBoundaryRoute path={`${match.url}exercise`} component={Exercise} />
        <ErrorBoundaryRoute path={`${match.url}correction`} component={Correction} />
        {/* jhipster-needle-add-route-path - JHipster will add routes here */}
      </Switch>
    </div>
  );
};
