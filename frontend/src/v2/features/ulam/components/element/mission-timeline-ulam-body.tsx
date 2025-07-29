import { useGlobalRoutes } from '@router/use-global-routes'
import { FC } from 'react'
import TimelineWrapper from '../../../common/components/layout/timeline-wrapper'
import { useMissionType } from '../../../common/hooks/use-mission-type'
import { MissionReportTypeEnum } from '../../../common/types/mission-types'
import { OwnerType } from '../../../common/types/owner-type'
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
  const { getUrl } = useGlobalRoutes()
  const { getNoTimelineMessage } = useMissionType()
  return (
    <TimelineWrapper
      isError={isError}
      actions={actions}
      isLoading={isLoading}
      groupBy="startDateTimeUtc"
      item={MissionTimelineItemUlam}
      baseUrl={`${getUrl(OwnerType.MISSION)}/${missionId}`}
      noTimelineMessage={getNoTimelineMessage(missionReportType)}
    />
  )
}

export default MissionTimelineUlamBody
