import React from 'react'
import { Accent, Icon, Tag, THEME } from '@mtes-mct/monitor-ui'
import { FlexboxGrid, Stack } from 'rsuite'
import { Action, ActionControl } from '../../../../../../common/types/action-types.ts'
import { controlMethodToHumanString, vesselTypeToHumanString } from '../../../../utils/control-utils.ts'
import Text from '../../../../../../common/components/ui/text.tsx'
import { useParams } from 'react-router-dom'
import { TimelineItemWrapper } from './timeline-item.tsx'

const ActionNavControl: React.FC<{ action: Action; onClick: any }> = ({ action, onClick }) => {
  const { actionId } = useParams()
  const actionData = action.data as unknown as ActionControl
  return (
    <TimelineItemWrapper onClick={onClick} borderWhenSelected={action.id === actionId}>
      <FlexboxGrid.Item style={{ width: '100%', padding: '1rem' }}>
        <Stack direction="row" spacing="0.5rem">
          <Stack.Item alignSelf="flex-start" style={{ paddingTop: '0.2rem' }}>
            <Icon.ControlUnit color={THEME.color.charcoal} size={20} />
          </Stack.Item>
          <Stack.Item>
            <Stack direction="column" alignItems="flex-start" spacing="0.5rem">
              <Stack.Item>
                <Stack direction="row" spacing="0.5rem">
                  <Stack.Item>
                    <Text as="h3" weight="medium" color={THEME.color.gunMetal}>
                      Contrôles{' '}
                      <b>{`${controlMethodToHumanString(actionData?.controlMethod)} - ${vesselTypeToHumanString(
                        actionData?.vesselType
                      )}`}</b>
                    </Text>
                  </Stack.Item>
                </Stack>
              </Stack.Item>
              {action.summaryTags?.length > 0 && (
                <Stack.Item data-testid={'nav-summary-tags'}>
                  {action.summaryTags?.map((tag: string) => (
                    <Tag key={tag} accent={Accent.PRIMARY} style={{ marginRight: '0.5rem' }}>
                      {tag}
                    </Tag>
                  ))}
                </Stack.Item>
              )}
            </Stack>
          </Stack.Item>
        </Stack>
      </FlexboxGrid.Item>
    </TimelineItemWrapper>
  )
}

export default ActionNavControl
