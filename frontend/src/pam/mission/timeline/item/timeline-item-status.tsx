import { FC } from 'react'
import { Icon, THEME } from '@mtes-mct/monitor-ui'
import { Stack } from 'rsuite'
import { ActionStatus as NavActionStatus } from '../../../../types/action-types'
import { StatusColorTag } from '../../status/status-selection-dropdown'
import { mapStatusToText, statusReasonToHumanString } from '../../status/utils'
import Text from '../../../../ui/text'
import { useParams } from 'react-router-dom'
import { MissionTimelineItemProps, TimelineItemWrapper } from './timeline-item.tsx'

const ActionStatus: FC<MissionTimelineItemProps> = ({ action, previousActionWithSameType, onClick }) => {
  const { actionId } = useParams()
  const isSelected = action.id === actionId
  const actionData = action.data as unknown as NavActionStatus
  const prevActionData = previousActionWithSameType?.data as unknown as NavActionStatus
  return (
    <TimelineItemWrapper onClick={onClick} borderWhenSelected={false}>
      <Stack direction={'column'} spacing={'0.2rem'} style={{ padding: '0.2rem 0' }}>
        <Stack.Item alignSelf={'flex-start'}>
          <Stack alignItems="center" spacing="0.5rem" style={{ width: '100%' }}>
            <Stack.Item>
              <StatusColorTag status={actionData?.status} />
            </Stack.Item>
            <Stack.Item style={{ maxWidth: 'calc(100% - 3rem)' }}>
              <Text
                as="h3"
                weight="normal"
                color={isSelected ? THEME.color.charcoal : THEME.color.slateGray}
                decoration={isSelected ? 'underline' : 'normal'}
                style={{
                  whiteSpace: 'nowrap',
                  overflow: 'hidden',
                  textOverflow: 'ellipsis'
                }}
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
        </Stack.Item>
        {!!prevActionData && (
          <Stack.Item alignSelf={'flex-start'}>
            <Text as={'h3'} color={THEME.color.slateGray} fontStyle={'italic'}>
              {`${mapStatusToText(prevActionData?.status)} ${prevActionData?.reason ? `- ${statusReasonToHumanString(prevActionData?.reason)} ` : ''}- fin`}
            </Text>
          </Stack.Item>
        )}
      </Stack>
    </TimelineItemWrapper>
  )
}

export default ActionStatus
