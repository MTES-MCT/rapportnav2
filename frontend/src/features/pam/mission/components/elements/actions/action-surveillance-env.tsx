import React from 'react'
import { Icon, Label, THEME } from '@mtes-mct/monitor-ui'
import { Stack } from 'rsuite'
import Text from '../../../../../common/components/ui/text.tsx'
import { Action } from '@common/types/action-types.ts'
import { EnvActionSurveillance } from '@common/types/env-mission-types.ts'
import { useParams } from 'react-router-dom'
import useActionById from '../../../hooks/use-action-by-id.tsx'
import useIsMissionFinished from '../../../hooks/use-is-mission-finished.tsx'
import ActionHeader from './action-header.tsx'
import ActionEnvObservationsUnit from './action-env-observations-unit.tsx'
import ActionEnvDateRange from './action-env-daterange.tsx'
import EnvActionControlPlans from '@common/components/elements/env-action-control-plans.tsx'

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
          <ActionEnvDateRange
            missionId={missionId}
            actionId={actionId}
            startDateTimeUtc={envAction.startDateTimeUtc}
            endDateTimeUtc={envAction.endDateTimeUtc}
          />
        </Stack.Item>
        <Stack.Item style={{ width: '100%' }}>
          <EnvActionControlPlans controlPlans={actionData?.formattedControlPlans} />
        </Stack.Item>

        <Stack.Item>
          <Label>Observations</Label>
          <Text as="h3" weight="medium" color={THEME.color.gunMetal}>
            {actionData?.observations || 'aucunes'}
          </Text>
        </Stack.Item>

        <Stack.Item style={{ width: '100%' }}>
          <ActionEnvObservationsUnit
            missionId={missionId}
            actionId={actionId}
            observationsByUnit={actionData.observationsByUnit}
            label={'Observations (unités)'}
          />
        </Stack.Item>
      </Stack>
    )
  }

  return null
}

export default ActionSurveillanceEnv
