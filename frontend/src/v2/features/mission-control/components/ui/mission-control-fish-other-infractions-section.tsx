import { Label } from '@mtes-mct/monitor-ui'
import React from 'react'
import { Stack } from 'rsuite'
import { MissionFishActionData } from '../../../common/types/mission-action'
import MissionInfractionFishSummary from '../../../mission-infraction/components/elements/mission-infraction-fish-summary'

interface MissionControlFishOtherSectionProps {
  action: MissionFishActionData
}

const MissionControlFishOtherInfractionsSection: React.FC<MissionControlFishOtherSectionProps> = ({ action }) => {
  return (
    <Stack direction="column" alignItems="flex-start" spacing={'0.2rem'}>
      <Stack.Item>
        <Label>Autres infractions</Label>
      </Stack.Item>
      <Stack.Item style={{ width: '100%' }}>
        <MissionInfractionFishSummary
          title="Autres infractions"
          isActionDisabled={true}
          infractions={action.otherInfractions ?? []}
        />
      </Stack.Item>
    </Stack>
  )
}

export default MissionControlFishOtherInfractionsSection
