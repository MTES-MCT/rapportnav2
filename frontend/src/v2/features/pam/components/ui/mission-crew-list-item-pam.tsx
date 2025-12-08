import { FlexboxGrid, Stack } from 'rsuite'
import { MissionCrew, MissionCrewAbsence, MissionCrewAbsenceType } from '../../../common/types/crew-type.ts'
import React from 'react'
import Text from '@common/components/ui/text.tsx'
import {
  CommentIcon,
  MissionCrewDeleteIconButton,
  MissionCrewEditIconButton,
  MissionCrewMemberText,
  TruncateCommentText
} from '../../../mission-general-infos/ui/mission-crew-list.tsx'
import { Accent, Checkbox, Icon, IconButton, Size } from '@mtes-mct/monitor-ui'
import CrewAbsenceTag from './crew-absence-tag.tsx'

interface CrewListProps {
  index: number
  name: string
  crewMember: MissionCrew
  handleEditAbsence: (index: number, absenceType: MissionCrewAbsenceType) => void
  handleEditCrew: (index: number) => void
  isAbsentFullMission?: boolean
  onToggleCheckbox?: (index: number, isChecked: boolean) => void
  handleDelete: (index: number) => void
}

const MissionCrewListItemPam: React.FC<CrewListProps> = (props: CrewListProps) => {
  return props.crewMember.agent ? <StandardCrewMember {...props} /> : <InvitedCrewMember {...props} />
}

const StandardCrewMember: React.FC<CrewListProps> = ({
  index,
  name,
  crewMember,
  handleEditAbsence,
  isAbsentFullMission,
  onToggleCheckbox
}) => {
  return (
    <>
      <FlexboxGrid key={`${name}-${index}`} align={'middle'}>
        <FlexboxGrid.Item colspan={1}>
          <Checkbox
            checked={!isAbsentFullMission}
            onClick={() => {
              onToggleCheckbox?.(index, isAbsentFullMission)
            }}
            style={{ marginTop: '-10px' }}
          ></Checkbox>
        </FlexboxGrid.Item>
        <FlexboxGrid.Item colspan={7}>{<MissionCrewMemberText agent={crewMember?.agent} />}</FlexboxGrid.Item>

        <FlexboxGrid.Item colspan={6}>
          {
            <Text as="h3" truncate={true} data-testid="crew-member-role">
              {crewMember?.role?.title}
            </Text>
          }
        </FlexboxGrid.Item>
        <FlexboxGrid.Item colspan={9}>
          <FlexboxGrid align={'middle'} justify="start">
            <FlexboxGrid.Item colspan={20}>
              {crewMember?.absences?.length && crewMember?.absences?.length < 2
                ? crewMember?.absences?.map((absence: MissionCrewAbsence) => (
                    <CrewAbsenceTag key={absence.id} absence={absence} />
                  ))
                : undefined}
            </FlexboxGrid.Item>
          </FlexboxGrid>
        </FlexboxGrid.Item>
        <FlexboxGrid.Item colspan={1}>
          <Stack direction="row">
            <Stack.Item>
              <IconButton
                role="edit-crew"
                size={Size.NORMAL}
                accent={Accent.TERTIARY}
                Icon={isAbsentFullMission && MissionCrewAbsenceType.FULL_MISSION ? Icon.EditUnbordered : Icon.Calendar}
                title={
                  isAbsentFullMission && MissionCrewAbsenceType.FULL_MISSION
                    ? 'Modifier une non-participation'
                    : 'Manager les absences temporaires'
                }
                data-testid="edit-crew-member-icon"
                onClick={() => {
                  handleEditAbsence(
                    index,
                    isAbsentFullMission ? MissionCrewAbsenceType.FULL_MISSION : MissionCrewAbsenceType.TEMPORARY
                  )
                }}
              />
            </Stack.Item>
          </Stack>
        </FlexboxGrid.Item>

        {crewMember?.absences?.length > 0 && crewMember?.absences?.length > 1 && (
          <FlexboxGrid key={`${name}-${index}`} align={'middle'} style={{ width: '100%', marginTop: '5px' }}>
            <FlexboxGrid.Item colspan={1}></FlexboxGrid.Item>
            <FlexboxGrid.Item colspan={22}>
              <Stack direction="row" style={{ width: '100%' }} spacing={'1rem'}>
                {crewMember?.absences?.map((absence: MissionCrewAbsence) => (
                  <CrewAbsenceTag key={absence.id} absence={absence} />
                ))}
              </Stack>
            </FlexboxGrid.Item>
          </FlexboxGrid>
        )}
      </FlexboxGrid>
    </>
  )
}

const InvitedCrewMember: React.FC<CrewListProps> = ({ index, name, crewMember, handleEditCrew, handleDelete }) => {
  return (
    <FlexboxGrid key={`${name}-${index}`} align={'middle'}>
      <FlexboxGrid.Item colspan={1}>
        <Icon.Account />
      </FlexboxGrid.Item>
      <FlexboxGrid.Item colspan={7}>
        {<MissionCrewMemberText agent={crewMember?.agent} fullName={crewMember.fullName} />}
      </FlexboxGrid.Item>
      <FlexboxGrid.Item colspan={6}>
        {
          <Text as="h3" truncate={true} data-testid="crew-member-role">
            {crewMember?.role?.title}
          </Text>
        }
      </FlexboxGrid.Item>
      <FlexboxGrid.Item colspan={7}>
        <FlexboxGrid align={'middle'} justify="start">
          <FlexboxGrid.Item colspan={3} style={{ marginLeft: '4px' }}>
            <CommentIcon comment={crewMember.comment} />
          </FlexboxGrid.Item>
          <FlexboxGrid.Item colspan={18}>
            <TruncateCommentText>{crewMember.comment}</TruncateCommentText>
          </FlexboxGrid.Item>
        </FlexboxGrid>
      </FlexboxGrid.Item>
      <FlexboxGrid.Item colspan={3}>
        <Stack direction="row" justifyContent={'flex-end'} style={{ marginRight: '-4px' }}>
          <Stack.Item>
            <MissionCrewEditIconButton onClick={() => handleEditCrew(index)} />
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
