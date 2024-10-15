import { useMissionCompletenessForStats } from '@common/hooks/use-mission-completeness-for-stats.tsx'
import { CompletenessForStatsStatusEnum, MissionStatusEnum } from '@common/types/mission-types.ts'
import { Tag, THEME } from '@mtes-mct/monitor-ui'
import { FC } from 'react'
import Text from '../../ui/text.tsx'

interface MissionStatusTagProps {
  missionStatus?: MissionStatusEnum
  completenessForStats?: CompletenessForStatsStatusEnum
}

const MissionCompletenessForStatsTag: FC<MissionStatusTagProps> = ({ missionStatus, completenessForStats }) => {
  const { getCompletenessForStats } = useMissionCompletenessForStats(missionStatus, completenessForStats)
  const forStats = getCompletenessForStats()

  return (
    <Tag
      iconColor={forStats.color}
      backgroundColor={THEME.color.cultured}
      color={forStats.color}
      Icon={forStats.icon}
      withCircleIcon={true}
    >
      <Text as="h3" weight="medium" color={forStats.color}>
        {forStats.text}
      </Text>
    </Tag>
  )
}

export default MissionCompletenessForStatsTag
