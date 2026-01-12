import Text from '@common/components/ui/text'
import { Accent, Tag } from '@mtes-mct/monitor-ui'
import React from 'react'
import { Stack } from 'rsuite'

interface MissionNatinfTagFishProps {
  natinf?: number
  description?: string
}

const MissionNatinfTagFish: React.FC<MissionNatinfTagFishProps> = ({ natinf, description }) => {
  const text = !natinf ? 'Sans infraction' : `NATINF : ${natinf}`
  return (
    <Stack direction="row" spacing={'0.5rem'}>
      <Stack.Item>
        <Tag accent={Accent.PRIMARY}>
          <Text as="h3" weight="medium">
            {description ? `${description} / ${text}` : `${text}`}
          </Text>
        </Tag>
      </Stack.Item>
    </Stack>
  )
}

export default MissionNatinfTagFish
