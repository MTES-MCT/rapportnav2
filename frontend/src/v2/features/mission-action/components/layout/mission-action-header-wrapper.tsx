import { MissionStatusEnum } from '@common/types/mission-types.ts'
import { IconProps } from '@mtes-mct/monitor-ui'
import { FC, FunctionComponent } from 'react'
import { Stack } from 'rsuite'
import { MissionAction } from '../../../common/types/mission-action.ts'
import MissionActionHeaderCompletenessForStats from '../elements/mission-action-header-completeness-for-stats.tsx'
import { MissionActionHeaderTitleWrapper } from './mission-action-header-title-wrapper.tsx'

export type MissionActionHeaderWrapperProps = {
  title?: string
  actionId?: string
  missionId: number
  action: MissionAction
  icon?: FunctionComponent<IconProps>
  missionStatus?: MissionStatusEnum
}

const MissionActionHeaderWrapper: FC<MissionActionHeaderWrapperProps> = ({
  icon,
  title,
  action,
  missionId,
  missionStatus
}) => {
  return (
    <Stack direction="column" spacing={'0.5rem'}>
      <Stack.Item style={{ width: '100%' }}>
        {action?.actionType && (
          <MissionActionHeaderTitleWrapper
            icon={icon}
            title={title}
            actionId={action.id}
            missionId={missionId}
            source={action.source}
            startDateTimeUtc={action?.data.startDateTimeUtc}
          />
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

export default MissionActionHeaderWrapper
