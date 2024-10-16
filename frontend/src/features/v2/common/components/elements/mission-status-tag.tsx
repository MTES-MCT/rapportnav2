import { MissionStatusEnum } from '@common/types/mission-types.ts'
import { useMissionStatus } from '@features/v2/common/hooks/use-mission-status'
import { Tag, THEME } from '@mtes-mct/monitor-ui'
import { FC } from 'react'
import Text from '../../../../common/components/ui/text'

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
