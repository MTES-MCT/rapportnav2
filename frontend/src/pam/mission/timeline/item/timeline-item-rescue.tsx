import { FC } from 'react'
import { ActionRescue } from '../../../../types/action-types.ts'
import { useParams } from 'react-router-dom'
import { MissionTimelineItemProps, TimelineItemWrapper } from './timeline-item.tsx'
import { FlexboxGrid, Stack } from 'rsuite'
import { Icon, THEME } from '@mtes-mct/monitor-ui'
import Text from '../../../../ui/text.tsx'

const ActionOtherRescue: FC<MissionTimelineItemProps> = ({ action, onClick }) => {
  const { actionId } = useParams()
  const actionData = action.data as unknown as ActionRescue

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
            <Icon.Rescue color={THEME.color.charcoal} size={20} />
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
              {`Assistance / `}
              <b>{`${actionData?.isPersonRescue ? 'Sauvegarde de la vie humaine' : 'Assistance de navire en difficulté'}`}</b>
            </Text>
          </Stack.Item>
        </Stack>
      </FlexboxGrid.Item>
    </TimelineItemWrapper>
  )
}

export default ActionOtherRescue
