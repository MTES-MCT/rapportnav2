import { FC } from 'react'
import MissionTimelineWrapper from '../../../../features/mission-timeline/components/layout/mission-timeline-wrapper'
import { MissionAction } from '../../../common/types/mission-action'
import { useTimeline } from '../../../mission-timeline/hooks/use-timeline'
import MissionTimelineItemUlam from './mission-timeline-item-ulam'

interface MissionTimelineProps {
  missionId: number
  isLoading: boolean
  isError: Error | null
  actions?: MissionAction[]
}

const MissionTimelineUlam: FC<MissionTimelineProps> = ({ isError, isLoading, actions, missionId }) => {
  const { getTimeLineAction } = useTimeline()
  //const query = useGetActionListQuery(missionId)
  return (
    <MissionTimelineWrapper
      isError={isError}
      missionId={missionId}
      groupBy="startDateTimeUtc"
      isLoading={isLoading}
      item={MissionTimelineItemUlam}
      actions={getTimeLineAction(actions)}
    />
  )
}

export default MissionTimelineUlam
