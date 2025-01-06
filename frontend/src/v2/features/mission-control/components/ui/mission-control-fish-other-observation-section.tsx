import Text from '@common/components/ui/text'
import { Label, THEME } from '@mtes-mct/monitor-ui'
import { isEmpty } from 'lodash'
import React from 'react'
import { Stack } from 'rsuite'
import { MissionFishActionData } from '../../../common/types/mission-action'

interface MissionControlFishOtherObservationsSectionProps {
  action: MissionFishActionData
}

const MissionControlFishOtherObservationsSection: React.FC<MissionControlFishOtherObservationsSectionProps> = ({
  action
}) => {
  return (
    <Stack direction="column" alignItems="flex-start" spacing={'0.2rem'}>
      <Stack.Item>
        <Label>Autres observations</Label>
      </Stack.Item>
      <Stack.Item style={{ backgroundColor: THEME.color.white, width: '100%', padding: '1rem' }}>
        <Text as="h3" weight="medium">
          {!isEmpty(action.otherComments) ? action.otherComments : 'Aucune observation'}
        </Text>
      </Stack.Item>
    </Stack>
  )
}

export default MissionControlFishOtherObservationsSection
