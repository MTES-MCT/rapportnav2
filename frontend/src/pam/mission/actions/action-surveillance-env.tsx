import React from 'react'
import { DateRangePicker, Icon, Label, THEME } from '@mtes-mct/monitor-ui'
import { Stack } from 'rsuite'
import Text from '../../../ui/text'
import { Action } from '../../../types/action-types'
import { EnvActionSurveillance } from '../../../types/env-mission-types'
import { useParams } from 'react-router-dom'
import useActionById from './use-action-by-id.tsx'
import useIsMissionFinished from '../use-is-mission-finished.tsx'
import ActionHeader from './action-header.tsx'

interface ActionSurveillancePropsEnv {
  action: Action
}

const ActionSurveillanceEnv: React.FC<ActionSurveillancePropsEnv> = ({ action }) => {
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
    const actionData: EnvActionSurveillance = envAction.data
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
            icon={Icon.Observation}
            title={'Surveillance Environnement'}
            date={envAction.startDateTimeUtc}
            showButtons={false}
            showStatus={true}
            isMissionFinished={isMissionFinished}
            completenessForStats={action.completenessForStats}
          />
        </Stack.Item>
        <Stack.Item style={{ width: '100%' }}>
          <Label>Date et heure de début et de fin</Label>
          <DateRangePicker
            defaultValue={[envAction.startDateTimeUtc, envAction.endDateTimeUtc ?? new Date()]}
            // label="Dates du rapport"
            withTime={true}
            isCompact={true}
            readOnly={true}
            disabled={true}
          />
        </Stack.Item>
        <Stack.Item style={{ width: '100%' }}>
          <Label>Thématique de contrôle</Label>
          <Text as="h3" weight="medium" color={THEME.color.gunMetal}>
            {actionData?.formattedControlPlans?.themes?.length
              ? actionData?.formattedControlPlans?.themes[0]
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

        <Stack.Item>
          <Label>Observations</Label>
          <Text as="h3" weight="medium" color={THEME.color.gunMetal}>
            {actionData?.observations || 'aucunes'}
          </Text>
        </Stack.Item>
      </Stack>
    )
  }

  return null
}

export default ActionSurveillanceEnv
