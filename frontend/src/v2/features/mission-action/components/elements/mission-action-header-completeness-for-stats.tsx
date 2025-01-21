import Text from '@common/components/ui/text'
import { CompletenessForStats, MissionStatusEnum } from '@common/types/mission-types.ts'
import { createElement } from 'react'
import { Stack } from 'rsuite'
import { useMissionCompletenessForStats } from '../../../common/hooks/use-mission-completeness-for-stats'

interface MissionActionHeaderCompletenessForStatsProps {
  missionStatus?: MissionStatusEnum
  completenessForStats?: CompletenessForStats
}

export const MissionActionHeaderCompletenessForStats: React.FC<MissionActionHeaderCompletenessForStatsProps> = ({
  missionStatus,
  completenessForStats
}) => {
  const { icon, statusMessage, color } = useMissionCompletenessForStats(completenessForStats, missionStatus)

  return (
    <Stack spacing={'0.5rem'}>
      <Stack.Item alignSelf={'center'} style={{ color: color }}>
        {createElement(icon)}
      </Stack.Item>
      <Stack.Item alignSelf={'baseline'}>
        <Text as={'h3'} color={color}>
          {statusMessage}
        </Text>
      </Stack.Item>
    </Stack>
  )
}

export default MissionActionHeaderCompletenessForStats
