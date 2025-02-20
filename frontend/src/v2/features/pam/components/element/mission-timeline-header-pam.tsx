import { FC } from 'react'
import { useNavigate } from 'react-router-dom'
import { ModuleType } from 'src/v2/features/common/types/module-type'
import MissionTimelineHeaderWrapper from '../../../mission-timeline/components/layout/mission-timeline-Header-wrapper'
import { usePamTimelineRegistry } from '../../hooks/use-pam-timeline-registry'

interface MissionTimelineHeaderPamProps {
  missionId: number
}

const MissionTimelineHeaderPam: FC<MissionTimelineHeaderPamProps> = ({ missionId }) => {
  const navigate = useNavigate()
  const { timelineDropdownItems } = usePamTimelineRegistry()
  const handleOnSubmit = (id?: string) => navigate(`/v2/${ModuleType.PAM}/missions/${missionId}/${id}`)

  return (
    <MissionTimelineHeaderWrapper
      missionId={missionId}
      onSubmit={handleOnSubmit}
      moduleType={ModuleType.PAM}
      dropdownItems={timelineDropdownItems}
    />
  )
}

export default MissionTimelineHeaderPam
