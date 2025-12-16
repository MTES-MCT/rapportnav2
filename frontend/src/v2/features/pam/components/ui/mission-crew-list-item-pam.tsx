import { FlexboxGrid, Stack } from 'rsuite'
import { MissionCrew, MissionCrewAbsence, MissionCrewAbsenceType } from '../../../common/types/crew-type.ts'
import React from 'react'
import Text from '@common/components/ui/text.tsx'
import {
  CommentIcon,
  MissionCrewEditIconButton,
  MissionCrewMemberText,
  TruncateCommentText
} from '../../../mission-general-infos/ui/mission-crew-list.tsx'
import { Checkbox, Icon } from '@mtes-mct/monitor-ui'

interface CrewListProps {
  index: number
  name: string
  crewMember: MissionCrew
  handleEdit: (index: number, absenceType: MissionCrewAbsenceType) => void
  isAbsentFullMission?: boolean
  onToggleCheckbox?: (index: number, isChecked: boolean) => void
}

const MissionCrewListItemPam: React.FC<CrewListProps> = (props: CrewListProps) => {
  return props.crewMember.agent ? <StandardCrewMember {...props} /> : <InvitedCrewMember {...props} />
}

const StandardCrewMember: React.FC<CrewListProps> = ({
  index,
  name,
  crewMember,
  handleEdit,
  isAbsentFullMission,
  onToggleCheckbox
}) => {
  return (
    <FlexboxGrid key={`${name}-${index}`} align={'middle'}>
      <FlexboxGrid.Item colspan={1}>
        <Checkbox
          checked={!isAbsentFullMission}
          onClick={() => {
            debugger
            onToggleCheckbox?.(index, isAbsentFullMission)
          }}
        ></Checkbox>
      </FlexboxGrid.Item>
      <FlexboxGrid.Item colspan={7}>{<MissionCrewMemberText agent={crewMember?.agent} />}</FlexboxGrid.Item>

      <FlexboxGrid.Item colspan={7}>
        {
          <Text as="h3" truncate={true} data-testid="crew-member-role">
            {crewMember?.role?.title}
          </Text>
        }
      </FlexboxGrid.Item>
      <FlexboxGrid.Item colspan={8}>
        <FlexboxGrid align={'middle'} justify="start">
          <FlexboxGrid.Item colspan={20}>
            <TruncateCommentText>
              {isAbsentFullMission ? 'full time' : crewMember.absences.length ? 'temp' : ''}
            </TruncateCommentText>
          </FlexboxGrid.Item>
        </FlexboxGrid>
      </FlexboxGrid.Item>
      <FlexboxGrid.Item colspan={1}>
        <Stack direction="row">
          <Stack.Item>
            <MissionCrewEditIconButton
              onClick={() => {
                handleEdit(
                  index,
                  isAbsentFullMission ? MissionCrewAbsenceType.FULL_MISSION : MissionCrewAbsenceType.TEMPORARY
                )
              }}
            />
          </Stack.Item>
        </Stack>
      </FlexboxGrid.Item>
    </FlexboxGrid>
  )
}

const InvitedCrewMember: React.FC<CrewListProps> = ({ index, name, crewMember, handleEdit }) => {
  return (
    <FlexboxGrid key={`${name}-${index}`} align={'middle'}>
      <FlexboxGrid.Item colspan={1}>
        <Icon.Account />
      </FlexboxGrid.Item>
      <FlexboxGrid.Item colspan={7}>
        {<MissionCrewMemberText agent={crewMember?.agent} fullName={crewMember.fullName} />}
      </FlexboxGrid.Item>
      <FlexboxGrid.Item colspan={7}>
        {
          <Text as="h3" truncate={true} data-testid="crew-member-role">
            {crewMember?.role?.title}
          </Text>
        }
      </FlexboxGrid.Item>
      <FlexboxGrid.Item colspan={6}>
        <FlexboxGrid align={'middle'} justify="start">
          <FlexboxGrid.Item colspan={4}>
            <CommentIcon comment={crewMember.comment} />
          </FlexboxGrid.Item>
          <FlexboxGrid.Item colspan={16}>
            <TruncateCommentText>{crewMember.comment}</TruncateCommentText>
          </FlexboxGrid.Item>
        </FlexboxGrid>
      </FlexboxGrid.Item>
      <FlexboxGrid.Item colspan={3}>
        <Stack direction="row">
          <Stack.Item>
            <MissionCrewEditIconButton
              onClick={() => {
                debugger
                handleEdit(index, MissionCrewAbsenceType.FULL_MISSION)
              }}
            />
          </Stack.Item>
        </Stack>
      </FlexboxGrid.Item>
    </FlexboxGrid>
  )
}

export default MissionCrewListItemPam
