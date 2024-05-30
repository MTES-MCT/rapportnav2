import useAgentRoles from './use-agent-roles'

import {
  Accent,
  Button,
  Dialog,
  DialogProps,
  FormikSelect,
  FormikTextInput,
  Icon,
  IconButton,
  IconButtonProps,
  Size,
  THEME
} from '@mtes-mct/monitor-ui'
import { Form, Formik } from 'formik'
import { useEffect, useState } from 'react'
import { FlexboxGrid, Stack, StackProps } from 'rsuite'
import styled from 'styled-components'
import * as Yup from 'yup'
import { Agent, MissionCrew as MissionCrewModel } from '../../../types/crew-types'
import { AddOrUpdateMissionCrewInput } from './use-add-update-mission-crew'
import useAgentsByUserService from './use-agents-by-user-service'

const CrewFormDialogBody = styled((props: DialogProps) => <Dialog.Body {...props} />)(({ theme }) => ({
  padding: 24,
  width: '100%',
  backgroundColor: theme.color.gainsboro
}))

const CrewFormDialogAction = styled((props: DialogProps) => <Dialog.Action {...props} />)(({ theme }) => ({
  paddingTop: 0,
  paddingBottom: 32,
  justifyContent: 'center',
  backgroundColor: theme.color.gainsboro
}))

const CrewFormStack = styled((props: StackProps) => (
  <Stack direction="column" alignItems="flex-start" spacing="1rem" {...props} />
))({
  width: '100%'
})

const CloseIconButton = styled((props: Omit<IconButtonProps, 'Icon'>) => (
  <IconButton
    {...props}
    Icon={Icon.Close}
    size={Size.NORMAL}
    role={'quit-modal-crew'}
    accent={Accent.TERTIARY}
    color={THEME.color.gainsboro}
    data-testid="close-crew-form-icon"
  />
))(({ theme }) => ({
  color: theme.color.gainsboro
}))

type CrewForm = {
  roleId?: string
  comment: string
  agentId?: string
}

const COMMENT_MAX_LENGTH = 23

const crewSchema = Yup.object().shape({
  roleId: Yup.string().required('Fonction requise.'),
  agentId: Yup.string().required('Identité requise.'),
  comment: Yup.string().max(COMMENT_MAX_LENGTH, 'Maximum 23 caractères.')
})

interface MissionCrewModalProps {
  crewId?: string
  crewList: MissionCrewModel[]
  handleClose: (open: boolean) => void
  handleSubmitForm: (crew: AddOrUpdateMissionCrewInput) => Promise<void>
}

const MissionCrewForm: React.FC<MissionCrewModalProps> = ({ crewId, crewList, handleClose, handleSubmitForm }) => {
  const { data: agentRoles } = useAgentRoles()
  const { data: agents } = useAgentsByUserService()
  const [initValue, setInitValue] = useState<CrewForm>()

  useEffect(() => {
    const crew = crewList?.find(crew => crew.id === crewId)
    setInitValue({ roleId: crew?.role?.id, agentId: crew?.agent?.id, comment: crew?.comment || '' })
  }, [crewId])

  const handleSubmit = async (value: CrewForm) => {
    const agent = agents?.find(agent => agent.id === value.agentId)
    const role = agentRoles?.find(role => role.id === value.roleId)
    const crew: AddOrUpdateMissionCrewInput = {
      role,
      agent,
      id: crewId,
      comment: value.comment
    }
    await handleSubmitForm(crew)
  }

  return (
    <Dialog>
      <Dialog.Title>
        <FlexboxGrid align="middle" justify="space-between" style={{ paddingLeft: 24, paddingRight: 24 }}>
          <FlexboxGrid.Item>{`${crewId ? 'Mise à jour' : 'Ajout'} d’un membre d’équipage`}</FlexboxGrid.Item>
          <FlexboxGrid.Item>
            <CloseIconButton onClick={() => handleClose(false)} />
          </FlexboxGrid.Item>
        </FlexboxGrid>
      </Dialog.Title>
      {initValue && (
        <Formik
          onSubmit={handleSubmit}
          validateOnChange={false}
          initialValues={initValue}
          validationSchema={crewSchema}
        >
          <Form>
            <CrewFormDialogBody>
              <CrewFormStack>
                <Stack.Item style={{ width: '100%' }}>
                  <CrewFormStack direction="row">
                    <Stack.Item style={{ flex: 1, width: '50%' }}>
                      <FormikSelect
                        name="agentId"
                        label="Identité"
                        isLight={true}
                        isRequired={true}
                        options={
                          agents?.map((agent: Agent) => ({
                            value: agent.id,
                            label: `${agent.firstName} ${agent.lastName}`
                          })) || []
                        }
                        disabledItemValues={crewList?.map(crew => crew.agent?.id).filter(Boolean) as string[]}
                      />
                    </Stack.Item>
                    <Stack.Item style={{ flex: 1, width: '50%' }}>
                      <FormikSelect
                        name="roleId"
                        isLight={true}
                        label="Fonction"
                        isRequired={true}
                        options={(agentRoles ?? [])?.map(({ id, title }) => ({ value: id, label: title }))}
                      />
                    </Stack.Item>
                  </CrewFormStack>
                </Stack.Item>
                <Stack.Item style={{ flex: 1, width: '100%' }}>
                  <FormikTextInput
                    isLight={true}
                    name="comment"
                    itemType="text"
                    label="Commentaires (23 caractères max.)"
                  />
                </Stack.Item>
              </CrewFormStack>
            </CrewFormDialogBody>
            <CrewFormDialogAction>
              <Button type="submit" data-testid="submit-crew-form-button" accent={Accent.PRIMARY}>
                {`${crewId ? 'Mettre à jour' : 'Ajouter'} un membre`}
              </Button>
              <Button accent={Accent.SECONDARY} onClick={() => handleClose(false)}>
                Annuler
              </Button>
            </CrewFormDialogAction>
          </Form>
        </Formik>
      )}
    </Dialog>
  )
}

export default MissionCrewForm
