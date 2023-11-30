import React from 'react'
import { Accent, Button, Checkbox, Icon, Label, MultiRadio, Size, THEME } from '@mtes-mct/monitor-ui'
import { FishAction, SpeciesControl } from '../../../../types/fish-mission-types'
import Text from '../../../../ui/text'
import { Stack } from 'rsuite'
import { BOOLEAN_AS_OPTIONS, controlCheckMultiradioOptions } from '../action-control-fish'

interface FishControlOtherObservationsSectionProps {
  action: FishAction
}

const FishControlOtherObservationsSection: React.FC<FishControlOtherObservationsSectionProps> = ({ action }) => {
  return (
    <Stack direction="column" alignItems="flex-start" spacing={'0.2rem'}>
      <Stack.Item>
        <Label>Autres observations</Label>
      </Stack.Item>

      <Stack.Item style={{ backgroundColor: THEME.color.white, width: '100%', padding: '1rem' }}>
        <Text as="h3" weight="medium">
          {!!action?.otherComments ? action.otherComments : 'Aucune observation'}
        </Text>
      </Stack.Item>
    </Stack>
  )
}

export default FishControlOtherObservationsSection
