import { useMissionStatus } from '@common/hooks/use-mission-status'
import { MissionStatusEnum } from '@common/types/mission-types.ts'
import { Tag, THEME } from '@mtes-mct/monitor-ui'
import { FC } from 'react'
import Text from '../../ui/text'

interface MissionStatusTagProps {
  status?: MissionStatusEnum
}

const MissionStatusTag: FC<MissionStatusTagProps> = ({ status }) => {
  const statusComponent = useMissionStatus(status || MissionStatusEnum.UNAVAILABLE)
  return (
    <Tag
      withCircleIcon={true}
      Icon={statusComponent.icon}
      color={THEME.color.charcoal}
      iconColor={statusComponent.color}
      backgroundColor={THEME.color.cultured}
    >
      <Text as="h3" weight="medium" color={THEME.color.charcoal}>
        {statusComponent.text}
      </Text>
    </Tag>
  )
}

export default MissionStatusTag
