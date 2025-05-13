import { FieldArrayRenderProps } from 'formik'
import React, { useState } from 'react'
import { Stack } from 'rsuite'
import { MissionCrewMember } from '../../../../common/types/crew-type.ts'
import Text, { TextProps } from '@common/components/ui/text.tsx'
import styled from 'styled-components'
import MissionCrewListItemPam from '../../ui/mission-crew-list-item-pam.tsx'
import MissionGeneralInformationCrewPamForm from './mission-general-information-crew-pam-form.tsx'
import { AddOrUpdateMissionCrewInput } from '@features/pam/mission/hooks/use-add-update-mission-crew.tsx'
import { MissionCrew } from '@common/types/crew-types.ts'
import {
  MissionCrewAddMemberButton,
  MissionCrewListItemStyled,
  MissionCrewListStyled,
  MissionCrewStack,
  MissionCrewTitleLabel,
  MissionCrewUnderlineStack
} from '../../../../mission-general-infos/ui/mission-crew-list.tsx'

interface MissionGeneralInformationCrewPamProps {
  name: string
  missionId?: number
  currentCrewList: MissionCrew[]
  fieldArray: FieldArrayRenderProps
}

const EmptyCrewListText = styled((props: Omit<TextProps, 'as'> & { isMissionFinished: boolean }) => (
  <Text {...props} as="h3" />
))(({ theme, isMissionFinished }) => ({
  paddingLeft: 8,
  fontStyle: 'italic',
  color: isMissionFinished ? theme.color.maximumRed : theme.color.charcoal
}))

const MissionGeneralInformationCrewPam: React.FC<MissionGeneralInformationCrewPamProps> = ({
  name,
  currentCrewList,
  missionId,
  fieldArray
}) => {
  const [selectedCrewMemberId, setSelectedCrewMemberId] = useState<number | undefined>(undefined)

  const handleUpdate = (agent: Omit<AddOrUpdateMissionCrewInput, 'missionId'>) => {
    if (!agent.id) {
      // Agent has no id, so we append it to the currentCrewList.
      fieldArray.form.setFieldValue(name, [...currentCrewList, { ...agent, missionId }])
    } else {
      // Find the index of the crew member in the currentCrewList by matching id.
      const index = currentCrewList.findIndex(crew => crew.id === agent.id)
      if (index !== -1) {
        // Replace the matching entry with the updated data.
        const updatedCrewList = [
          ...currentCrewList.slice(0, index),
          { ...agent, missionId },
          ...currentCrewList.slice(index + 1)
        ]
        fieldArray.form.setFieldValue(name, updatedCrewList)
      } else {
        // Optionally, if the crew member is not found, we can append it.
        fieldArray.form.setFieldValue(name, [...currentCrewList, { ...agent, missionId }])
      }
    }

    setSelectedCrewMemberId(undefined)
  }

  const handleDelete = (index: number) => fieldArray.remove(index)

  return (
    <>
      <MissionCrewUnderlineStack>
        <MissionCrewTitleLabel>Équipage</MissionCrewTitleLabel>
      </MissionCrewUnderlineStack>

      {fieldArray.form.values.crew?.length === 0 && (
        <Stack direction={'row'} style={{ width: '100%', marginBottom: '1rem' }}>
          <Stack.Item>
            {/*TODO */}
            <EmptyCrewListText isMissionFinished={false}>
              Sélectionner votre bordée, pour importer votre liste d'équipage
            </EmptyCrewListText>
          </Stack.Item>
        </Stack>
      )}

      <MissionCrewStack>
        <Stack.Item style={{ width: '100%' }}>
          <MissionCrewListStyled>
            {fieldArray.form.values.crew?.map((crewMember: MissionCrewMember, index: number) => (
              <MissionCrewListItemStyled
                index={index}
                key={`${crewMember.agent.id}-index`}
                length={fieldArray.form.values.crew.length}
              >
                <MissionCrewListItemPam
                  name={name}
                  index={index}
                  crewMember={crewMember}
                  handleDelete={handleDelete}
                  handleEdit={() => setSelectedCrewMemberId(crewMember.id)}
                />
              </MissionCrewListItemStyled>
            ))}
          </MissionCrewListStyled>
        </Stack.Item>
        <Stack.Item style={{ width: '100%', marginTop: 16 }}>
          <MissionCrewAddMemberButton onClick={() => setSelectedCrewMemberId(0)}>
            Ajouter un membre d'équipage
          </MissionCrewAddMemberButton>
        </Stack.Item>
        <>
          {selectedCrewMemberId !== undefined && (
            <MissionGeneralInformationCrewPamForm
              data-testid="crew-form"
              handleClose={() => setSelectedCrewMemberId(undefined)}
              crewId={selectedCrewMemberId}
              crewList={fieldArray.form.values.crew}
              handleSubmitForm={async (crew: Omit<AddOrUpdateMissionCrewInput, 'missionId'>) => {
                setSelectedCrewMemberId(undefined)
                handleUpdate(crew)
              }}
            />
          )}
        </>
      </MissionCrewStack>
    </>
  )
}

export default MissionGeneralInformationCrewPam
