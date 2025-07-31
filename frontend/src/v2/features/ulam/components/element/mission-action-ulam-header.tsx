import { MissionStatusEnum } from '@common/types/mission-types.ts'
import { FC } from 'react'
import ActionHeaderWrapper from '../../../common/components/layout/action-header-wrapper'
import ActionHeaderCompletenessForStats from '../../../common/components/ui/action-header-completeness-for-stats'
import { useMissionFinished } from '../../../common/hooks/use-mission-finished'
import { MissionAction } from '../../../common/types/mission-action'
import { OwnerType } from '../../../common/types/owner-type'
import { useUlamActionRegistry } from '../../hooks/use-ulam-action-registry'

export type MissionActionHeaderProps = {
  missionId: string
  action: MissionAction
  missionStatus?: MissionStatusEnum
}

const MissionActionUlamHeader: FC<MissionActionHeaderProps> = ({ action, missionId, missionStatus }) => {
  const isMissionFinished = useMissionFinished(missionId)
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
        <ActionHeaderCompletenessForStats
          isMissionFinished={isMissionFinished}
          networkSyncStatus={action?.networkSyncStatus}
          completenessForStats={action?.completenessForStats}
        />
      }
    />
  )
}

export default MissionActionUlamHeader
