import { FishAction } from '@common/types/fish-mission-types.ts'
import { Label } from '@mtes-mct/monitor-ui'
import React from 'react'
import { Stack } from 'rsuite'

interface MissionControlFishOtherSectionProps {
  action: FishAction
}

const MissionControlFishOtherInfractionsSection: React.FC<MissionControlFishOtherSectionProps> = ({ action }) => {
  return (
    <Stack direction="column" alignItems="flex-start" spacing={'0.2rem'}>
      <Stack.Item>
        <Label>Autres infractions</Label>
      </Stack.Item>
      <Stack.Item style={{ width: '100%' }}>INFRACTION</Stack.Item>
    </Stack>
  )
}

export default MissionControlFishOtherInfractionsSection

//<FishInfractionSummary title="Autres infractions" infractions={action.otherInfractions} />
