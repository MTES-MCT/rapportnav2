import { FC } from 'react'
import { ModuleType } from '../../../../features/common/types/module-type'
import MissionTimelineHeaderWrapper from '../../../../features/mission-timeline/components/layout/mission-timeline-Header-wrapper'

interface MissionTimelineHeaderUlamProps {
  missionId?: string
}

const MissionTimelineHeaderUlam: FC<MissionTimelineHeaderUlamProps> = ({ missionId }) => {
  return <MissionTimelineHeaderWrapper missionId={missionId} moduleType={ModuleType.ULAM} hideStatus={true} />
}

export default MissionTimelineHeaderUlam
