import React from 'react'
import { CoordinatesFormat, CoordinatesInput, DatePicker, Icon, Label, THEME } from '@mtes-mct/monitor-ui'
import Divider from 'rsuite/Divider'
import { Stack } from 'rsuite'
import Text from '../../../ui/text'
import ControlsToCompleteTag from '../controls/controls-to-complete-tag'
import EnvControlForm from '../controls/env-control-form'
import { ControlType } from '../../../types/control-types'
import { actionTargetTypeLabels, EnvActionControl, vehicleTypeLabels } from '../../../types/env-mission-types'
import { useParams } from 'react-router-dom'
import EnvInfractionAddNewTarget from '../infractions/env-infraction-add-new-target.tsx'
import EnvInfractionExistingTargets from '../infractions/env-infraction-existing-targets.tsx'
import useActionById from './use-action-by-id.tsx'
import { extractLatLonFromMultiPoint } from '../../../utils/geometry.ts'
import { Coordinates } from '@mtes-mct/monitor-ui/types/definitions'
import { ActionDetailsProps } from './action-mapping.ts'
import ActionHeader from './action-header.tsx'
import useIsMissionFinished from '../use-is-mission-finished.tsx'

type ActionControlPropsEnv = ActionDetailsProps

const ActionControlEnv: React.FC<ActionControlPropsEnv> = ({ action }) => {
  const { missionId, actionId } = useParams()
  const isMissionFinished = useIsMissionFinished(missionId)

  const { data: envAction, loading, error } = useActionById(actionId, missionId, action.source, action.type)

  if (loading) {
    return <div>Chargement...</div>
  }
  if (error) {
    return <div>error</div>
  }
  if (envAction) {
    const actionData: EnvActionControl = envAction.data
    return (
      <Stack
        direction="column"
        spacing={'2rem'}
        alignItems="flex-start"
        style={{ width: '100%' }}
        data-testid={'action-control-env'}
      >
        <Stack.Item style={{ width: '100%' }}>
          <ActionHeader
            icon={Icon.ControlUnit}
            title={'Contrôles'}
            date={envAction.startDateTimeUtc}
            showButtons={false}
            showStatus={true}
            isMissionFinished={isMissionFinished}
            completenessForStats={envAction.completenessForStats}
          />
        </Stack.Item>
        <Stack.Item style={{ width: '100%' }}>
          <Label>Thématique de contrôle</Label>
          <Text as="h3" weight="medium" color={THEME.color.gunMetal}>
            {actionData?.formattedControlPlans?.themes?.length
              ? actionData?.formattedControlPlans?.themes.join(', ')
              : 'inconnue'}
          </Text>
        </Stack.Item>
        <Stack.Item style={{ width: '100%' }}>
          <Label>Sous-thématiques de contrôle</Label>
          <Text as="h3" weight="medium" color={THEME.color.gunMetal}>
            {actionData?.formattedControlPlans?.subThemes?.length
              ? actionData?.formattedControlPlans?.subThemes?.join(', ')
              : 'inconnues'}
          </Text>
        </Stack.Item>
        <Stack.Item style={{ width: '100%' }}>
          <Label>Date et heure</Label>
          <DatePicker
            defaultValue={envAction.startDateTimeUtc}
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
            defaultValue={(extractLatLonFromMultiPoint(actionData?.geom) as Coordinates) || undefined}
            coordinatesFormat={CoordinatesFormat.DEGREES_MINUTES_DECIMALS}
            // label="Lieu du contrôle"
            // isLight={true}
            disabled={true}
          />
        </Stack.Item>

        <Stack.Item style={{ width: '100%' }}>
          <Divider style={{ backgroundColor: THEME.color.charcoal }} />
        </Stack.Item>
        <Stack.Item style={{ width: '100%' }}>
          <Stack direction="column" alignItems="flex-start" spacing={'2rem'} style={{ width: '100%' }}>
            <Stack.Item style={{ width: '100%' }}>
              <Stack direction="row" alignItems="flex-start" spacing={'2rem'} style={{ width: '100%' }}>
                <Stack.Item style={{ width: '33%' }}>
                  <Stack direction="column" spacing={'1rem'} alignItems="flex-start">
                    <Stack.Item>
                      <Label>Nbre total de contrôles</Label>
                      <Text as="h3" weight="medium" color={THEME.color.gunMetal}>
                        {actionData?.actionNumberOfControls ?? 'inconnu'}
                      </Text>
                    </Stack.Item>
                    <Stack.Item>
                      <Label>Type de cible</Label>
                      <Text as="h3" weight="medium" color={THEME.color.gunMetal}>
                        {actionData?.actionTargetType
                          ? actionTargetTypeLabels[actionData?.actionTargetType].libelle
                          : 'inconnu'}
                      </Text>
                    </Stack.Item>
                    <Stack.Item>
                      <Label>Type de véhicule</Label>
                      <Text as="h3" weight="medium" color={THEME.color.gunMetal}>
                        {actionData?.vehicleType ? vehicleTypeLabels[actionData?.vehicleType].libelle : 'inconnu'}
                      </Text>
                    </Stack.Item>
                    <Stack.Item>
                      <Label>Observations</Label>
                      <Text as="h3" weight="medium" color={THEME.color.gunMetal}>
                        {actionData?.observations ?? 'aucunes'}
                      </Text>
                    </Stack.Item>
                  </Stack>
                </Stack.Item>

                <Stack.Item style={{ width: '67%' }}>
                  <Stack direction="column" alignItems="flex-start" spacing={'1.5rem'} style={{ width: '100%' }}>
                    {(actionData?.controlsToComplete?.length ?? 0) > 0 && (
                      <Stack.Item alignSelf="flex-end">
                        <ControlsToCompleteTag
                          amountOfControlsToComplete={actionData?.controlsToComplete?.length}
                          isLight={true}
                        />
                      </Stack.Item>
                    )}

                    <Stack.Item>
                      <Text as="h3" weight="bold" color={THEME.color.gunMetal}>
                        dont...
                      </Text>
                    </Stack.Item>
                    <Stack.Item style={{ width: '100%' }}>
                      <EnvControlForm
                        controlType={ControlType.ADMINISTRATIVE}
                        data={actionData?.controlAdministrative}
                        maxAmountOfControls={actionData?.actionNumberOfControls}
                        shouldCompleteControl={!!actionData?.controlsToComplete?.includes(ControlType.ADMINISTRATIVE)}
                      />
                    </Stack.Item>
                    <Stack.Item style={{ width: '100%' }}>
                      <EnvControlForm
                        controlType={ControlType.NAVIGATION}
                        data={actionData?.controlNavigation}
                        maxAmountOfControls={actionData?.actionNumberOfControls}
                        shouldCompleteControl={!!actionData?.controlsToComplete?.includes(ControlType.NAVIGATION)}
                      />
                    </Stack.Item>
                    <Stack.Item style={{ width: '100%' }}>
                      <EnvControlForm
                        controlType={ControlType.GENS_DE_MER}
                        data={actionData?.controlGensDeMer}
                        maxAmountOfControls={actionData?.actionNumberOfControls}
                        shouldCompleteControl={!!actionData?.controlsToComplete?.includes(ControlType.GENS_DE_MER)}
                      />
                    </Stack.Item>
                    <Stack.Item style={{ width: '100%' }}>
                      <EnvControlForm
                        controlType={ControlType.SECURITY}
                        data={actionData?.controlSecurity}
                        maxAmountOfControls={actionData?.actionNumberOfControls}
                        shouldCompleteControl={!!actionData?.controlsToComplete?.includes(ControlType.SECURITY)}
                      />
                    </Stack.Item>
                  </Stack>
                </Stack.Item>
              </Stack>
            </Stack.Item>
            <Stack.Item style={{ width: '100%' }}>
              <EnvInfractionAddNewTarget
                availableControlTypesForInfraction={actionData?.availableControlTypesForInfraction}
              />
            </Stack.Item>
            <Stack.Item style={{ width: '100%' }}>
              <EnvInfractionExistingTargets
                infractionsByTarget={actionData?.infractions}
                availableControlTypesForInfraction={actionData?.availableControlTypesForInfraction}
              />
            </Stack.Item>
          </Stack>
        </Stack.Item>
      </Stack>
    )
  }

  return null
}

export default ActionControlEnv
