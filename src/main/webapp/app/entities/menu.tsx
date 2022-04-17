import React from 'react';

import MenuItem from 'app/shared/layout/menus/menu-item';

const EntitiesMenu = () => {
  return (
    <>
      {/* prettier-ignore */}
      <MenuItem icon="asterisk" to="/stats">
        Stats
      </MenuItem>
      <MenuItem icon="asterisk" to="/trends">
        Trends
      </MenuItem>
      <MenuItem icon="asterisk" to="/ranking">
        Ranking
      </MenuItem>
      <MenuItem icon="asterisk" to="/map">
        Map
      </MenuItem>
      {/* jhipster-needle-add-entity-to-menu - JHipster will add entities to the menu here */}
    </>
  );
};

export default EntitiesMenu as React.ComponentType<any>;
