import { FieldArrayRenderProps } from 'formik'
import React, { useState } from 'react'
import { Stack } from 'rsuite'
import Text from '@common/components/ui/text.tsx'
import {
  MissionCrewAddMemberButton,
  MissionCrewCollapsibleTitle,
  MissionCrewListStyled,
  MissionCrewStack,
  MissionCrewTitleLabel,
  MissionCrewUnderlineStack
} from '../../../../../mission-general-infos/ui/mission-crew-list.tsx'
import { MissionPassenger } from '../../../../../common/types/passenger-type.ts'
import MissionGeneralInformationPassengerPamForm from './mission-general-information-passenger-pam-form.tsx'
import { Icon, THEME } from '@mtes-mct/monitor-ui'
import MissionPassengerListItemPam from '../../../ui/mission-passenger-list-item-pam.tsx'

interface MissionGeneralInformationCrewPamProps {
  name: string
  missionId?: number
  currentPassengerList: MissionPassenger[]
  fieldArray: FieldArrayRenderProps
}

const MissionGeneralInformationCrewPam: React.FC<MissionGeneralInformationCrewPamProps> = ({
  name,
  currentPassengerList,
  missionId,
  fieldArray
}) => {
  const [selectedPassengerId, setSelectedPassengerId] = useState<number | undefined>(undefined)
  const [isExpanded, setIsExpanded] = useState(true)

  const handleUpdate = (passenger: Omit<MissionPassenger, 'missionId'>) => {
    if (!passenger.id) {
      // Agent has no id, so we append it to the currentPassengerList.
      fieldArray.form.setFieldValue(name, [...currentPassengerList, { ...passenger, missionId }])
    } else {
      // Find the index of the crew member in the currentPassengerList by matching id.
      const index = currentPassengerList.findIndex(crew => crew.id === passenger.id)
      if (index !== -1) {
        // Replace the matching entry with the updated data.
        const updatedList = [
          ...currentPassengerList.slice(0, index),
          { ...passenger, missionId },
          ...currentPassengerList.slice(index + 1)
        ]
        fieldArray.form.setFieldValue(name, updatedList)
      } else {
        // Optionally, if the crew member is not found, we can append it.
        fieldArray.form.setFieldValue(name, [...currentPassengerList, { ...passenger, missionId }])
      }
    }

    setSelectedPassengerId(undefined)
  }

  const handleDelete = (index: number) => {
    fieldArray.remove(index)
    setSelectedPassengerId(undefined)
  }

  return (
    <>
      <MissionCrewUnderlineStack>
        <MissionCrewCollapsibleTitle onClick={() => setIsExpanded(!isExpanded)}>
          <Icon.Chevron
            style={{
              transform: isExpanded ? 'rotate(270deg)' : 'rotate(0deg)',
              transition: 'transform 0.2s ease'
            }}
          />
          <MissionCrewTitleLabel>Passagers</MissionCrewTitleLabel>
        </MissionCrewCollapsibleTitle>
      </MissionCrewUnderlineStack>

      {isExpanded && (
        <>
          {!fieldArray.form.values.passengers?.length && (
            <Stack direction={'row'} style={{ width: '100%' }}>
              <Stack.Item>
                <Text as={'h3'} fontStyle={'italic'} color={THEME.color.charcoal}>
                  Pas de passagers
                </Text>
              </Stack.Item>
            </Stack>
          )}

          <MissionCrewStack>
            <Stack.Item style={{ width: '100%' }}>
              <MissionCrewListStyled>
                {fieldArray.form.values.passengers?.map((passenger: MissionPassenger, index: number) => (
                  <div
                    key={passenger.id}
                    style={{
                      borderBottom:
                        index === (fieldArray.form.values.passengers ?? [])?.length - 1
                          ? 'none'
                          : `1px solid ${THEME.color.lightGray}`
                    }}
                  >
                    <MissionPassengerListItemPam
                      name={name}
                      index={index}
                      passenger={passenger}
                      handleDelete={handleDelete}
                      handleEdit={() => setSelectedPassengerId(index)}
                    />
                  </div>
                ))}
              </MissionCrewListStyled>
            </Stack.Item>
            <Stack.Item style={{ width: '100%', marginTop: 16 }}>
              <MissionCrewAddMemberButton onClick={() => setSelectedPassengerId(-1)}>
                Ajouter un passager
              </MissionCrewAddMemberButton>
            </Stack.Item>
          </MissionCrewStack>
        </>
      )}

      {selectedPassengerId !== undefined && (
        <MissionGeneralInformationPassengerPamForm
          data-testid="passenger-form"
          handleClose={() => setSelectedPassengerId(undefined)}
          passenger={selectedPassengerId === -1 ? undefined : fieldArray.form.values.passengers[selectedPassengerId]}
          handleSubmitForm={async (passenger: MissionPassenger) => {
            setSelectedPassengerId(undefined)
            handleUpdate(passenger)
          }}
        />
      )}
    </>
  )
}

export default MissionGeneralInformationCrewPam
