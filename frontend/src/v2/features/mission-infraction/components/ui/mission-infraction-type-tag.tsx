import Text from '@common/components/ui/text.tsx'
import { InfractionTypeEnum } from '@common/types/env-mission-types.ts'
import { InfractionType } from '@common/types/fish-mission-types.ts'
import { Accent, Tag } from '@mtes-mct/monitor-ui'
import React from 'react'
import { useInfraction } from '../../hooks/use-infraction.tsx'

interface MissionInfractionTagProps {
  type?: InfractionTypeEnum | InfractionType
}

const MissionInfractionTypeTag: React.FC<MissionInfractionTagProps> = ({ type }) => {
  const { getInfractionTypeTag } = useInfraction()
  return (
    <Tag accent={Accent.PRIMARY}>
      <Text as="h3" weight="medium">
        {getInfractionTypeTag(type)}
      </Text>
    </Tag>
  )
}

export default MissionInfractionTypeTag
