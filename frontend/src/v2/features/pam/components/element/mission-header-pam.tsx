import { FC } from 'react'
import MissionPageHeaderWrapper from '../../../common/components/layout/mission-page-header-wrapper'
import useGetMissionQuery from '../../../common/services/use-mission'

interface MissionHeaderProps {
  missionId?: number
  onClickClose: () => void
  onClickExport: () => void
  exportLoading?: boolean
}

const MissionHeaderPam: FC<MissionHeaderProps> = ({ missionId, onClickClose, onClickExport, exportLoading }) => {
  const { data: mission } = useGetMissionQuery(missionId)
  return (
    <MissionPageHeaderWrapper
      mission={mission}
      onClickClose={onClickClose}
      onClickExport={onClickExport}
      exportLoading={exportLoading}
    />
  )
}

export default MissionHeaderPam
