import React, { FC } from 'react'
import { Icon, THEME } from '@mtes-mct/monitor-ui'
import { FlexboxGrid, Stack } from 'rsuite'
import { ActionStatus as NavActionStatus } from '../../../../../../common/types/action-types.ts'
import { StatusColorTag } from '../../../ui/status-selection-dropdown.tsx'
import { mapStatusToText, statusReasonToHumanString } from '../../../../utils/status-utils.ts'
import Text from '../../../../../../common/components/ui/text.tsx'
import { useParams } from 'react-router-dom'
import { MissionTimelineItemProps, TimelineItemWrapper } from './timeline-item.tsx'

const ActionStatus: FC<MissionTimelineItemProps> = ({ action, previousActionWithSameType, onClick }) => {
  const { actionId } = useParams()
  const isSelected = action.id === actionId
  const actionData = action.data as unknown as NavActionStatus
  const prevActionData = previousActionWithSameType?.data as unknown as NavActionStatus
  return (
    <TimelineItemWrapper onClick={onClick} borderWhenSelected={false}>
      <FlexboxGrid.Item style={{ width: '100%', padding: '0.2rem 0' }}>
        <Stack direction="row" spacing="0.5rem">
          <Stack.Item>
            <StatusColorTag status={actionData?.status} />
          </Stack.Item>
          <Stack.Item alignSelf="flex-start" style={{ maxWidth: 'calc(100% - 3rem)' }}>
            <Text
              as="h3"
              weight="medium"
              color={isSelected ? THEME.color.charcoal : THEME.color.slateGray}
              style={{
                whiteSpace: 'nowrap',
                overflow: 'hidden',
                textOverflow: 'ellipsis',
                textDecoration: isSelected ? 'underline' : 'none'
              }}
              data-testid="timeline-item-status-description"
            >
              <b>{`${mapStatusToText(actionData?.status)} - début${
                !!actionData?.reason ? ' - ' + statusReasonToHumanString(actionData?.reason) : ''
              }`}</b>
              {!!actionData?.observations ? ' - ' + actionData?.observations : ''}
            </Text>
          </Stack.Item>
          <Stack.Item>
            <Icon.EditUnbordered size={20} color={THEME.color.slateGray} />
          </Stack.Item>
        </Stack>
        {!!prevActionData && (
          <Text as={'h3'} color={THEME.color.slateGray} fontStyle={'italic'}>
            {`${mapStatusToText(prevActionData?.status)} ${prevActionData?.reason ? `- ${statusReasonToHumanString(prevActionData?.reason)} ` : ''}- fin`}
          </Text>
        )}
      </FlexboxGrid.Item>
    </TimelineItemWrapper>
  )
}

export default ActionStatus
