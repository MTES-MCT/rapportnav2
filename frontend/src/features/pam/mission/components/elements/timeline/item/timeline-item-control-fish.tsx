import React from 'react'
import { Accent, Icon, Tag, THEME } from '@mtes-mct/monitor-ui'
import { FlexboxGrid, Stack } from 'rsuite'
import { Action } from '../../../../../../common/types/action-types.ts'
import { FishAction, formatMissionActionTypeForHumans } from '../../../../../../common/types/fish-mission-types.ts'
import ControlsToCompleteTag from '../../../ui/controls-to-complete-tag.tsx'
import Text from '../../../../../../common/components/ui/text.tsx'
import { useParams } from 'react-router-dom'
import { vesselNameOrUnknown } from '../../../../utils/action-utils.ts'
import { TimelineItemWrapper } from './timeline-item.tsx'

const ActionFishControl: React.FC<{ action: Action; onClick: any }> = ({ action, onClick }) => {
  const { actionId } = useParams()
  const actionData = action.data as unknown as FishAction
  return (
    <TimelineItemWrapper onClick={onClick} borderWhenSelected={action.id === actionId}>
      <FlexboxGrid.Item style={{ width: '100%', padding: '1rem' }}>
        <Stack direction="row" spacing="0.5rem">
          <Stack.Item alignSelf="flex-start">
            <Icon.ControlUnit color={THEME.color.charcoal} size={20} />
          </Stack.Item>
          <Stack.Item alignSelf="flex-start" style={{ width: '100%' }}>
            <Stack direction="column" spacing="0.5rem" alignItems="flex-start" style={{ width: '100%' }}>
              <Stack.Item>
                <Text as="h3" weight="medium" color={THEME.color.gunMetal}>
                  {formatMissionActionTypeForHumans(actionData?.actionType)} -{' '}
                  {vesselNameOrUnknown(actionData?.vesselName)}
                </Text>
              </Stack.Item>

              <Stack.Item alignSelf="flex-start" style={{ width: '100%' }}>
                <Stack direction="row" spacing="1rem" wrap={true}>
                  <Stack.Item grow={3}>
                    {actionData?.controlsToComplete !== undefined && actionData?.controlsToComplete?.length > 0 ? (
                      <ControlsToCompleteTag amountOfControlsToComplete={actionData?.controlsToComplete?.length} />
                    ) : (
                      <>
                        {action.summaryTags?.map((tag: string) => (
                          <Tag key={tag} accent={Accent.PRIMARY} style={{ marginRight: '0.5rem' }}>
                            {tag}
                          </Tag>
                        ))}
                      </>
                    )}
                  </Stack.Item>
                  <Stack.Item alignSelf="flex-end">
                    <Text as="h4" color={THEME.color.slateGray} fontStyle="italic">
                      ajouté par CNSP
                    </Text>
                  </Stack.Item>
                </Stack>
              </Stack.Item>
            </Stack>
          </Stack.Item>
        </Stack>
      </FlexboxGrid.Item>
    </TimelineItemWrapper>
  )
}

export default ActionFishControl
