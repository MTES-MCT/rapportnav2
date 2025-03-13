import { FormikCheckbox } from '@mtes-mct/monitor-ui'
import { FieldArrayRenderProps } from 'formik'
import React, { useState } from 'react'
import { Stack } from 'rsuite'
import { Agent, MissionCrewMember } from '../../common/types/crew-type.ts'
import MissionCrewFormNoComment from '../ui/mission-crew-form-no-comment.tsx'
import MissionCrewListNoComment from '../ui/mission-crew-list-no-comment.tsx'
import {
  MissionCrewAddMemberButton,
  MissionCrewListItemStyled,
  MissionCrewListStyled,
  MissionCrewStack,
  MissionCrewTitleLabel,
  MissionCrewUnderlineStack
} from '../ui/mission-crew-list.tsx'

interface MissionGeneralInformationCrewNoCommentProps {
  name: string
  missionId?: number
  agents: Agent[]
  fieldArray: FieldArrayRenderProps
}

const MissionGeneralInformationCrewNoComment: React.FC<MissionGeneralInformationCrewNoCommentProps> = ({
  name,
  agents,
  missionId,
  fieldArray
}) => {
  const [openForm, setOpenForm] = useState<boolean>(false)
  const handleDelete = (index: number) => fieldArray.remove(index)
  const handleUpdate = (members: MissionCrewMember[]) => fieldArray.form.setFieldValue(name, members)

  const getCrewMembers = (agents: Agent[], crewAgentIds: number[] = []): MissionCrewMember[] => {
    return agents.filter(agent => !crewAgentIds.includes(agent.id)).map(agent => ({ agent, missionId }))
  }

  const handleEdit = (agents: Agent[], agentIds: number[], crewMembers: MissionCrewMember[]) => {
    const crewAgentIds = crewMembers.map(crew => crew.agent.id)
    const filteredAgents = agents.filter(a => agentIds.includes(a.id))

    const members = getCrewMembers(filteredAgents, crewAgentIds)
    handleUpdate(members)
    setOpenForm(false)
  }

  return (
    <>
      <MissionCrewUnderlineStack>
        <MissionCrewTitleLabel>Agents de la mission</MissionCrewTitleLabel>
      </MissionCrewUnderlineStack>

      {fieldArray.form.values.crew?.length === 0 && (
        <Stack direction={'row'} style={{ width: '100%' }}>
          <Stack.Item>
            <FormikCheckbox
              name={'isAllAgentsParticipating'}
              onClick={() => handleUpdate(getCrewMembers(agents ?? []))}
              label={"Tous les agents de l'unité participent à la mission"}
            />
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
                <MissionCrewListNoComment
                  name={name}
                  index={index}
                  crewMember={crewMember}
                  handleDelete={handleDelete}
                  handleEdit={() => setOpenForm(true)}
                />
              </MissionCrewListItemStyled>
            ))}
          </MissionCrewListStyled>
        </Stack.Item>
        <Stack.Item style={{ width: '100%', marginTop: '16px' }}>
          <MissionCrewAddMemberButton onClick={() => setOpenForm(true)}>
            Ajouter un/des agent(s) à la mission
          </MissionCrewAddMemberButton>
        </Stack.Item>
        <>
          {openForm && (
            <MissionCrewFormNoComment
              agents={agents ?? []}
              data-testid="crew-form"
              handleClose={setOpenForm}
              crewMembers={fieldArray.form.values.crew}
              handleEdit={agentIds => handleEdit(agents ?? [], agentIds, fieldArray.form.values.crew)}
            />
          )}
        </>
      </MissionCrewStack>
    </>
  )
}

export default MissionGeneralInformationCrewNoComment
