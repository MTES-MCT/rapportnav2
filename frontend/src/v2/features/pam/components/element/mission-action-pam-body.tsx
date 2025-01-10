import { FC } from 'react'
import { MissionAction } from '../../../common/types/mission-action'
import MissionActionWrapper from '../../../mission-action/components/layout/mission-action-wrapper'
import MissionActionItemPam from './mission-action-item-pam'

interface MissionActionProps {
  missionId: number
  isLoading?: boolean
  error?: Error | null
  action?: MissionAction
}

const MissionActionPamBody: FC<MissionActionProps> = ({ missionId, error, action, isLoading }) => {
  return (
    <MissionActionWrapper
      action={action}
      isError={error}
      missionId={missionId}
      isLoading={isLoading}
      item={MissionActionItemPam}
    />
  )
}

export default MissionActionPamBody
