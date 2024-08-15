import React from 'react'
import { CoordinatesFormat, CoordinatesInput, DatePicker, Icon, Label, Option, THEME } from '@mtes-mct/monitor-ui'
import { ControlCheck, FishAction, formatMissionActionTypeForHumans } from '../../../types/fish-mission-types'
import Text from '../../../ui/text'
import { Divider, Stack } from 'rsuite'
import ControlsToCompleteTag from '../controls/controls-to-complete-tag'
import { ControlType } from '../../../types/control-types'
import ControlAdministrativeForm from '../controls/control-administrative-form'
import ControlNavigationForm from '../controls/control-navigation-form'
import ControlGensDeMerForm from '../controls/control-gens-de-mer-form'
import ControlSecurityForm from '../controls/control-security-form'
import FishControlAdministrativeSection from './fish/fish-control-administrative-section'
import FishControlEnginesSection from './fish/fish-control-engines-section'
import FishControlSpeciesSection from './fish/fish-control-species-section'
import FishControlSeizureSection from './fish/fish-control-seizure-section'
import FishControlOtherObservationsSection from './fish/fish-control-other-observation-section'
import FishControlFleetSegmentSection from './fish/fish-control-fleet-segment-section'
import useActionById from './use-action-by-id.tsx'
import { useParams } from 'react-router-dom'
import FishControlOtherInfractionsSection from './fish/fish-control-other-infractions-section.tsx'
import { vesselNameOrUnknown } from './utils.ts'
import { ActionDetailsProps } from './action-mapping.ts'
import ActionHeader from './action-header.tsx'
import useIsMissionFinished from '../use-is-mission-finished.tsx'
import ActionEnvObservationsUnit from './action-env-observations-unit.tsx'
import ActionFishObservationsUnit from './action-fish-observations-unit.tsx'

export const controlCheckMultiRadioOptions = Object.keys(ControlCheck).map(key => {
  let label
  switch (key) {
    case ControlCheck.YES:
      label = 'Oui'
      break
    case ControlCheck.NO:
      label = 'Non'
      break
    default:
      label = 'Non contrôlé'
  }

  return {
    label,
    value: ControlCheck[key as keyof typeof ControlCheck]
  }
})

export const BOOLEAN_AS_OPTIONS: Array<Option<boolean>> = [
  { label: 'Oui', value: true },
  { label: 'Non', value: false }
]

type ActionControlPropsFish = ActionDetailsProps

const ActionControlFish: React.FC<ActionControlPropsFish> = ({ action }) => {
  const { missionId, actionId } = useParams()
  const isMissionFinished = useIsMissionFinished(missionId)

  const { data: fishAction, loading, error } = useActionById(actionId, missionId, action.source, action.type)

  if (loading) {
    return <div>Chargement...</div>
  }
  if (error) {
    return <div>error</div>
  }
  if (fishAction) {
    const actionData: FishAction = fishAction.data
    return (
      <Stack
        direction="column"
        spacing={'2rem'}
        alignItems="flex-start"
        style={{ width: '100%' }}
        data-testid={'action-control-fish'}
      >
        <Stack.Item style={{ width: '100%' }}>
          <ActionHeader
            icon={Icon.ControlUnit}
            title={formatMissionActionTypeForHumans(actionData?.actionType)}
            date={fishAction.startDateTimeUtc}
            showButtons={false}
            showStatus={true}
            isMissionFinished={isMissionFinished}
            completenessForStats={fishAction.completenessForStats}
          />
        </Stack.Item>
        <Stack.Item style={{ width: '100%' }}>
          <Stack direction="column" spacing={'0rem'} alignItems="flex-start" style={{ width: '100%' }}>
            <Stack.Item>
              <Text as="h3" weight="bold" color={THEME.color.gunMetal}>
                {vesselNameOrUnknown(actionData?.vesselName)}
              </Text>
            </Stack.Item>
            <Stack.Item></Stack.Item>
          </Stack>
        </Stack.Item>
        <Stack.Item style={{ width: '100%' }}>
          <Label>Date et heure</Label>
          <DatePicker
            defaultValue={fishAction.startDateTimeUtc}
            // label="Date et heure"
            withTime={true}
            isCompact={false}
            isLight={true}
            name="startDateTimeUtc"
            readOnly={true}
            disabled={true}
          />
        </Stack.Item>
        <Stack.Item>
          <Label>Lieu du contrôle</Label>
          <CoordinatesInput
            defaultValue={[actionData?.latitude as any, actionData?.longitude as any]}
            coordinatesFormat={CoordinatesFormat.DEGREES_MINUTES_DECIMALS}
            // label="Lieu du contrôle"
            // isLight={true}
            disabled={true}
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
          <FishControlOtherInfractionsSection action={actionData} />
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
          <Stack direction="column" alignItems="flex-start">
            <Stack.Item>
              <Label>Saisi par</Label>
            </Stack.Item>
            <Stack.Item>{actionData.userTrigram ?? '--'}</Stack.Item>
          </Stack>
        </Stack.Item>
        <Stack.Item style={{ width: '100%' }}>
          <Divider style={{ backgroundColor: THEME.color.charcoal }} />
        </Stack.Item>
        <Stack.Item style={{ width: '100%' }}>
          <Stack direction="column" spacing="0.5rem" style={{ width: '100%' }}>
            <Stack.Item style={{ width: '100%' }}>
              {(actionData?.controlsToComplete?.length ?? 0) > 0 && (
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
              />
            </Stack.Item>
            <Stack.Item style={{ width: '100%' }}>
              <ControlNavigationForm
                data={actionData?.controlNavigation}
                shouldCompleteControl={!!actionData?.controlsToComplete?.includes(ControlType.NAVIGATION)}
                unitShouldConfirm={true}
              />
            </Stack.Item>
            <Stack.Item style={{ width: '100%' }}>
              <ControlGensDeMerForm
                data={actionData?.controlGensDeMer}
                shouldCompleteControl={!!actionData?.controlsToComplete?.includes(ControlType.GENS_DE_MER)}
                unitShouldConfirm={true}
              />
            </Stack.Item>
            <Stack.Item style={{ width: '100%' }}>
              <ControlSecurityForm
                data={actionData?.controlSecurity}
                shouldCompleteControl={!!actionData?.controlsToComplete?.includes(ControlType.SECURITY)}
                unitShouldConfirm={true}
              />
            </Stack.Item>
          </Stack>
        </Stack.Item>
        <Stack.Item style={{ width: '100%' }}>
          <ActionFishObservationsUnit
            missionId={missionId}
            actionId={actionId}
            observationsByUnit={actionData?.observationsByUnit}
            label={'Observations de l’unité sur le contrôle de l’environnement marin'}
          />
        </Stack.Item>
      </Stack>
    )
  }
  return null
}

export default ActionControlFish
