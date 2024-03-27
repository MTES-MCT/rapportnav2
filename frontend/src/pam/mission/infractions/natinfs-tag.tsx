import React from 'react'
import { Accent, Tag } from '@mtes-mct/monitor-ui'
import Text from '../../../ui/text.tsx'

interface InfractionTagProps {
  natinfs?: String[]
}

const NatinfsTag: React.FC<InfractionTagProps> = ({ natinfs }) => {
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

export default NatinfsTag
