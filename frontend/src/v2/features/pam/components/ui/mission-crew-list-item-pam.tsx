import { FlexboxGrid, Stack } from 'rsuite'
import {
  CommentIcon,
  MissionCrewDeleteIconButton,
  MissionCrewEditIconButton,
  MissionCrewMemberText,
  TruncateCommentText
} from '../../../common/components/ui/mission-crew-list.tsx'
import { MissionCrewMember } from '../../../common/types/crew-type.ts'
import React from 'react'
import Text from '@common/components/ui/text.tsx'

interface CrewListProps {
  index: number
  name: string
  crewMember: MissionCrewMember
  handleEdit: (index: number) => void
  handleDelete: (index: number) => void
}

const MissionCrewListItemPam: React.FC<CrewListProps> = ({ index, name, crewMember, handleEdit, handleDelete }) => {
  return (
    <FlexboxGrid key={`${name}-${index}`} align={'middle'}>
      <FlexboxGrid.Item colspan={7}>{<MissionCrewMemberText agent={crewMember?.agent} />}</FlexboxGrid.Item>
      <FlexboxGrid.Item colspan={7}>
        {
          <Text as="h3" truncate={true} data-testid="crew-member-role">
            {crewMember?.role?.title}
          </Text>
        }
      </FlexboxGrid.Item>
      <FlexboxGrid.Item colspan={7}>
        <FlexboxGrid align={'middle'} justify="start">
          <FlexboxGrid.Item colspan={4}>
            <CommentIcon comment={crewMember.comment} />
          </FlexboxGrid.Item>
          <FlexboxGrid.Item colspan={20}>
            <TruncateCommentText>{crewMember.comment}</TruncateCommentText>
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
