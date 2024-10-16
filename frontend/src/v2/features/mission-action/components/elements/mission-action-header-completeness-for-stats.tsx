import Text from '@common/components/ui/text'
import { CompletenessForStatsStatusEnum, MissionStatusEnum } from '@common/types/mission-types.ts'
import { createElement } from 'react'
import { Stack } from 'rsuite'
import { useMissionCompletenessForStats } from '../../../common/hooks/use-mission-completeness-for-stats'

interface MissionActionHeaderCompletenessForStatsProps {
  missionStatus?: MissionStatusEnum
  completenessForStats?: CompletenessForStatsStatusEnum
}

export const MissionActionHeaderCompletenessForStats: React.FC<MissionActionHeaderCompletenessForStatsProps> = ({
  missionStatus,
  completenessForStats
}) => {
  const { getCompletenessForStatsStatus } = useMissionCompletenessForStats(missionStatus, completenessForStats)
  const { icon, text, color } = getCompletenessForStatsStatus()

  return (
    <Stack spacing={'0.5rem'}>
      <Stack.Item alignSelf={'center'}>{createElement(icon)}</Stack.Item>
      <Stack.Item alignSelf={'baseline'}>
        <Text as={'h3'} color={color}>
          {text}
        </Text>
      </Stack.Item>
    </Stack>
  )
}

export default MissionActionHeaderCompletenessForStats
