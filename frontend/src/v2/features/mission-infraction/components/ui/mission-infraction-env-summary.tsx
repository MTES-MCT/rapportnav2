import { ControlType } from '@common/types/control-types'
import { InfractionTypeEnum } from '@common/types/env-mission-types'
import React from 'react'
import { Stack } from 'rsuite'
import MissionNatinfTag from '../../../common/components/ui/mission-natinfs-tag'
import MissionInfractionEnvTitle from './mission-infraction-env-title'
import MissionInfractionTypeTag from './mission-infraction-type-tag'

interface MissionInfractionEnvSummaryProps {
  natinfs?: string[]
  controlType?: ControlType
  infractionType?: InfractionTypeEnum
}

const MissionInfractionEnvSummary: React.FC<MissionInfractionEnvSummaryProps> = ({
  natinfs,
  controlType,
  infractionType
}) => {
  return (
    <Stack direction="row" spacing={'0.5rem'} style={{ width: '100%' }} justifyContent="flex-start">
      <Stack.Item>
        <MissionInfractionEnvTitle controlType={controlType} />
      </Stack.Item>
      <Stack.Item>
        <Stack direction="row" spacing={'0.5rem'} style={{ width: '100%', flexWrap: 'wrap' }}>
          <Stack.Item>
            <MissionInfractionTypeTag type={infractionType} />
          </Stack.Item>
          <Stack.Item>
            <MissionNatinfTag natinfs={natinfs} />
          </Stack.Item>
        </Stack>
      </Stack.Item>
    </Stack>
  )
}

export default MissionInfractionEnvSummary
