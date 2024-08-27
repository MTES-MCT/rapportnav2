import React from 'react'
import { Action } from '@common/types/action-types.ts'
import { useParams } from 'react-router-dom'
import { EnvActionControl } from '@common/types/env-mission-types.ts'
import { TimelineItemWrapper } from './timeline-item.tsx'
import { FlexboxGrid, Stack } from 'rsuite'
import { Icon, THEME } from '@mtes-mct/monitor-ui'
import Text from '../../../../../../common/components/ui/text.tsx'

const ActionEnvSurveillance: React.FC<{ action: Action; onClick: any }> = ({ action, onClick }) => {
  const { actionId } = useParams()
  const actionData = action.data as unknown as EnvActionControl
  return (
    <TimelineItemWrapper onClick={onClick} borderWhenSelected={action.id === actionId}>
      <FlexboxGrid.Item
        style={{
          width: '100%',
          padding: '1rem'
        }}
      >
        <Stack direction="row" spacing="0.5rem">
          <Stack.Item alignSelf="flex-start">
            <Icon.Observation color={THEME.color.charcoal} size={20} />
          </Stack.Item>
          <Stack.Item alignSelf="flex-start" style={{ width: '100%' }}>
            <Stack direction="column" spacing="0.5rem" alignItems="flex-start" style={{ width: '100%' }}>
              <Stack.Item>
                <Stack direction="row" spacing="0.25rem">
                  <Stack.Item>
                    <Text as="h3" weight="medium" color={THEME.color.gunMetal}>
                      Surveillance{' '}
                      <b data-testid={'theme'}>
                        {actionData &&
                        'formattedControlPlans' in actionData &&
                        actionData?.formattedControlPlans?.themes?.length
                          ? actionData?.formattedControlPlans?.themes[0]
                          : 'environnement marin'}
                      </b>
                    </Text>
                  </Stack.Item>
                </Stack>
              </Stack.Item>

              <Stack.Item alignSelf="flex-end" style={{ width: '100%' }}>
                <Text as="h4" color={THEME.color.slateGray} fontStyle={'italic'} style={{ textAlign: 'right' }}>
                  ajouté par CACEM
                </Text>
              </Stack.Item>
            </Stack>
          </Stack.Item>
        </Stack>
      </FlexboxGrid.Item>
    </TimelineItemWrapper>
  )
}

export default ActionEnvSurveillance
