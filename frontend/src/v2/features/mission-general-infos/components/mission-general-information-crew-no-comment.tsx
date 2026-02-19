import { FormikCheckbox, THEME } from '@mtes-mct/monitor-ui'
import { FieldArrayRenderProps } from 'formik'
import React, { useState } from 'react'
import { Stack } from 'rsuite'
import { Agent, MissionCrew } from '../../common/types/crew-type.ts'
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
import Text from '@common/components/ui/text.tsx'

interface MissionGeneralInformationCrewNoCommentProps {
  name: string
  missionId?: number
  agents: Agent[]
  fieldArray: FieldArrayRenderProps
  isMissionFinished: boolean
}

const MissionGeneralInformationCrewNoComment: React.FC<MissionGeneralInformationCrewNoCommentProps> = ({
  name,
  agents,
  missionId,
  fieldArray,
  isMissionFinished
}) => {
  const [openForm, setOpenForm] = useState<boolean>(false)
  const handleDelete = (index: number) => fieldArray.remove(index)
  const handleUpdate = (members: MissionCrewMember[]) => fieldArray.form.setFieldValue(name, members)

  const handleEdit = (agentIds: number[], agents: Agent[]) => {
    handleUpdate(agents.filter(agent => agentIds.includes(agent.id)).map(agent => ({ agent, missionId })))
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
              onClick={() => handleUpdate(agents.map(agent => ({ agent, missionId })))}
              label={"Tous les agents de l'unité participent à la mission"}
            />
          </Stack.Item>
        </Stack>
      )}

      <MissionCrewStack>
        <Stack.Item style={{ width: '100%' }}>
          {isMissionFinished && !fieldArray.form.values.crew?.length ? (
            <Text as={'h2'} color={THEME.color.maximumRed} style={{ margin: '1rem 0' }}>
              Veuillez renseigner la liste d'agents participant à la mission{' '}
            </Text>
          ) : (
            <MissionCrewListStyled>
              {fieldArray.form.values.crew?.map((crewMember: MissionCrew, index: number) => (
                <MissionCrewListItemStyled
                  index={index}
                  key={`${crewMember?.agent?.id}-index`}
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
          )}
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
              handleEdit={agentIds => handleEdit(agentIds, agents ?? [])}
            />
          )}
        </>
      </MissionCrewStack>
    </>
  )
}

export default MissionGeneralInformationCrewNoComment
