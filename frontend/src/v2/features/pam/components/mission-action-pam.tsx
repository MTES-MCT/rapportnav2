import { FC } from 'react'
import { useMissionActionQuery } from '../../common/services/use-mission-action'
import MissionActionWrapper from '../../mission-action/components/layout/mission-action-wrapper'
import MissionActionItemPam from './mission-action-item-pam'

interface MissionActionProps {
  actionId?: string
  missionId?: number
}

const MissionActionPam: FC<MissionActionProps> = ({ missionId, actionId }) => {
  const { data: action, loading, error } = useMissionActionQuery(actionId, missionId)
  return (
    <MissionActionWrapper
      action={action}
      isError={error}
      isLoading={loading}
      missionId={action?.missionId}
      item={MissionActionItemPam}
    />
  )
}

export default MissionActionPam
