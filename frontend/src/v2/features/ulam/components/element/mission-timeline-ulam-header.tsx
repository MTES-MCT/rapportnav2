import { FC } from 'react'
import { useNavigate } from 'react-router-dom'
import MissionTimelineHeaderWrapper from '../../../../features/mission-timeline/components/layout/mission-timeline-Header-wrapper'
import { MissionReportTypeEnum } from '../../../common/types/mission-types'
import { ModuleType } from '../../../common/types/module-type'
import { useUlamTimelineRegistry } from '../../hooks/use-ulam-timeline-registry'

interface MissionTimelineUlamHeaderProps {
  missionId: string
  missionReportType?: MissionReportTypeEnum
}

const MissionTimelineUlamHeader: FC<MissionTimelineUlamHeaderProps> = ({ missionId, missionReportType }) => {
  const navigate = useNavigate()
  const { getTimelineDropdownItems } = useUlamTimelineRegistry()
  const handleOnSubmit = (id?: string) => navigate(`/v2/${ModuleType.ULAM}/missions/${missionId}/${id}`)

  return (
    <MissionTimelineHeaderWrapper
      hideStatus={true}
      missionId={missionId}
      onSubmit={handleOnSubmit}
      moduleType={ModuleType.ULAM}
      dropdownItems={getTimelineDropdownItems(missionReportType)}
    />
  )
}

export default MissionTimelineUlamHeader
