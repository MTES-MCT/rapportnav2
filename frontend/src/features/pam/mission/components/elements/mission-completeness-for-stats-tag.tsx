import { FC } from 'react'
import { Icon, Tag, THEME } from '@mtes-mct/monitor-ui'
import { CompletenessForStatsStatusEnum, MissionStatusEnum } from '../../../../common/types/mission-types.ts'
import Text from '../../../../common/components/ui/text.tsx'

interface MissionStatusTagProps {
  missionStatus?: MissionStatusEnum
  completenessForStats?: CompletenessForStatsStatusEnum
}

const MissionCompletenessForStatsTag: FC<MissionStatusTagProps> = ({ missionStatus, completenessForStats }) => {
  let color, IconComponent, text

  if (completenessForStats && missionStatus === MissionStatusEnum.ENDED) {
    if (completenessForStats === CompletenessForStatsStatusEnum.COMPLETE) {
      color = THEME.color.mediumSeaGreen
      IconComponent = Icon.Confirm
      text = 'Complété'
    } else {
      color = THEME.color.maximumRed
      IconComponent = Icon.AttentionFilled
      text = 'À compléter'
    }
  } else {
    if (completenessForStats === CompletenessForStatsStatusEnum.COMPLETE) {
      color = THEME.color.mediumSeaGreen
      IconComponent = Icon.Confirm
      text = 'Données à jour'
    } else {
      color = THEME.color.charcoal
      IconComponent = Icon.AttentionFilled
      text = 'À compléter'
    }
  }

  return (
    <Tag
      iconColor={color}
      backgroundColor={THEME.color.cultured}
      color={color}
      Icon={IconComponent}
      withCircleIcon={true}
    >
      <Text as="h3" weight="medium" color={color}>
        {text}
      </Text>
    </Tag>
  )
}

export default MissionCompletenessForStatsTag
