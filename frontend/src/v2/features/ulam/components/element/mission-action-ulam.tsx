import { FC } from 'react'
import { useMissionActionQuery } from '../../../common/services/use-mission-action'
import MissionActionWrapper from '../../../mission-action/components/layout/mission-action-wrapper'
import MissionActionItemUlam from './mission-action-item-ulam'

interface MissionActionProps {
  actionId?: string
  missionId?: number
}

const MissionActionUlam: FC<MissionActionProps> = ({ missionId, actionId }) => {
  const { data: action, loading, error } = useMissionActionQuery(actionId, missionId)
  return (
    <MissionActionWrapper
      action={action}
      isError={error}
      isLoading={loading}
      missionId={action?.missionId}
      item={MissionActionItemUlam}
    />
  )
}

export default MissionActionUlam
