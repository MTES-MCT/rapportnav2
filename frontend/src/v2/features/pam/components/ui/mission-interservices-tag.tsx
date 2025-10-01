import { Tag, THEME } from '@mtes-mct/monitor-ui'
import React from 'react'
import Text from '@common/components/ui/text.tsx'

interface MissionInterServicesTagProps {}

const MissionInterServicesTag: React.FC<MissionInterServicesTagProps> = () => {
  return (
    <Tag
      backgroundColor={THEME.color.white}
      borderColor={THEME.color.slateGray}
      color={THEME.color.slateGray}
      style={{ height: 'auto' }}
    >
      <Text as={'h3'} weight="medium" color={THEME.color.slateGray}>
        Inter-services
      </Text>
    </Tag>
  )
}

export default MissionInterServicesTag
