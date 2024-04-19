import { FC } from 'react'
import { Icon, Tag, THEME } from '@mtes-mct/monitor-ui'
import { MissionReportStatusEnum, MissionStatusEnum } from '../../types/mission-types.ts'
import Text from '../../ui/text.tsx'

interface MissionStatusTagProps {
  missionStatus?: MissionStatusEnum
  reportStatus?: MissionReportStatusEnum
}

const MissionReportStatusTag: FC<MissionStatusTagProps> = ({ missionStatus, reportStatus }) => {
  let color, IconComponent, text
  
  if (reportStatus && missionStatus === MissionStatusEnum.IN_PROGRESS) {
    if (reportStatus === MissionReportStatusEnum.COMPLETE) {
      color = THEME.color.mediumSeaGreen
      IconComponent = Icon.Confirm
      text = 'Données à jour'
    } else {
      color = THEME.color.charcoal
      IconComponent = Icon.AttentionFilled
      text = 'À compléter'
    }
  } else if (reportStatus && missionStatus === MissionStatusEnum.ENDED) {
    if (reportStatus === MissionReportStatusEnum.COMPLETE) {
      color = THEME.color.mediumSeaGreen
      IconComponent = Icon.Confirm
      text = 'Complété'
    } else {
      color = THEME.color.maximumRed
      IconComponent = Icon.AttentionFilled
      text = 'À compléter'
    }
  } else {
    color = THEME.color.copperRed
    IconComponent = Icon.Close
    text = 'Indisponible'
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

export default MissionReportStatusTag
