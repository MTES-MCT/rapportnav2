import Text from '@common/components/ui/text'
import { Accent, Tag } from '@mtes-mct/monitor-ui'
import React from 'react'

interface MissionNatinfTagFishProps {
  natinf?: number
}

const MissionNatinfTagFish: React.FC<MissionNatinfTagFishProps> = ({ natinf }) => {
  const text = !natinf ? 'Sans infraction' : `NATINF : ${natinf}`
  return (
    <Tag accent={Accent.PRIMARY}>
      <Text as="h3" weight="medium">
        {text}
      </Text>
    </Tag>
  )
}

export default MissionNatinfTagFish
