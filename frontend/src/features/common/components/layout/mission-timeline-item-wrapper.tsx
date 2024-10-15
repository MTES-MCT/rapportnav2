import MissionTimelineItemDate from '@common/components/ui/v2/mission-timeline-item-date'
import { Action } from '@common/types/action-types'
import { FC } from 'react'
import { Stack } from 'rsuite'
import MissionTimelineItemStatus from '../elements/v2/mission-timeline-item-status'

interface MissionTimelineItemWrapperProps {
  action: Action
  card: JSX.Element
}

const MissionTimelineItemWrapper: FC<MissionTimelineItemWrapperProps> = ({ action, card }) => {
  return (
    <Stack direction="row" spacing={'0.5rem'} style={{ overflow: 'hidden' }}>
      <Stack.Item style={{ minWidth: '50px' }}>
        <MissionTimelineItemDate date={action.startDateTimeUtc} />
      </Stack.Item>
      <Stack.Item style={{ width: '100%', maxWidth: 'calc(100% - 5.4rem', overflow: 'hidden' }}>{card}</Stack.Item>
      <Stack.Item alignSelf="stretch">
        <MissionTimelineItemStatus
          type={action.type}
          status={action.status}
          completenessForStats={action.completenessForStats}
        />
      </Stack.Item>
    </Stack>
  )
}

export default MissionTimelineItemWrapper
