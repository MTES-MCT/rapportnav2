import React, { useState } from 'react'
import { Accent, Button, Icon, Label, Size, THEME } from '@mtes-mct/monitor-ui'
import {
  Infraction,
  ControlType,
  InfractionByTarget,
  InfractionTarget,
  InfractionEnvNewTarget
} from '../../mission-types'
import {
  GET_MISSION_BY_ID,
  MUTATION_ADD_OR_UPDATE_INFRACTION,
  MUTATION_ADD_OR_UPDATE_INFRACTION_ENV,
  MUTATION_DELETE_INFRACTION
} from '../queries'
import { useMutation } from '@apollo/client'
import { useParams } from 'react-router-dom'
import omit from 'lodash/omit'
import EnvInfractionNewTargetForm from './env-infraction-new-target-form'
import EnvInfractionSummary from './env-infraction-summary'
import EnvInfractionExistingTargetForm from './env-infraction-existing-target-form'

export interface EnvInfractionExistingTargetProps {
  availableControlTypes?: ControlType[]
  infractionsByTarget?: InfractionByTarget[]
}

const EnvInfractionExistingTarget: React.FC<EnvInfractionExistingTargetProps> = ({
  availableControlTypes,
  infractionsByTarget
}) => {
  const { missionId, actionId } = useParams()

  const [selectedVessel, setSelectedVessel] = useState<string | undefined>(undefined)

  const [formData, setFormData] = useState<Infraction | undefined>(undefined) // only 1 infraction for nav and fish

  const [mutate, { mutateData, mutateLoading, mutateError }] = useMutation(MUTATION_ADD_OR_UPDATE_INFRACTION_ENV, {
    refetchQueries: [GET_MISSION_BY_ID]
  })

  const [deleteMutation] = useMutation(MUTATION_DELETE_INFRACTION, {
    refetchQueries: [GET_MISSION_BY_ID]
  })

  const onChangeFormField = (field: string, value: any) => {
    setFormData((prevData: any) => ({ ...prevData, [field]: value }))
  }

  const onSubmit = async (e: React.FormEvent, infraction?: Infraction) => {
    e.preventDefault()
    const mutationData = {
      ...omit(infraction, '__typename', 'target'),
      id: infraction?.id,
      missionId,
      actionId,
      // controlType: infraction?.controlType,
      vesselType: infraction?.target?.vesselType,
      vesselSize: infraction?.target?.vesselSize,
      vesselIdentifier: infraction?.target?.vesselIdentifier,
      identityControlledPerson: infraction?.target?.identityControlledPerson
      // controlId,
      // controlType
    } as unknown as InfractionEnvNewTarget
    debugger
    await mutate({ variables: { infraction: mutationData } })
    setSelectedVessel(undefined)
  }

  const deleteInfraction = (infractionId: string) => {
    debugger
    deleteMutation({
      variables: {
        id: infractionId
      }
    })
  }

  return (
    <>
      {!!infractionsByTarget?.length &&
        infractionsByTarget?.map((infractionByTarget: InfractionByTarget) => (
          <div style={{ width: '100%', backgroundColor: THEME.color.white, padding: '1rem', marginBottom: '0.25rem' }}>
            {selectedVessel === infractionByTarget.vesselIdentifier ? (
              <>
                <form onSubmit={(e: React.FormEvent) => onSubmit(e, formData)}>
                  <EnvInfractionExistingTargetForm
                    infraction={formData}
                    onChange={onChangeFormField}
                    onCancel={() => {
                      setFormData(undefined)
                      setSelectedVessel(undefined)
                    }}
                    availableControlTypes={availableControlTypes}
                  />
                </form>
              </>
            ) : (
              <EnvInfractionSummary
                infractionByTarget={infractionByTarget}
                onAddInfractionForTarget={(infraction?: Partial<Infraction>) => {
                  setFormData(infraction as Infraction)
                  setSelectedVessel(infractionByTarget.vesselIdentifier)
                }}
                onEditInfractionForTarget={(infraction: Infraction) => {
                  setFormData(infraction)
                  setSelectedVessel(infractionByTarget.vesselIdentifier)
                }}
                onDeleteInfraction={(infractionId: string) => deleteInfraction(infractionId)}
              />
            )}
          </div>
        ))}
    </>
  )
}

export default EnvInfractionExistingTarget
