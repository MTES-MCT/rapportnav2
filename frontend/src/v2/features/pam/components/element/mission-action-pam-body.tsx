import { FC } from 'react'
import ActionWrapper from '../../../common/components/layout/action-wrapper'
import { MissionAction } from '../../../common/types/mission-action'
import MissionActionItemPam from './mission-action-pam-item'

interface MissionActionProps {
  missionId: string
  isLoading?: boolean
  error?: Error | null
  action?: MissionAction
}

const MissionActionPamBody: FC<MissionActionProps> = ({ missionId, error, action, isLoading }) => {
  return (
    <ActionWrapper
      action={action}
      isError={error}
      ownerId={missionId}
      isLoading={isLoading}
      item={MissionActionItemPam}
    />
  )
}

export default MissionActionPamBody
