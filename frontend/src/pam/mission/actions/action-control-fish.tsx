import React from 'react'
import { THEME } from '@mtes-mct/monitor-ui'
import { FishAction } from '../../fish-mission-types'
import Title from '../../../ui/title'
import { Stack } from 'rsuite'
import ControlAdministrativeForm from '../controls/env-control-form'
import ControlNavigationForm from '../controls/control-navigation-form'
import ControlGensDeMerForm from '../controls/control-gens-de-mer-form'
import ControlSecurityForm from '../controls/control-security-form'

interface ActionControlPropsFish {
  action: FishAction
}

const ActionControlFish: React.FC<ActionControlPropsFish> = ({ action }) => {
  return (
    <div>
      <h1>Controles Fish</h1>

      <Stack>
        <Stack.Item style={{ width: '100%' }}>
          <Stack direction="column" spacing="0.5rem" style={{ width: '100%' }}>
            <Stack.Item style={{ width: '100%' }}>
              <Title as="h3">Autre(s) contrôle(s) effectué(s) par l’unité sur le navire</Title>
            </Stack.Item>
            {/* <Stack.Item style={{ width: '100%' }}>
              <ControlAdministrativeForm data={control.controlAdministrative} />
            </Stack.Item>
            <Stack.Item style={{ width: '100%' }}>
              <ControlNavigationForm data={control.controlNavigation} />
            </Stack.Item>
            <Stack.Item style={{ width: '100%' }}>
              <ControlGensDeMerForm data={control.controlGensDeMer} />
            </Stack.Item>
            <Stack.Item style={{ width: '100%' }}>
              <ControlSecurityForm data={control.controlSecurity} />
            </Stack.Item> */}
          </Stack>
        </Stack.Item>
      </Stack>

      <div></div>
      <div></div>
    </div>
  )
}

export default ActionControlFish
