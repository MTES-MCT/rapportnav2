import { FC } from 'react'
import { MissionReportTypeEnum } from 'src/v2/features/common/types/mission-types'
import { useMissionType } from '../../../common/hooks/use-mission-type'
import MissionTimelineWrapper from '../../../mission-timeline/components/layout/mission-timeline-wrapper'
import { MissionTimelineAction } from '../../../mission-timeline/types/mission-timeline-output'
import MissionTimelineItemUlam from './mission-timeline-ulam-item'

interface MissionTimelineBodyProps {
  missionId?: string
  isError?: boolean
  isLoading?: boolean
  actions: MissionTimelineAction[]
  missionReportType?: MissionReportTypeEnum
}

const MissionTimelineUlamBody: FC<MissionTimelineBodyProps> = ({
  missionId,
  actions,
  isError,
  isLoading,
  missionReportType
}) => {
  const { getNoTimelineMessage } = useMissionType()
  return (
    <MissionTimelineWrapper
      isError={isError}
      actions={actions}
      missionId={missionId}
      isLoading={isLoading}
      groupBy="startDateTimeUtc"
      item={MissionTimelineItemUlam}
      noTimelineMessage={getNoTimelineMessage(missionReportType)}
    />
  )
}

export default MissionTimelineUlamBody
