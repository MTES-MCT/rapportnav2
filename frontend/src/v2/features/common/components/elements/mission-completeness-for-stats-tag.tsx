import Text from '@common/components/ui/text'
import { CompletenessForStats, MissionStatusEnum } from '@common/types/mission-types.ts'
import { Tag, THEME } from '@mtes-mct/monitor-ui'
import { FC } from 'react'
import { useMissionCompletenessForStats } from '../../hooks/use-mission-completeness-for-stats'

interface MissionStatusTagProps {
  missionStatus?: MissionStatusEnum
  completenessForStats?: CompletenessForStats
}

const MissionCompletenessForStatsTag: FC<MissionStatusTagProps> = ({ missionStatus, completenessForStats }) => {
  const { color, icon, text } = useMissionCompletenessForStats(completenessForStats, missionStatus)
  return (
    <Tag iconColor={color} backgroundColor={THEME.color.cultured} color={color} Icon={icon} withCircleIcon={true}>
      <Text as="h3" weight="medium" color={color}>
        {text}
      </Text>
    </Tag>
  )
}

export default MissionCompletenessForStatsTag
