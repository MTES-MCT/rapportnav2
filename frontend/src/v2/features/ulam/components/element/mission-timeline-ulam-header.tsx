import { useGlobalRoutes } from '@router/use-global-routes'
import { FC } from 'react'
import { useNavigate } from 'react-router-dom'
import MissionTimelineHeaderWrapper from '../../../../features/mission-timeline/components/layout/mission-timeline-Header-wrapper'
import { MissionReportTypeEnum } from '../../../common/types/mission-types'
import { ModuleType } from '../../../common/types/module-type'
import { OwnerType } from '../../../common/types/owner-type'
import { useUlamTimelineRegistry } from '../../hooks/use-ulam-timeline-registry'

interface MissionTimelineUlamHeaderProps {
  missionId: string
  missionReportType?: MissionReportTypeEnum
}

const MissionTimelineUlamHeader: FC<MissionTimelineUlamHeaderProps> = ({ missionId, missionReportType }) => {
  const navigate = useNavigate()
  const { getUrl } = useGlobalRoutes()
  const { getTimelineDropdownItems } = useUlamTimelineRegistry()
  const handleOnSubmit = (id?: string) => navigate(`${getUrl(OwnerType.MISSION)}/${missionId}/${id}`)

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
