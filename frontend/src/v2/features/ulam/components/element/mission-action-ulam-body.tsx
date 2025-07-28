import { FC } from 'react'
import ActionWrapper from '../../../common/components/layout/action-wrapper'
import { MissionAction } from '../../../common/types/mission-action'
import MissionActionItemUlam from './mission-action-item-ulam'

interface MissionActionProps {
  missionId: string
  isLoading?: boolean
  error?: Error | null
  action?: MissionAction
}

const MissionActionUlamBody: FC<MissionActionProps> = ({ missionId, error, action, isLoading }) => {
  return (
    <ActionWrapper
      action={action}
      isError={error}
      ownerId={missionId}
      isLoading={isLoading}
      item={MissionActionItemUlam}
    />
  )
}

export default MissionActionUlamBody
