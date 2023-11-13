import React from 'react'
import { THEME } from '@mtes-mct/monitor-ui'
import { FishAction } from '../../fish-mission-types'
import Text from '../../../ui/text'
import { Stack } from 'rsuite'
import ControlsToCompleteTag from '../controls/controls-to-complete-tag'
import { Action, ControlType } from '../../mission-types'
import ControlAdministrativeForm from '../controls/control-administrative-form'
import ControlNavigationForm from '../controls/control-navigation-form'
import ControlGensDeMerForm from '../controls/control-gens-de-mer-form'
import ControlSecurityForm from '../controls/control-security-form'

interface ActionControlPropsFish {
  action: Action
}

const ActionControlFish: React.FC<ActionControlPropsFish> = ({ action }) => {
  return (
    <div>
      <h1>Controles Fish</h1>

      <Stack>
        <Stack.Item style={{ width: '100%' }}>
          <Stack direction="column" spacing="0.5rem" style={{ width: '100%' }}>
            <Stack.Item style={{ width: '100%' }}>
              {((action.data as any as FishAction)?.controlsToComplete?.length || 0) > 0 && (
                <Stack.Item alignSelf="flex-end">
                  <ControlsToCompleteTag
                    amountOfControlsToComplete={(action.data as any as FishAction)?.controlsToComplete?.length}
                    isLight={true}
                  />
                </Stack.Item>
              )}
            </Stack.Item>
            <Stack.Item style={{ width: '100%' }}>
              <Text as="h3">Autre(s) contrôle(s) effectué(s) par l’unité sur le navire</Text>
            </Stack.Item>
            <Stack.Item style={{ width: '100%' }}>
              <ControlAdministrativeForm
                data={(action.data as any as FishAction)?.controlAdministrative}
                shouldCompleteControl={
                  !!(action.data as any as FishAction)?.controlsToComplete?.includes(ControlType.ADMINISTRATIVE)
                }
                unitShouldConfirm={true}
                disableToggle={true}
              />
            </Stack.Item>
            <Stack.Item style={{ width: '100%' }}>
              <ControlNavigationForm
                data={(action.data as any as FishAction)?.controlNavigation}
                shouldCompleteControl={
                  !!(action.data as any as FishAction)?.controlsToComplete?.includes(ControlType.NAVIGATION)
                }
                unitShouldConfirm={true}
                disableToggle={true}
              />
            </Stack.Item>
            <Stack.Item style={{ width: '100%' }}>
              <ControlGensDeMerForm
                data={(action.data as any as FishAction)?.controlGensDeMer}
                shouldCompleteControl={
                  !!(action.data as any as FishAction)?.controlsToComplete?.includes(ControlType.GENS_DE_MER)
                }
                unitShouldConfirm={true}
                disableToggle={true}
              />
            </Stack.Item>
            <Stack.Item style={{ width: '100%' }}>
              <ControlSecurityForm
                data={(action.data as any as FishAction)?.controlSecurity}
                shouldCompleteControl={
                  !!(action.data as any as FishAction)?.controlsToComplete?.includes(ControlType.SECURITY)
                }
                unitShouldConfirm={true}
                disableToggle={true}
              />
            </Stack.Item>
          </Stack>
        </Stack.Item>
      </Stack>

      <div></div>
      <div></div>
    </div>
  )
}

export default ActionControlFish
