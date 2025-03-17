import { FC } from 'react'
import MissionPageHeader from '../../../common/components/ui/mission-page-header'
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
    <MissionPageHeader
      mission={mission}
      onClickClose={onClickClose}
      onClickExport={onClickExport}
      exportLoading={exportLoading}
    />
  )
}

export default MissionHeaderPam
