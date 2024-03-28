import React from 'react'
import { Label } from '@mtes-mct/monitor-ui'
import { FishAction } from '../../../../types/fish-mission-types'
import { Stack } from 'rsuite'
import FishInfractionSummary from '../../infractions/fish-infraction-summary.tsx'

interface FishControlOtherSectionProps {
  action: FishAction
}

const FishControlOtherInfractionsSection: React.FC<FishControlOtherSectionProps> = ({ action }) => {
  return (
    <Stack direction="column" alignItems="flex-start" spacing={'0.2rem'}>
      <Stack.Item>
        <Label>Autres infractions</Label>
      </Stack.Item>
      <Stack.Item style={{ width: '100%' }}>
        <FishInfractionSummary title="Autres infractions" infractions={action.otherInfractions} />
      </Stack.Item>
    </Stack>
  )
}

export default FishControlOtherInfractionsSection
