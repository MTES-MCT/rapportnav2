import React, { useEffect, useState } from 'react'
import { Stack } from 'rsuite'
import { THEME, Label, Select, Icon, Button, Accent, Size, Dropdown } from '@mtes-mct/monitor-ui'
import omit from 'lodash/omit'
import { useParams } from 'react-router-dom'
import { Agent, MissionCrew as MissionCrewModel, AgentRole } from '../../../types/crew-types'
import Text from '../../../ui/text'
import useAgentRoles from './use-agent-roles'
import useAgentsByUserService from './use-agents-by-user-service'
import useAddOrUpdateMissionCrew, { AddOrUpdateMissionCrewInput } from './use-add-update-mission-crew'
import useMissionCrew from './use-mission-crew'
import useDeleteMissionCrew from './use-delete-mission-crew'

interface MissionCrewProps {}

const MissionCrew: React.FC<MissionCrewProps> = ({}) => {
  const { missionId } = useParams()

  const { data: agentRoles, agentRolesLoading, agentRolesError } = useAgentRoles()
  const { data: agents, agentsLoading, agentsError } = useAgentsByUserService()
  const { data: crew, crewLoading, crewError } = useMissionCrew(missionId!)
  const [addOrUpdateCrew, { updateData, updateLoading, updateError }] = useAddOrUpdateMissionCrew()
  const [deleteCrew, { deleteData, deleteLoading, deleteError }] = useDeleteMissionCrew()

  const [crewList, setCrewList] = useState<MissionCrewModel[] | undefined>(crew)
  const [hideAddButton, setHideAddButton] = useState<boolean>(false)

  // refresh the crew var after a mutation's refetchQuery
  useEffect(() => {
    setCrewList(crew)
  }, [crew])

  const onClickAddCrewMember = () => {
    setCrewList([...(crewList || []), {} as MissionCrewModel])
    setHideAddButton(true)
  }

  const onDeleteCrewMember = async (id: string) => {
    await deleteCrew({ variables: { id } })
  }

  const updateExistingCrew = async (missionCrew: MissionCrewModel, field?: string, value?: any) => {
    let data: AddOrUpdateMissionCrewInput = {
      missionId,
      ...omit(missionCrew, '__typename'),
      agent: omit(missionCrew.agent, '__typename'),
      role: omit(missionCrew.role, '__typename')
    }
    if (field === 'agent') {
      data = {
        ...data,
        [field]: omit(
          agents?.find((agent: Agent) => agent.id === value),
          '__typename'
        )
      }
    } else if (field === 'role') {
      data = {
        ...data,
        [field]: omit(
          agentRoles?.find((role: AgentRole) => role.id === value),
          '__typename'
        )
      }
    }

    await addOrUpdateCrew({ variables: { crew: data } })
  }

  const addNewCrew = async (field?: string, value?: any) => {
    const newCrew = (crewList?.pop() || {}) as MissionCrewModel
    if (field === 'agent') {
      newCrew.agent = omit(
        agents?.find((agent: Agent) => agent.id === value),
        '__typename'
      )
      setCrewList([...(crewList || []), newCrew])
    } else if (field === 'role') {
      newCrew.role = omit(
        agentRoles?.find((role: AgentRole) => role.id === value),
        '__typename'
      )
      const data: AddOrUpdateMissionCrewInput = {
        missionId,
        agent: newCrew.agent,
        role: newCrew.role
      }
      await addOrUpdateCrew({ variables: { crew: data } })
      setHideAddButton(false)
    }
  }

  const onChange = async (missionCrew: MissionCrewModel, field?: string, value?: any) => {
    if (field === 'agent' && !value) {
      return
    }
    if (missionCrew.id) {
      // case: crew member already exists
      updateExistingCrew(missionCrew, field, value)
    } else {
      // case: new crew member being created
      addNewCrew(field, value)
    }
  }

  return (
    <>
      <Label>Equipage à bord</Label>
      <Stack direction="column" alignItems="flex-start" spacing="1rem" style={{ width: '100%' }}>
        {!!!crewList || !crewList.length ? (
          <Stack.Item style={{ width: '100%', backgroundColor: THEME.color.gainsboro, padding: '0.5rem' }}>
            <Text as="h3">Aucun membre d'équipage renseigné</Text>
          </Stack.Item>
        ) : (
          crewList?.map((crew: MissionCrewModel) => (
            <Stack.Item key={crew.id} style={{ width: '100%', backgroundColor: THEME.color.gainsboro }}>
              <Stack
                direction="row"
                alignItems="flex-start"
                spacing="1rem"
                style={{ width: '100%', backgroundColor: THEME.color.gainsboro, padding: '0.5rem' }}
              >
                <Stack.Item style={{ flex: 1 }}>
                  <Select
                    name="agent"
                    label="Identité"
                    isLight={true}
                    value={crew?.agent?.id}
                    options={
                      (agents?.map(({ id, firstName, lastName }: Agent) => ({
                        value: id,
                        label: [firstName, lastName].join(' ')
                      })) as any) || []
                    }
                    onChange={(nextValue?: string) => onChange(crew, 'agent', nextValue)}
                  />
                </Stack.Item>
                <Stack.Item style={{ flex: 1 }}>
                  <Select
                    label="Fonction"
                    name="role"
                    isLight={true}
                    value={crew?.role?.id}
                    options={agentRoles?.map(({ id, title }) => ({ value: id, label: title })) as any}
                    disabled={!crew?.agent?.lastName}
                    onChange={(nextValue?: string) => onChange(crew, 'role', nextValue)}
                  />
                </Stack.Item>
                <Stack.Item style={{ flex: 0 }} alignSelf="center">
                  <Label>&nbsp;</Label> {/*  fake label to align the icon with the fields */}
                  <Dropdown
                    Icon={Icon.More}
                    accent={Accent.SECONDARY}
                    onSelect={function noRefCheck() {}}
                    title=""
                    placement="bottomEnd"
                  >
                    <Dropdown.Item
                      eventKey="DELETE"
                      onClick={() => onDeleteCrewMember(crew?.id)}
                      style={{ backgroundColor: THEME.color.white }}
                    >
                      Supprimer le membre
                    </Dropdown.Item>
                  </Dropdown>
                </Stack.Item>
              </Stack>
            </Stack.Item>
          ))
        )}
        {!hideAddButton && (
          <Stack.Item>
            <Button
              onClick={() => onClickAddCrewMember()}
              accent={Accent.SECONDARY}
              size={Size.NORMAL}
              Icon={Icon.Plus}
              role="add-crew-member-button"
              isFullWidth={false}
            >
              Ajouter un membre d’équipage
            </Button>
          </Stack.Item>
        )}
      </Stack>
    </>
  )
}

export default MissionCrew
