import { FlexboxGrid, Stack } from 'rsuite'
import React from 'react'
import Text from '@common/components/ui/text.tsx'
import {
  MissionCrewDeleteIconButton,
  MissionCrewEditIconButton
} from '../../../mission-general-infos/ui/mission-crew-list.tsx'
import { MissionPassenger, PASSENGER_OPTIONS } from '../../../common/types/passenger-type.ts'
import { THEME } from '@mtes-mct/monitor-ui'

interface CrewListProps {
  index: number
  name: string
  passenger: MissionPassenger
  handleEdit: (index: number) => void
  handleDelete: (index: number) => void
}

const MissionCrewListItemPam: React.FC<CrewListProps> = ({ index, name, passenger, handleEdit, handleDelete }) => {
  return (
    <FlexboxGrid key={`${name}-${index}`} align={'middle'}>
      <FlexboxGrid.Item colspan={7}>
        <Text as="h3" truncate={true} weight={'medium'} data-testid="passenger">
          {`${passenger?.fullName}`}
        </Text>
      </FlexboxGrid.Item>
      <FlexboxGrid.Item colspan={7}>
        {
          <Text as="h3" truncate={true} color={THEME.color.gunMetal} weight={'medium'} data-testid="passenger-role">
            {PASSENGER_OPTIONS.find((option: any) => option.value === passenger?.organization)?.label}
          </Text>
        }
      </FlexboxGrid.Item>
      <FlexboxGrid.Item colspan={7}>
        <FlexboxGrid align={'middle'} justify="start">
          <FlexboxGrid.Item colspan={4}>{/*<CommentIcon comment={crewMember.comment} />*/}</FlexboxGrid.Item>
          <FlexboxGrid.Item colspan={20}>
            <Text as="h3" truncate={true} color={THEME.color.charcoal} data-testid="passenger-intern">
              {passenger.isIntern ? 'Stagiaire' : ''}
            </Text>
          </FlexboxGrid.Item>
        </FlexboxGrid>
      </FlexboxGrid.Item>
      <FlexboxGrid.Item colspan={3}>
        <Stack direction="row">
          <Stack.Item>
            <MissionCrewEditIconButton onClick={() => handleEdit(index)} />
          </Stack.Item>
          <Stack.Item>
            <MissionCrewDeleteIconButton onClick={() => handleDelete(index)} />
          </Stack.Item>
        </Stack>
      </FlexboxGrid.Item>
    </FlexboxGrid>
  )
}

export default MissionCrewListItemPam
