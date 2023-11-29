import React from 'react'
import { CoordinatesFormat, CoordinatesInput, DatePicker, Icon, THEME } from '@mtes-mct/monitor-ui'
import { FishAction, formatMissionActionTypeForHumans } from '../../../types/fish-mission-types'
import Text from '../../../ui/text'
import { Stack } from 'rsuite'
import ControlsToCompleteTag from '../controls/controls-to-complete-tag'
import { Action } from '../../../types/action-types'
import { ControlType } from '../../../types/control-types'
import ControlAdministrativeForm from '../controls/control-administrative-form'
import ControlNavigationForm from '../controls/control-navigation-form'
import ControlGensDeMerForm from '../controls/control-gens-de-mer-form'
import ControlSecurityForm from '../controls/control-security-form'
import { formatDateTimeForFrenchHumans } from '../../../dates'

interface ActionControlPropsFish {
  action: Action
}

const ActionControlFish: React.FC<ActionControlPropsFish> = ({ action }) => {
  return (
    <Stack direction="column" spacing={'2rem'} alignItems="flex-start" style={{ width: '100%' }}>
      <Stack.Item style={{ width: '100%' }}>
        <Stack direction="row" spacing={'0.5rem'} style={{ width: '100%' }}>
          <Stack.Item alignSelf="baseline">
            <Icon.ControlUnit color={THEME.color.charcoal} size={20} />
          </Stack.Item>
          <Stack.Item grow={2}>
            <Text as="h2" weight="bold">
              {formatMissionActionTypeForHumans((action.data as any as FishAction)?.actionType)}{' '}
              {action.startDateTimeUtc && `(${formatDateTimeForFrenchHumans(action.startDateTimeUtc)})`}
            </Text>
          </Stack.Item>
        </Stack>
      </Stack.Item>
      <Stack.Item style={{ width: '100%' }}>
        <Stack direction="column" spacing={'0rem'} alignItems="flex-start" style={{ width: '100%' }}>
          <Stack.Item>
            <Text as="h3" weight="bold" color={THEME.color.gunMetal}>
              {(action.data as any as FishAction)?.vesselName}
            </Text>
          </Stack.Item>
          <Stack.Item></Stack.Item>
        </Stack>
      </Stack.Item>
      <Stack.Item style={{ width: '100%' }}>
        <DatePicker
          defaultValue={action.startDateTimeUtc}
          label="Date et heure"
          withTime={true}
          isCompact={false}
          isLight={true}
          name="startDateTimeUtc"
        />
      </Stack.Item>
      <Stack.Item>
        <CoordinatesInput
          defaultValue={[
            (action.data as unknown as FishAction).latitude as any,
            (action.data as unknown as FishAction).longitude as any
          ]}
          coordinatesFormat={CoordinatesFormat.DECIMAL_DEGREES}
          label="Lieu du contrôle"
          isLight={true}
          // disabled={true}
        />
      </Stack.Item>
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
  )
}

export default ActionControlFish
