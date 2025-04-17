import Text from '@common/components/ui/text'
import { InfractionTypeEnum } from '@common/types/env-mission-types'
import { THEME } from '@mtes-mct/monitor-ui'
import React from 'react'
import { Stack } from 'rsuite'
import MissionNatinfTag from '../../../common/components/ui/mission-natinfs-tag'
import MissionInfractionTypeTag from '../../../mission-infraction/components/ui/mission-infraction-type-tag'

interface MissionTargetEnvSummaryProps {
  natinfs?: string[]
  infractionType?: InfractionTypeEnum
}

const MissionTargetEnvSummary: React.FC<MissionTargetEnvSummaryProps> = ({ natinfs, infractionType }) => {
  return (
    <Stack direction="row" spacing={'0.5rem'} justifyContent="flex-start">
      <Stack direction="row" style={{ width: '100%' }}>
        <Stack.Item style={{ width: '50%' }}>
          <Text as="h3" weight="bold" color={THEME.color.gunMetal} truncate>
            Infraction contrôle de l’environnement
          </Text>
        </Stack.Item>
        <Stack.Item style={{ width: '50%' }}>
          <Stack direction="row" spacing={'0.5rem'} style={{ width: '100%' }}>
            <Stack.Item>
              <MissionInfractionTypeTag type={infractionType} />
            </Stack.Item>
            <Stack.Item>
              <MissionNatinfTag natinfs={natinfs} />
            </Stack.Item>
          </Stack>
        </Stack.Item>
      </Stack>
    </Stack>
  )
}

export default MissionTargetEnvSummary
