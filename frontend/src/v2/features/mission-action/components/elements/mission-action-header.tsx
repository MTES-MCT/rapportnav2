import { ActionTypeEnum, MissionSourceEnum } from '@common/types/env-mission-types.ts'
import { MissionStatusEnum } from '@common/types/mission-types.ts'
import { FC } from 'react'
import { Stack } from 'rsuite'
import useActionByIdQuery from '../../services/use-action-by-id.tsx'
import MissionActionHeaderCompletenessForStats from './mission-action-header-completeness-for-stats.tsx'
import MissionActionHeaderTitle from './mission-action-header-title.tsx'

export type MissionActionHeaderProps = {
  actionId?: string
  missionId?: string
  missionStatus?: MissionStatusEnum
}

const MissionActionHeader: FC<MissionActionHeaderProps> = ({ actionId, missionId, missionStatus }) => {
  const { data: action } = useActionByIdQuery(
    actionId,
    missionId,
    MissionSourceEnum.RAPPORTNAV,
    ActionTypeEnum.ANTI_POLLUTION
  )

  return (
    <Stack direction="column" spacing={'0.5rem'}>
      <Stack.Item style={{ width: '100%' }}>
        {action?.type && (
          <MissionActionHeaderTitle actionType={action.type} startDateTimeUtc={action?.startDateTimeUtc} />
        )}
      </Stack.Item>
      <Stack.Item style={{ width: '100%' }}>
        <MissionActionHeaderCompletenessForStats
          missionStatus={missionStatus}
          completenessForStats={action?.completenessForStats}
        />
      </Stack.Item>
    </Stack>
  )
}

export default MissionActionHeader
