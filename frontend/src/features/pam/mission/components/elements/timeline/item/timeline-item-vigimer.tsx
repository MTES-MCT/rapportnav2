import { FC } from 'react'
import { useParams } from 'react-router-dom'
import { MissionTimelineItemProps, TimelineItemWrapper } from './timeline-item.tsx'
import { FlexboxGrid, Stack } from 'rsuite'
import { Icon, THEME } from '@mtes-mct/monitor-ui'
import Text from '../../../../../../common/components/ui/text.tsx'
import { ActionVigimer as NavActionVigimer } from '../../../../../../common/types/action-types.ts'

const ActionVigimer: FC<MissionTimelineItemProps> = ({ action, onClick }) => {
  const { actionId } = useParams()
  const actionData = action.data as unknown as NavActionVigimer

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
            <Icon.More color={THEME.color.charcoal} size={20} />
          </Stack.Item>
          <Stack.Item alignSelf="flex-start" style={{ maxWidth: 'calc(100% - 3rem)' }}>
            <Text
              as="h3"
              weight="medium"
              color={THEME.color.gunMetal}
              style={{
                whiteSpace: 'nowrap',
                overflow: 'hidden',
                textOverflow: 'ellipsis'
              }}
            >
              {'Permanence Vigimer'}
            </Text>
          </Stack.Item>
        </Stack>
      </FlexboxGrid.Item>
    </TimelineItemWrapper>
  )
}

export default ActionVigimer
