import { FlexboxGrid, Stack } from 'rsuite'
import { MissionCrew } from '../../common/types/crew-type.ts'
import { MissionCrewDeleteIconButton, MissionCrewEditIconButton, MissionCrewMemberText } from './mission-crew-list.tsx'

interface MissionCrewListNoCommentProps {
  index: number
  name: string
  crewMember: MissionCrew
  handleEdit: (index: number) => void
  handleDelete: (index: number) => void
}

const MissionCrewListNoComment: React.FC<MissionCrewListNoCommentProps> = ({
  index,
  name,
  crewMember,
  handleEdit,
  handleDelete
}) => {
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

export default MissionCrewListNoComment
