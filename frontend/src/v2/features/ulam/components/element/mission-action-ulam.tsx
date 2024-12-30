import { FC } from 'react'
import useGetActionQuery from '../../../common/services/use-mission-action'
import MissionActionWrapper from '../../../mission-action/components/layout/mission-action-wrapper'
import MissionActionItemUlam from './mission-action-item-ulam'

interface MissionActionProps {
  actionId?: string
  missionId: number
}

const MissionActionUlam: FC<MissionActionProps> = ({ missionId, actionId }) => {
  const query = useGetActionQuery(missionId, actionId)
  return (
    <MissionActionWrapper
      action={query.data}
      missionId={missionId}
      isError={query.error}
      isLoading={query.isLoading}
      item={MissionActionItemUlam}
    />
  )
}

export default MissionActionUlam
