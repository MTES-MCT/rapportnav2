import React, { useState } from 'react'
import { Accent, Button, Icon, Label, Size, THEME } from '@mtes-mct/monitor-ui'
import { Infraction, ControlType, InfractionEnvNewTarget } from '../../mission-types'
import InfractionSummary from './infraction-summary'
import InfractionForm from './infraction-form'
import { infractionButtonTitle } from './utils'
import { GET_MISSION_BY_ID, MUTATION_ADD_OR_UPDATE_INFRACTION_ENV } from '../queries'
import { useMutation } from '@apollo/client'
import { useParams } from 'react-router-dom'
import omit from 'lodash/omit'
import { Stack } from 'rsuite'
import EnvInfractionNewTargetForm from './env-infraction-new-target-form'

export interface EnvInfractionNewTargetProps {
  availableControlTypes?: ControlType[]
  infractions?: Infraction[]
}

const EnvInfractionNewTarget: React.FC<EnvInfractionNewTargetProps> = ({ availableControlTypes }) => {
  const { missionId, actionId } = useParams()

  const [showInfractionForNewTarget, setShowInfractionForNewTarget] = useState<boolean>(false)

  const [formData, setFormData] = useState<InfractionEnvNewTarget | undefined>(undefined) // only 1 infraction for nav and fish

  const onChangeFormField = (field: string, value: any) => {
    setFormData((prevData: any) => ({ ...prevData, [field]: value }))
  }

  const [mutate, { mutateData, mutateLoading, mutateError }] = useMutation(MUTATION_ADD_OR_UPDATE_INFRACTION_ENV, {
    refetchQueries: [GET_MISSION_BY_ID]
  })

  const onSubmit = async (e: React.FormEvent, data?: InfractionEnvNewTarget) => {
    e.preventDefault()
    const mutationData = {
      ...omit(data, '__typename'),
      id: data?.id,
      missionId,
      actionId
      // controlId,
      // controlType
    }
    debugger
    await mutate({ variables: { infraction: mutationData } })
    setShowInfractionForNewTarget(false)
  }

  return (
    <>
      {showInfractionForNewTarget ? (
        <div style={{ width: '100%', backgroundColor: THEME.color.white, padding: '1rem' }}>
          <form onSubmit={(e: React.FormEvent) => onSubmit(e, formData)}>
            <EnvInfractionNewTargetForm
              infraction={formData}
              onChange={onChangeFormField}
              availableControlTypes={availableControlTypes}
              onCancel={() => {
                setFormData(undefined)
                setShowInfractionForNewTarget(false)
              }}
            />
          </form>
        </div>
      ) : (
        <Stack justifyContent="flex-end">
          <Stack.Item>
            {' '}
            <Button
              onClick={() => setShowInfractionForNewTarget(true)}
              accent={Accent.SECONDARY}
              size={Size.NORMAL}
              Icon={Icon.Plus}
            >
              Ajouter une nouvelle cible avec infraction
            </Button>
          </Stack.Item>
        </Stack>
      )}
    </>
  )
}

export default EnvInfractionNewTarget
