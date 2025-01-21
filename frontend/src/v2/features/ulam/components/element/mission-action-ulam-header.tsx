import { MissionStatusEnum } from '@common/types/mission-types.ts'
import { FC } from 'react'
import { MissionAction } from '../../../common/types/mission-action'
import { ModuleType } from '../../../common/types/module-type'
import MissionActionHeaderWrapper from '../../../mission-action/components/layout/mission-action-header-wrapper'
import { useUlamActionRegistry } from '../../hooks/use-ulam-action-registry'

export type MissionActionHeaderProps = {
  missionId: number
  action: MissionAction
  missionStatus?: MissionStatusEnum
}

const MissionActionUlamHeader: FC<MissionActionHeaderProps> = ({ action, missionId, missionStatus }) => {
  const { title, icon } = useUlamActionRegistry(action.actionType)

  return (
    <MissionActionHeaderWrapper
      icon={icon}
      title={title}
      action={action}
      missionId={missionId}
      moduleType={ModuleType.ULAM}
      missionStatus={missionStatus}
    />
  )
}

export default MissionActionUlamHeader
