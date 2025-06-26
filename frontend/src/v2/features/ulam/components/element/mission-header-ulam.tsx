import { FC } from 'react'
import MissionPageHeader from '../../../common/components/ui/mission-page-header'
import useGetMissionQuery from '../../../common/services/use-mission'

interface MissionHeaderProps {
  missionId?: string
  onClickClose: () => void
}

const MissionHeaderUlam: FC<MissionHeaderProps> = ({ missionId, onClickClose }) => {
  const { data: mission } = useGetMissionQuery(missionId)
  return <MissionPageHeader mission={mission} onClickClose={onClickClose} />
}

export default MissionHeaderUlam
