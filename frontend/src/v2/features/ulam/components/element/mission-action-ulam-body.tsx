import { FC } from 'react'
import { MissionAction } from '../../../common/types/mission-action'
import MissionActionWrapper from '../../../mission-action/components/layout/mission-action-wrapper'
import MissionActionItemUlam from './mission-action-item-ulam'

interface MissionActionProps {
  missionId: string
  isLoading?: boolean
  error?: Error | null
  action?: MissionAction
}

const MissionActionUlamBody: FC<MissionActionProps> = ({ missionId, error, action, isLoading }) => {
  return (
    <MissionActionWrapper
      action={action}
      isError={error}
      missionId={missionId}
      isLoading={isLoading}
      item={MissionActionItemUlam}
    />
  )
}

export default MissionActionUlamBody
