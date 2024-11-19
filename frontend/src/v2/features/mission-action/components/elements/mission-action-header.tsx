import { MissionStatusEnum } from '@common/types/mission-types.ts'
import { FC } from 'react'
import { Stack } from 'rsuite'
import { useMissionActionQuery } from '../../../common/services/use-mission-action.tsx'
import MissionActionHeaderCompletenessForStats from './mission-action-header-completeness-for-stats.tsx'
import MissionActionHeaderTitle from './mission-action-header-title.tsx'

export type MissionActionHeaderProps = {
  actionId?: string
  missionId?: number
  missionStatus?: MissionStatusEnum
}

const MissionActionHeader: FC<MissionActionHeaderProps> = ({ actionId, missionId, missionStatus }) => {
  const { data: action } = useMissionActionQuery(actionId, missionId)

  return (
    <Stack direction="column" spacing={'0.5rem'}>
      <Stack.Item style={{ width: '100%' }}>
        {action?.actionType && (
          <MissionActionHeaderTitle actionType={action.actionType} startDateTimeUtc={action?.data.startDateTimeUtc} />
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
