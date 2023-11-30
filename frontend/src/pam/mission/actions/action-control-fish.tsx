import React from 'react'
import {
  Accent,
  Button,
  CoordinatesFormat,
  CoordinatesInput,
  DatePicker,
  Icon,
  Label,
  MultiRadio,
  Size,
  THEME
} from '@mtes-mct/monitor-ui'
import { ControlCheck, FishAction, formatMissionActionTypeForHumans } from '../../../types/fish-mission-types'
import Text from '../../../ui/text'
import { Divider, Stack } from 'rsuite'
import ControlsToCompleteTag from '../controls/controls-to-complete-tag'
import { Action } from '../../../types/action-types'
import { ControlType } from '../../../types/control-types'
import ControlAdministrativeForm from '../controls/control-administrative-form'
import ControlNavigationForm from '../controls/control-navigation-form'
import ControlGensDeMerForm from '../controls/control-gens-de-mer-form'
import ControlSecurityForm from '../controls/control-security-form'
import { formatDateTimeForFrenchHumans } from '../../../dates'
import FishControlAdministrativeSection from './fish/fish-control-administrative-section'
import FishControlEnginesSection from './fish/fish-control-engines-section'
import FishControlSpeciesSection from './fish/fish-control-species-section'
import FishControlSeizureSection from './fish/fish-control-seizure-section'
import FishControlOtherObservationsSection from './fish/fish-control-other-observation-section'
import FishControlQualitySection from './fish/fish-control-quality-section'
import FishControlFleetSegmentSection from './fish/fish-control-fleet-segment-section'

export const controlCheckMultiradioOptions = Object.keys(ControlCheck).map(key => ({
  label: key.replace(/_/g, ' '),
  value: ControlCheck[key as keyof typeof ControlCheck]
}))

export const BOOLEAN_AS_OPTIONS: Array<Option<boolean>> = [
  { label: 'Oui', value: true },
  { label: 'Non', value: false }
]

interface ActionControlPropsFish {
  action: Action
}

const ActionControlFish: React.FC<ActionControlPropsFish> = ({ action }) => {
  const actionData = action.data as unknown as FishAction
  return (
    <Stack direction="column" spacing={'2rem'} alignItems="flex-start" style={{ width: '100%' }}>
      <Stack.Item style={{ width: '100%' }}>
        <Stack direction="row" spacing={'0.5rem'} style={{ width: '100%' }}>
          <Stack.Item alignSelf="baseline">
            <Icon.ControlUnit color={THEME.color.charcoal} size={20} />
          </Stack.Item>
          <Stack.Item grow={2}>
            <Text as="h2" weight="bold">
              {formatMissionActionTypeForHumans(actionData?.actionType)}{' '}
              {action.startDateTimeUtc && `(${formatDateTimeForFrenchHumans(action.startDateTimeUtc)})`}
            </Text>
          </Stack.Item>
        </Stack>
      </Stack.Item>
      <Stack.Item style={{ width: '100%' }}>
        <Stack direction="column" spacing={'0rem'} alignItems="flex-start" style={{ width: '100%' }}>
          <Stack.Item>
            <Text as="h3" weight="bold" color={THEME.color.gunMetal}>
              {actionData?.vesselName}
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
        <FishControlAdministrativeSection action={actionData} />
      </Stack.Item>
      <Stack.Item style={{ width: '100%' }}>
        <FishControlEnginesSection action={actionData} />
      </Stack.Item>
      <Stack.Item style={{ width: '100%' }}>
        <FishControlSpeciesSection action={actionData} />
      </Stack.Item>
      <Stack.Item style={{ width: '100%' }}>
        <FishControlSeizureSection action={actionData} />
      </Stack.Item>

      <Stack.Item style={{ width: '100%' }}>
        <Stack direction="column" alignItems="flex-start">
          <Stack.Item>
            <Label>Autres infractions</Label>
          </Stack.Item>
        </Stack>
      </Stack.Item>
      <Stack.Item style={{ width: '100%' }}>
        <FishControlOtherObservationsSection action={actionData} />
      </Stack.Item>
      <Stack.Item style={{ width: '100%' }}>
        <Divider style={{ backgroundColor: THEME.color.charcoal }} />
      </Stack.Item>
      <Stack.Item style={{ width: '100%' }}>
        <FishControlFleetSegmentSection action={actionData} />
      </Stack.Item>
      <Stack.Item style={{ width: '100%' }}>
        <FishControlQualitySection action={actionData} />
      </Stack.Item>

      <Stack.Item style={{ width: '100%' }}>
        <Stack direction="column" alignItems="flex-start">
          <Stack.Item>
            <Label>Saisi par</Label>
          </Stack.Item>
          <Stack.Item>{actionData.userTrigram || '--'}</Stack.Item>
        </Stack>
      </Stack.Item>
      <Stack.Item style={{ width: '100%' }}>
        <Divider style={{ backgroundColor: THEME.color.charcoal }} />
      </Stack.Item>
      <Stack.Item style={{ width: '100%' }}>
        <Stack direction="column" spacing="0.5rem" style={{ width: '100%' }}>
          <Stack.Item style={{ width: '100%' }}>
            {(actionData?.controlsToComplete?.length || 0) > 0 && (
              <Stack.Item alignSelf="flex-end">
                <ControlsToCompleteTag
                  amountOfControlsToComplete={actionData?.controlsToComplete?.length}
                  isLight={true}
                />
              </Stack.Item>
            )}
          </Stack.Item>
          <Stack.Item style={{ width: '100%' }}>
            <Label>Autre(s) contrôle(s) effectué(s) par l’unité sur le navire</Label>
          </Stack.Item>
          <Stack.Item style={{ width: '100%' }}>
            <ControlAdministrativeForm
              data={actionData?.controlAdministrative}
              shouldCompleteControl={!!actionData?.controlsToComplete?.includes(ControlType.ADMINISTRATIVE)}
              unitShouldConfirm={true}
              disableToggle={true}
            />
          </Stack.Item>
          <Stack.Item style={{ width: '100%' }}>
            <ControlNavigationForm
              data={actionData?.controlNavigation}
              shouldCompleteControl={!!actionData?.controlsToComplete?.includes(ControlType.NAVIGATION)}
              unitShouldConfirm={true}
              disableToggle={true}
            />
          </Stack.Item>
          <Stack.Item style={{ width: '100%' }}>
            <ControlGensDeMerForm
              data={actionData?.controlGensDeMer}
              shouldCompleteControl={!!actionData?.controlsToComplete?.includes(ControlType.GENS_DE_MER)}
              unitShouldConfirm={true}
              disableToggle={true}
            />
          </Stack.Item>
          <Stack.Item style={{ width: '100%' }}>
            <ControlSecurityForm
              data={actionData?.controlSecurity}
              shouldCompleteControl={!!actionData?.controlsToComplete?.includes(ControlType.SECURITY)}
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
