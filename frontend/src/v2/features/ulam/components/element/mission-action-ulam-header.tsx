import { MissionStatusEnum } from '@common/types/mission-types.ts'
import { FC } from 'react'
import ActionHeaderWrapper from '../../../common/components/layout/action-header-wrapper'
import { MissionAction } from '../../../common/types/mission-action'
import { OwnerType } from '../../../common/types/owner-type'
import MissionActionHeaderCompletenessForStats from '../../../mission-action/components/elements/mission-action-header-completeness-for-stats'
import { useUlamActionRegistry } from '../../hooks/use-ulam-action-registry'

export type MissionActionHeaderProps = {
  missionId: string
  action: MissionAction
  missionStatus?: MissionStatusEnum
}

const MissionActionUlamHeader: FC<MissionActionHeaderProps> = ({ action, missionId, missionStatus }) => {
  const { title, icon } = useUlamActionRegistry(action.actionType)

  return (
    <ActionHeaderWrapper
      icon={icon}
      title={title}
      action={action}
      ownerId={missionId}
      ownerType={OwnerType.MISSION}
      missionStatus={missionStatus}
      completeness={
        <MissionActionHeaderCompletenessForStats
          missionId={missionId}
          networkSyncStatus={action?.networkSyncStatus}
          completenessForStats={action?.completenessForStats}
        />
      }
    />
  )
}

export default MissionActionUlamHeader
