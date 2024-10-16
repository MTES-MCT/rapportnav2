import { FC } from 'react'
import { ModuleType } from '../../../common/types/module-type'
import MissionTimelineHeaderWrapper from '../../../mission-timeline/components/layout/mission-timeline-Header-wrapper'

interface MissionTimelineHeaderPamProps {
  missionId?: string
}

const MissionTimelineHeaderPam: FC<MissionTimelineHeaderPamProps> = ({ missionId }) => {
  return <MissionTimelineHeaderWrapper missionId={missionId} moduleType={ModuleType.PAM} />
}

export default MissionTimelineHeaderPam
