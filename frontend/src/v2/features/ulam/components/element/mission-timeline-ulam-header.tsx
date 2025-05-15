import { FC } from 'react'
import { useNavigate } from 'react-router-dom'
import MissionTimelineHeaderWrapper from '../../../../features/mission-timeline/components/layout/mission-timeline-Header-wrapper'
import { ModuleType } from '../../../common/types/module-type'
import { useUlamTimelineRegistry } from '../../hooks/use-ulam-timeline-registry'
import useGetMissionGeneralInformationQuery
  from '../../../mission-general-infos/services/use-mission-general-information.tsx'
import { useMissionType } from '../../../common/hooks/use-mission-type.tsx'

interface MissionTimelineUlamHeaderProps {
  missionId: string
}

const MissionTimelineUlamHeader: FC<MissionTimelineUlamHeaderProps> = ({ missionId }) => {
  const navigate = useNavigate()
  const { timelineDropdownItems } = useUlamTimelineRegistry()
  const handleOnSubmit = (id?: string) => navigate(`/v2/${ModuleType.ULAM}/missions/${missionId}/${id}`)
  const { data: generalInfos } = useGetMissionGeneralInformationQuery(missionId)
  const { isExternalReinforcementTime } = useMissionType()


  if (isExternalReinforcementTime(generalInfos.missionReportType)) return null
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

export default MissionTimelineUlamHeader
