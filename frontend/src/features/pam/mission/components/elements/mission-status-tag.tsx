import { FC } from 'react'
import { Icon, Tag, THEME } from '@mtes-mct/monitor-ui'
import { MissionStatusEnum } from '../../../../common/types/mission-types.ts'
import Text from '../../../../common/components/ui/text.tsx'

interface MissionStatusTagProps {
  status?: MissionStatusEnum
}

const MissionStatusTag: FC<MissionStatusTagProps> = ({ status }) => {
  let iconColor, IconComponent, text

  switch (status) {
    case MissionStatusEnum.UPCOMING:
      iconColor = THEME.color.babyBlueEyes
      IconComponent = Icon.ClockDashed
      text = 'À venir'
      break
    case MissionStatusEnum.IN_PROGRESS:
      iconColor = THEME.color.blueGray
      IconComponent = Icon.Clock
      text = 'En cours'
      break
    case MissionStatusEnum.ENDED:
      iconColor = THEME.color.charcoal
      IconComponent = Icon.Confirm
      text = 'Terminée'
      break
    case MissionStatusEnum.UNAVAILABLE:
    default:
      iconColor = THEME.color.maximumRed
      IconComponent = Icon.Close
      text = 'Indisponible'
  }

  return (
    <Tag
      iconColor={iconColor}
      backgroundColor={THEME.color.cultured}
      color={THEME.color.charcoal}
      Icon={IconComponent}
      withCircleIcon={true}
    >
      <Text as="h3" weight="medium" color={THEME.color.charcoal}>
        {text}
      </Text>
    </Tag>
  )
}

export default MissionStatusTag
