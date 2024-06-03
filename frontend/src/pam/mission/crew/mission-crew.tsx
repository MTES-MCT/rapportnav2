import { Accent, Button, ButtonProps, Icon, Label, LabelProps, Size } from '@mtes-mct/monitor-ui'
import React, { useEffect, useState } from 'react'
import { useParams } from 'react-router-dom'
import { Stack, StackProps } from 'rsuite'
import { StackItemProps } from 'rsuite/esm/Stack/StackItem'
import styled from 'styled-components'
import { MissionCrew as MissionCrewModel } from '../../../types/crew-types'
import Text, { TextProps } from '../../../ui/text'
import useIsMissionFinished from '../use-is-mission-finished.tsx'
import MissionCrewForm from './mission-crew-form.tsx'
import CrewMemeberList from './mission-crew-member-list.tsx'
import useAddOrUpdateMissionCrew, { AddOrUpdateMissionCrewInput } from './use-add-update-mission-crew.tsx'
import useDeleteMissionCrew from './use-delete-mission-crew'
import useMissionCrew from './use-mission-crew'

const UnderlineTitleLabel = styled((props: LabelProps) => <Label {...props} />)(({ theme }) => ({
  fontWeight: 'bold',
  paddingBottom: 8,
  color: theme.color.gunMetal,
  borderBottom: `1px solid ${theme.color.charcoal}`
}))

const AddCrewMemberButton = styled((props: ButtonProps) => (
  <Button
    Icon={Icon.Plus}
    size={Size.NORMAL}
    isFullWidth={true}
    accent={Accent.SECONDARY}
    role="add-crew-member-button"
    data-testid="add-crew-member-button"
    {...props}
  />
))(({ theme }) => ({
  color: theme.color.charcoal
}))

const CrewStack = styled((props: StackProps) => (
  <Stack {...props} direction="column" alignItems="flex-start" spacing="0.25rem" />
))({
  width: '100%'
})

const CrewStackItem = styled((props: StackItemProps) => <Stack.Item {...props} />)({
  width: '100%'
})

const EmptyCrewListStackItem = styled((props: StackItemProps) => <CrewStackItem {...props} />)(({ theme }) => ({
  padding: '0.5rem',
  backgroundColor: theme.color.gainsboro
}))

const EmptyCrewListText = styled((props: Omit<TextProps, 'as'> & { isMissionFinished: boolean }) => (
  <Text {...props} as="h3" />
))(({ theme, isMissionFinished }) => ({
  color: isMissionFinished ? theme.color.maximumRed : theme.color.charcoal
}))

interface MissionCrewProps {}

const MissionCrew: React.FC<MissionCrewProps> = () => {
  const { missionId } = useParams()
  const [deleteCrew] = useDeleteMissionCrew()
  const { data: crew } = useMissionCrew(missionId!)
  const [addOrUpdateCrew] = useAddOrUpdateMissionCrew()
  const isMissionFinished = useIsMissionFinished(missionId)

  const [currentCrewId, setCurrentCrewId] = useState<string>()
  const [openCrewForm, setOpenCrewForm] = useState<boolean>(false)
  const [crewList, setCrewList] = useState<MissionCrewModel[] | undefined>(crew)

  const handleOpenCrewForm = (id?: string): void => {
    setCurrentCrewId(id)
    setOpenCrewForm(true)
  }

  // refresh the crew var after a mutation's refetchQuery
  useEffect(() => {
    setCrewList(crew)
  }, [crew])

  const onDeleteCrewMember = async (id?: string) => {
    if (!id) return
    await deleteCrew({ variables: { id } })
  }

  const handleSubmitForm = async (data: AddOrUpdateMissionCrewInput) => {
    if (!missionId) return
    const crew = { ...data, missionId: Number(missionId) }
    await addOrUpdateCrew({ variables: { crew } })
    setOpenCrewForm(false)
  }

  return (
    <>
      <UnderlineTitleLabel>Equipage à bord</UnderlineTitleLabel>
      <CrewStack>
        {!crewList || !crewList.length ? (
          <EmptyCrewListStackItem>
            <EmptyCrewListText isMissionFinished={!!isMissionFinished}>
              Aucun membre d'équipage renseigné
            </EmptyCrewListText>
          </EmptyCrewListStackItem>
        ) : (
          <Stack.Item style={{ width: '100%' }}>
            <CrewMemeberList
              crewList={crewList}
              handleEditCrew={handleOpenCrewForm}
              handleDeleteCrew={onDeleteCrewMember}
            />
          </Stack.Item>
        )}
        <Stack.Item style={{ width: '100%' }}>
          <AddCrewMemberButton onClick={() => handleOpenCrewForm()}>Ajouter un membre d’équipage</AddCrewMemberButton>
        </Stack.Item>
        <>
          {openCrewForm && crewList && (
            <MissionCrewForm
              data-testid="crew-form"
              crewList={crewList}
              crewId={currentCrewId}
              handleClose={setOpenCrewForm}
              handleSubmitForm={handleSubmitForm}
            />
          )}
        </>
      </CrewStack>
    </>
  )
}

export default MissionCrew
