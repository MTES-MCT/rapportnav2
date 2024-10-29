import Text from '@common/components/ui/text'
import { Accent, Tag } from '@mtes-mct/monitor-ui'
import React from 'react'

interface MissionNatinfTagProps {
  natinfs?: String[]
}

const MissionNatinfTag: React.FC<MissionNatinfTagProps> = ({ natinfs }) => {
  const text = !natinfs?.length
    ? 'Sans infraction'
    : `${natinfs.length > 1 ? `${natinfs.length} ` : ''}NATINF : ${natinfs.join(', ')}`
  return (
    <Tag accent={Accent.PRIMARY}>
      <Text as="h3" weight="medium">
        {text}
      </Text>
    </Tag>
  )
}

export default MissionNatinfTag
