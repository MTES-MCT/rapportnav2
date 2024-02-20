import React from 'react'
import { Icon, THEME } from '@mtes-mct/monitor-ui'
import { Stack } from 'rsuite'
import { Action, ActionStatus as NavActionStatus } from '../../../../types/action-types'
import { StatusColorTag } from '../../status/status-selection-dropdown'
import { mapStatusToText, statusReasonToHumanString } from '../../status/utils'
import Text from '../../../../ui/text'
import { useParams } from 'react-router-dom'
import { TimelineItemWrapper } from "./timeline-item.tsx";

const ActionStatus: React.FC<{ action: Action; onClick: any }> = ({action, onClick}) => {
  const {actionId} = useParams()
  const isSelected = action.id === actionId
  const actionData = action.data as unknown as NavActionStatus
  return (
    <TimelineItemWrapper onClick={onClick} borderWhenSelected={false}>
      <Stack alignItems="center" spacing="0.5rem">
        <Stack.Item>
          <StatusColorTag status={actionData?.status}/>
        </Stack.Item>
        <Stack.Item>

          <div
            style={{
              whiteSpace: 'nowrap',
              overflow: 'hidden',
              textOverflow: 'ellipsis',
              maxWidth: '500px'
            }}
          >
            <Text
              as="h3"
              weight="normal"
              color={isSelected ? THEME.color.charcoal : THEME.color.slateGray}
              decoration={isSelected ? 'underline' : 'normal'}
            >
              <b>{`${mapStatusToText(actionData?.status)} - début${
                !!actionData?.reason ? ' - ' + statusReasonToHumanString(actionData?.reason) : ''
              }`}</b>
              {!!actionData?.observations ? ' - ' + actionData?.observations : ''}
            </Text>
          </div>
        </Stack.Item>
        <Stack.Item>
          <Icon.EditUnbordered size={20} color={THEME.color.slateGray}/>
        </Stack.Item>
      </Stack>
    </TimelineItemWrapper>
  )
}

export default ActionStatus
