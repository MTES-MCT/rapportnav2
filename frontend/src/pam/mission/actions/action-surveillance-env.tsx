import React from 'react'
import { DateRangePicker, Icon, Label, Textarea, THEME } from '@mtes-mct/monitor-ui'
import { Stack } from 'rsuite'
import Text from '../../../ui/text'
import { formatDateTimeForFrenchHumans } from '../../../utils/dates.ts'
import { Action } from '../../../types/action-types'
import { EnvActionSurveillance } from '../../../types/env-mission-types'
import { useParams } from 'react-router-dom'
import useActionById from './use-action-by-id.tsx'

interface ActionSurveillancePropsEnv {
  action: Action
}

const ActionSurveillanceEnv: React.FC<ActionSurveillancePropsEnv> = ({ action }) => {
  const { missionId, actionId } = useParams()

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
          <Stack direction="row" spacing={'0.5rem'} style={{ width: '100%' }}>
            <Stack.Item alignSelf="baseline">
              <Icon.Observation color={THEME.color.charcoal} size={20} />
            </Stack.Item>
            <Stack.Item grow={2}>
              <Text as="h2" weight="bold">
                Surveillance Environnement{' '}
                {envAction.startDateTimeUtc && `(${formatDateTimeForFrenchHumans(envAction.startDateTimeUtc)})`}
              </Text>
            </Stack.Item>
          </Stack>
        </Stack.Item>
        <Stack.Item style={{ width: '100%' }}>
          <Label>Date et heure de début et de fin</Label>
          <DateRangePicker
            defaultValue={[envAction.startDateTimeUtc, envAction.endDateTimeUtc || new Date()]}
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
            {(!!actionData?.themes?.length && actionData?.themes[0]?.theme) ?? 'inconnue'}
          </Text>
        </Stack.Item>
        <Stack.Item style={{ width: '100%' }}>
          <Label>Sous-thématiques de contrôle</Label>
          <Text as="h3" weight="medium" color={THEME.color.gunMetal}>
            {!!actionData?.themes?.length && actionData?.themes[0]?.subThemes?.length
              ? actionData?.themes[0].subThemes?.join(', ')
              : 'inconnues'}
          </Text>
        </Stack.Item>

        <Stack.Item>
          <Label>Observations</Label>
          <Text as="h3" weight="medium" color={THEME.color.gunMetal}>
            {actionData?.observations || 'aucunes'}
          </Text>
        </Stack.Item>

        <Stack.Item style={{ width: '100%' }}>
          <Textarea label="Observations (unité)" isLight={true} name="observations" data-testid="observations" />
        </Stack.Item>
      </Stack>
    )
  }

  return null
}

export default ActionSurveillanceEnv
