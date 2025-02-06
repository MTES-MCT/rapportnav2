import { Accent, Button, ButtonProps, Checkbox, Icon, Label, LabelProps, Size } from '@mtes-mct/monitor-ui'
import React, { useEffect, useState } from 'react'
import { useParams } from 'react-router-dom'
import { Stack, StackProps } from 'rsuite'
import styled from 'styled-components'
import { MissionCrew as MissionCrewModel } from '../../../common/types/crew-type.ts'
import MissionCrewMemberList from './mission-crew/mission-crew-member-list.tsx'
import MissionCrewForm from './mission-crew/mission-crew-form.tsx'
import useMissionCrewQuery from '../../../common/services/use-mission-crews-by-mission.tsx'
import useAddMultipleCrewsMutation from '../../../common/services/use-add-multiple-crews.tsx'
import useAgentsQuery from '../../../common/services/use-agents.tsx'
import useDeleteMissionCrewMutation from '../../../common/services/use-delete-crew.tsx'


const TitleLabel = styled((props: LabelProps) => <Label {...props} />)(({ theme }) => ({
  fontWeight: 'bold',
  color: theme.color.gunMetal
}))

const UnderlineStack = styled((props: StackProps) => <Stack {...props} direction="row" alignItems="center" />)(
  ({ theme }) => ({
    paddingBottom: 4,
    borderBottom: `0.5px solid ${theme.color.lightGray}`,
    marginBottom: 8
  })
)

const AddCrewMemberButton = styled((props: ButtonProps) => (
  <Button
    Icon={Icon.Plus}
    size={Size.SMALL}
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

interface MissionCrewProps {}

const MissionCrewUlam: React.FC<MissionCrewProps> = () => {

  const { missionId } = useParams()
  const { data: crew } = useMissionCrewQuery(missionId)
  const {data: agents } = useAgentsQuery()

  const multipleCrewMutation = useAddMultipleCrewsMutation(missionId)
  const deleteCrewMutation = useDeleteMissionCrewMutation(missionId)

  const [openCrewForm, setOpenCrewForm] = useState<boolean>(false)
  const [crewList, setCrewList] = useState<MissionCrewModel[]>([])

  useEffect(() => {
    if (!crew) return
    setCrewList(crew)
  }, [crew])

  const loadAgentsService = async () => {
    let crews: MissionCrewModel[] = []
    agents?.forEach((agent) => {
      const crew: MissionCrewModel = {
        agent: agent,
        missionId: Number(missionId)
      }
      crews.push(crew)
    })

    await multipleCrewMutation.mutateAsync(crews)

  }



  const handleOpenCrewForm = (): void => {
    setOpenCrewForm(true)
  }

  const onDeleteCrewMember = async (id?: number) => {
    if (!id) return
   await deleteCrewMutation.mutateAsync(id)
  }

  const handleSubmitForm = async (crews: MissionCrewModel[]) => {
    if (crews.length === 0) return
    await multipleCrewMutation.mutateAsync(crews)
    setOpenCrewForm(false)
  }

  return (
    <>
      <UnderlineStack>
        <TitleLabel>Agents de la mission</TitleLabel>
      </UnderlineStack>

      {crewList.length === 0 && (
        <Stack direction={"row"} style={{width:'100%', marginBottom: '1rem'}}>
          <Stack.Item>
            <Checkbox
              label={"Tous les agents de l'unité participent à la mission"}
              onClick={() => loadAgentsService()}
              name={"isAllAgentsParticipating"}
            />
          </Stack.Item>
        </Stack>
      )}


      <CrewStack>
        <Stack.Item style={{ width: '100%' }}>
          <MissionCrewMemberList
            crewList={crewList}
            handleEditCrew={handleOpenCrewForm}
            handleDeleteCrew={onDeleteCrewMember}
          />
        </Stack.Item>
        <Stack.Item style={{ width: '100%', marginTop: 16 }}>
          <AddCrewMemberButton onClick={() => handleOpenCrewForm()}>
            Ajouter un/des agent(s) à la mission
          </AddCrewMemberButton>
        </Stack.Item>
        <>
          {openCrewForm && crewList && (
            <MissionCrewForm
              data-testid="crew-form"
              crewList={crewList}
              handleClose={setOpenCrewForm}
              handleSubmitForm={handleSubmitForm}
              missionId={Number(missionId)}
            />
          )}
        </>
      </CrewStack>
    </>
  )
}

export default MissionCrewUlam
