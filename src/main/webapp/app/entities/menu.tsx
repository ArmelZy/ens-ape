import React from 'react';
import { Translate } from 'react-jhipster';

import MenuItem from 'app/shared/layout/menus/menu-item';

const EntitiesMenu = () => {
  return (
    <>
      {/* prettier-ignore */}
      <MenuItem icon="asterisk" to="/course">
        <Translate contentKey="global.menu.entities.course" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/exercise">
        <Translate contentKey="global.menu.entities.exercise" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/correction">
        <Translate contentKey="global.menu.entities.correction" />
      </MenuItem>
      {/* jhipster-needle-add-entity-to-menu - JHipster will add entities to the menu here */}
    </>
  );
};

export default EntitiesMenu as React.ComponentType<any>;
