import { FlexboxGrid, Stack } from 'rsuite'
import {
  MissionCrewDeleteIconButton,
  MissionCrewEditIconButton,
  MissionCrewMemberText
} from '../../../common/components/ui/mission-crew-list.tsx'
import { MissionCrewMember } from '../../../common/types/crew-type.ts'

interface CrewListProps {
  index: number
  name: string
  crewMember: MissionCrewMember
  handleEdit: (index: number) => void
  handleDelete: (index: number) => void
}

const MissionCrewListItemUlam: React.FC<CrewListProps> = ({ index, name, crewMember, handleEdit, handleDelete }) => {
  return (
    <FlexboxGrid key={`${name}-${index}`} align={'middle'}>
      <FlexboxGrid.Item colspan={18}>{<MissionCrewMemberText agent={crewMember?.agent} />}</FlexboxGrid.Item>
      <FlexboxGrid.Item colspan={6}>
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

export default MissionCrewListItemUlam
