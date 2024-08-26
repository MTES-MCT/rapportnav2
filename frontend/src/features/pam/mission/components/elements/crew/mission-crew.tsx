import { Accent, Button, ButtonProps, Icon, Label, LabelProps, Size } from '@mtes-mct/monitor-ui'
import React, { useEffect, useState } from 'react'
import { useParams } from 'react-router-dom'
import { Stack, StackProps } from 'rsuite'
import styled from 'styled-components'
import { MissionCrew as MissionCrewModel } from '../../../../../common/types/crew-types.ts'
import Text, { TextProps } from '../../../../../common/components/ui/text.tsx'
import useIsMissionFinished from '../../../hooks/use-is-mission-finished.tsx'
import MissionCrewForm from './mission-crew-form.tsx'
import CrewMemeberList from './mission-crew-member-list.tsx'
import useAddOrUpdateMissionCrew, { AddOrUpdateMissionCrewInput } from '../../../hooks/use-add-update-mission-crew.tsx'
import useDeleteMissionCrew from '../../../hooks/use-delete-mission-crew.tsx'
import useMissionCrew from '../../../hooks/use-mission-crew.tsx'

const TitleLabel = styled((props: LabelProps) => <Label {...props} />)(({ theme }) => ({
  fontWeight: 'bold',
  color: theme.color.gunMetal
}))

const UnderlineStack = styled((props: StackProps) => <Stack {...props} direction="row" alignItems="center" />)(
  ({ theme }) => ({
    paddingBottom: 8,
    borderBottom: `1px solid ${theme.color.charcoal}`
  })
)

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
  <Stack {...props} direction="column" alignItems="center" spacing="0.25rem" />
))({
  width: '100%'
})


const EmptyCrewListText = styled((props: Omit<TextProps, 'as'> & { isMissionFinished: boolean }) => (
  <Text {...props} as="h3" />
))(({ theme, isMissionFinished }) => ({
  paddingLeft: 8,
  fontStyle: 'italic',
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
  const [crewList, setCrewList] = useState<MissionCrewModel[]>([])

  const handleOpenCrewForm = (id?: string): void => {
    setCurrentCrewId(id)
    setOpenCrewForm(true)
  }

  // refresh the crew var after a mutation's refetchQuery
  useEffect(() => {
    if (!crew) return
    setCrewList(crew)
  }, [crew])

  const onDeleteCrewMember = async (id?: string) => {
    if (!id) return
    await deleteCrew({ variables: { id } })
  }

  const handleSubmitForm = async (data: Omit<AddOrUpdateMissionCrewInput, 'missionId'>) => {
    if (!missionId) return
    const crew = { ...data, missionId: missionId }
    await addOrUpdateCrew({ variables: { crew } })
    setOpenCrewForm(false)
  }

  return (
    <>
      <UnderlineStack>
        <TitleLabel>Equipage</TitleLabel>
        {crewList?.length === 0 && (
          <EmptyCrewListText isMissionFinished={!!isMissionFinished}>
            Selectionner votre bordée, pour importer votre liste d'équipage
          </EmptyCrewListText>
        )}
      </UnderlineStack>

      <CrewStack>
        <Stack.Item style={{ width: '100%' }}>
          <CrewMemeberList
            crewList={crewList}
            handleEditCrew={handleOpenCrewForm}
            handleDeleteCrew={onDeleteCrewMember}
          />
        </Stack.Item>
        <Stack.Item style={{ width: '100%', marginTop: 16 }}>
          <AddCrewMemberButton disabled={crewList?.length === 0} onClick={() => handleOpenCrewForm()}>
            Ajouter un membre d’équipage
          </AddCrewMemberButton>
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
