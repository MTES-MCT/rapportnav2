import { FC } from 'react'
import { useNavigate } from 'react-router-dom'
import MissionTimelineHeaderWrapper from '../../../../features/mission-timeline/components/layout/mission-timeline-Header-wrapper'
import { ModuleType } from '../../../common/types/module-type'
import { useUlamTimelineRegistry } from '../../hooks/use-ulam-timeline-registry'

interface MissionTimelineHeaderUlamProps {
  missionId: number
}

const MissionTimelineHeaderUlam: FC<MissionTimelineHeaderUlamProps> = ({ missionId }) => {
  const navigate = useNavigate()
  const { timelineDropdownItems } = useUlamTimelineRegistry()
  const handleOnSubmit = (id?: string) => navigate(`/v2/${ModuleType.ULAM}/missions/${missionId}/${id}`)
  return (
    <MissionTimelineHeaderWrapper
      hideStatus={true}
      missionId={missionId}
      onSubmit={handleOnSubmit}
      moduleType={ModuleType.ULAM}
      dropdownItems={timelineDropdownItems}
    />
  )
}

export default MissionTimelineHeaderUlam
