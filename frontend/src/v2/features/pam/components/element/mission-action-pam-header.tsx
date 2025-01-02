import { MissionStatusEnum } from '@common/types/mission-types.ts'
import { FC } from 'react'
import { MissionAction } from '../../../common/types/mission-action'
import MissionActionHeaderWrapper from '../../../mission-action/components/layout/mission-action-header-wrapper'
import { usePamActionRegistry } from '../../hooks/use-pam-action-registry'

export type MissionActionHeaderProps = {
  missionId: number
  action: MissionAction
  missionStatus?: MissionStatusEnum
}

const MissionActionPamHeader: FC<MissionActionHeaderProps> = ({ action, missionId, missionStatus }) => {
  const { title, icon } = usePamActionRegistry(action.actionType)

  return (
    <MissionActionHeaderWrapper
      icon={icon}
      title={title}
      action={action}
      missionId={missionId}
      missionStatus={missionStatus}
    />
  )
}

export default MissionActionPamHeader
