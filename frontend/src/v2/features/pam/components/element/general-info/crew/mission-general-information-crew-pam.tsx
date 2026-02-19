import { FieldArrayRenderProps } from 'formik'
import React, { useState } from 'react'
import { Stack } from 'rsuite'
import {
  AddOrUpdateMissionCrewInput,
  MissionCrew,
  MissionCrewAbsence,
  MissionCrewAbsenceType
} from '../../../../../common/types/crew-type.ts'
import Text, { TextProps } from '@common/components/ui/text.tsx'
import styled from 'styled-components'
import MissionCrewListItemPam from '../../../ui/mission-crew-list-item-pam.tsx'
import {
  MissionCrewAddMemberButton,
  MissionCrewListItemStyled,
  MissionCrewListStyled,
  MissionCrewStack,
  MissionCrewTitleLabel,
  MissionCrewUnderlineStack
} from '../../../../../mission-general-infos/ui/mission-crew-list.tsx'
import { useMissionFinished } from '../../../../../common/hooks/use-mission-finished.tsx'
import MissionGeneralInformationCrewPamAbsenceForm from './mission-general-information-crew-pam-absence-form.tsx'
import MissionGeneralInformationCrewPamForm from './mission-general-information-crew-pam-form.tsx'

interface MissionGeneralInformationCrewPamProps {
  name: string
  missionId?: number
  currentCrewList: MissionCrew[]
  fieldArray: FieldArrayRenderProps
}

const EmptyCrewListText = styled((props: Omit<TextProps, 'as'> & { isMissionFinished: boolean }) => {
  const { isMissionFinished, ...otherProps } = props
  return <Text {...otherProps} as="h3" />
})(({ theme, isMissionFinished }) => ({
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
  const isMissionFinished = useMissionFinished(missionId)

  const [showAdditionalCrewDialog, setShowAdditionalCrewDialog] = useState(false)
  const [selectedCrewIndex, setSelectedCrewIndex] = useState<number | undefined>(undefined)
  const [absenceMode, setAbsenceMode] = useState<MissionCrewAbsenceType | undefined>(undefined)

  const handleUpdate = (crew: Omit<AddOrUpdateMissionCrewInput, 'missionId'>) => {
    if (!crew.id) {
      // Agent has no id, so we append it to the currentCrewList.
      fieldArray.form.setFieldValue(name, [...currentCrewList, { ...crew, missionId }])
    } else {
      // Find the index of the crew member in the currentCrewList by matching id.
      const index = currentCrewList.findIndex(c => c.id === crew.id)
      if (index !== -1) {
        // Replace the matching entry with the updated data.
        const updatedCrewList = [
          ...currentCrewList.slice(0, index),
          { ...crew, missionId },
          ...currentCrewList.slice(index + 1)
        ]
        fieldArray.form.setFieldValue(name, updatedCrewList)
      } else {
        // Optionally, if the crew member is not found, we can append it.
        fieldArray.form.setFieldValue(name, [...currentCrewList, { ...crew, missionId }])
      }
    }

    setSelectedCrewIndex(undefined)
  }

  const handleToggleCheckbox = (index: number, isChecked: boolean) => {
    if (isChecked) {
      fieldArray.form.setFieldValue(`${name}.${index}.absences`, [])
    } else {
      // if crew member has some temporary absences, wipe them out before setting full mission absence
      if ((fieldArray.form.values.crew[index].absences ?? []).length > 0) {
        fieldArray.form.setFieldValue(`${name}.${index}.absences`, [])
      }
      setSelectedCrewIndex(index)
      setAbsenceMode(MissionCrewAbsenceType.FULL_MISSION)
    }
  }

  const handleDelete = (index: number) => fieldArray.remove(index)

  return (
    <>
      <MissionCrewUnderlineStack>
        <MissionCrewTitleLabel>&#10095; Équipage</MissionCrewTitleLabel>
      </MissionCrewUnderlineStack>

      {!fieldArray.form.values.crew?.length && (
        <Stack direction={'row'} style={{ width: '100%', marginBottom: '1rem' }}>
          <Stack.Item>
            <EmptyCrewListText isMissionFinished={isMissionFinished}>
              Sélectionner votre bordée, pour importer votre liste d'équipage
            </EmptyCrewListText>
          </Stack.Item>
        </Stack>
      )}

      <MissionCrewStack>
        <Stack.Item style={{ width: '100%' }}>
          <MissionCrewListStyled>
            {fieldArray.form.values.crew?.map((crewMember: MissionCrew, index: number) => (
              <MissionCrewListItemStyled
                index={index}
                key={`${crewMember?.agent?.id}-${index}`}
                length={fieldArray.form.values.crew.length}
              >
                <MissionCrewListItemPam
                  name={name}
                  index={index}
                  crewMember={crewMember}
                  isAbsentFullMission={(crewMember.absences ?? []).some(
                    (abs: MissionCrewAbsence) => abs.isAbsentFullMission
                  )}
                  handleEditAbsence={(_i: number, absenceType: MissionCrewAbsenceType) => {
                    setSelectedCrewIndex(index)
                    setAbsenceMode(absenceType)
                  }}
                  handleEditCrew={() => {
                    setSelectedCrewIndex(index)
                    setShowAdditionalCrewDialog(true)
                  }}
                  onToggleCheckbox={handleToggleCheckbox}
                  handleDelete={handleDelete}
                />
              </MissionCrewListItemStyled>
            ))}
          </MissionCrewListStyled>
        </Stack.Item>
        <Stack.Item style={{ width: '100%', marginTop: 16 }}>
          <MissionCrewAddMemberButton
            onClick={() => {
              setShowAdditionalCrewDialog(true)
              setSelectedCrewIndex(undefined)
            }}
          >
            Ajouter un membre d'équipage
          </MissionCrewAddMemberButton>
        </Stack.Item>
        <>
          {selectedCrewIndex !== undefined && (
            <MissionGeneralInformationCrewPamAbsenceForm
              missionId={missionId}
              crewIndex={selectedCrewIndex}
              // data-testid="crew-absence-form"
              handleClose={() => {
                setSelectedCrewIndex(undefined)
                setAbsenceMode(undefined)
              }}
              absenceType={absenceMode}
            />
          )}
          {showAdditionalCrewDialog && (
            <MissionGeneralInformationCrewPamForm
              data-testid="crew-form"
              handleClose={() => {
                setShowAdditionalCrewDialog(false)
                setSelectedCrewIndex(undefined)
              }}
              crewIndex={selectedCrewIndex}
              crewList={fieldArray.form.values.crew}
              handleSubmitForm={async (crew: Omit<AddOrUpdateMissionCrewInput, 'missionId'>) => {
                setShowAdditionalCrewDialog(false)
                setSelectedCrewIndex(undefined)
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
