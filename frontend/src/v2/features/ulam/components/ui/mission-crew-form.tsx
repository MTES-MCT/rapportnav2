import {
  Accent,
  Button,
  Dialog,
  DialogProps,
  FormikMultiSelect,
  Icon,
  IconButton,
  IconButtonProps,
  Size,
  THEME
} from '@mtes-mct/monitor-ui'
import { Field, FieldProps, Form, Formik } from 'formik'
import React, { useEffect, useState } from 'react'
import { FlexboxGrid, Stack, StackProps } from 'rsuite'
import styled from 'styled-components'
import { Agent, MissionCrewMember } from '../../../common/types/crew-type.ts'

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
  agentIds: number[]
}

interface MissionCrewModalProps {
  agents: Agent[]
  crewMembers: MissionCrewMember[]
  handleClose: (open: boolean) => void
  handleEdit: (agentsIds: number[]) => void
}

const MissionCrewForm: React.FC<MissionCrewModalProps> = ({ agents, crewMembers, handleClose, handleEdit }) => {
  const [initValue, setInitValue] = useState<CrewForm>()

  useEffect(() => {
    const ids = crewMembers.map(crew => crew.agent?.id) ?? []
    setInitValue({ agentIds: ids })
  }, [crewMembers])

  const handleSubmit = async ({ agentIds }: CrewForm) => handleEdit(agentIds)

  return (
    <Dialog>
      <Dialog.Title>
        <FlexboxGrid align="middle" justify="space-between" style={{ paddingLeft: 24, paddingRight: 24 }}>
          <FlexboxGrid.Item>Ajout d'agent(s) de mission</FlexboxGrid.Item>
          <FlexboxGrid.Item>
            <CloseIconButton onClick={() => handleClose(false)} />
          </FlexboxGrid.Item>
        </FlexboxGrid>
      </Dialog.Title>
      {initValue && (
        <Formik initialValues={initValue} onSubmit={handleSubmit}>
          <Form>
            <CrewFormDialogBody>
              <CrewFormStack>
                <Stack.Item style={{ width: '100%' }}>
                  <CrewFormStack direction="row">
                    <Stack.Item style={{ flex: 1, width: '50%' }}>
                      <Field name="agentIds">
                        {({ field, form }: FieldProps<CrewForm>) => (
                          <FormikMultiSelect
                            {...field}
                            label="Nom des agents"
                            placeholder=""
                            isRequired={true}
                            searchable={true}
                            options={
                              agents?.map((agent: Agent) => ({
                                value: agent.id,
                                label: `${agent.firstName} ${agent.lastName}`
                              })) ?? []
                            }
                            isLight={true}
                            value={field.value}
                            onChange={(value: any) => form.setFieldValue(field.name, value)}
                          />
                        )}
                      </Field>
                    </Stack.Item>
                  </CrewFormStack>
                </Stack.Item>
              </CrewFormStack>
            </CrewFormDialogBody>
            <CrewFormDialogAction>
              <Button type="submit" data-testid="submit-crew-form-button" accent={Accent.PRIMARY}>
                Ajouter
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
