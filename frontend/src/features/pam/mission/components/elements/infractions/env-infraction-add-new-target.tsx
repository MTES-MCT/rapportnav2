import React, { useState } from 'react'
import { Accent, Button, Icon, Size, THEME } from '@mtes-mct/monitor-ui'
import { ControlType } from '@common/types/control-types.ts'
import { Infraction, InfractionEnvNewTarget } from '@common/types/infraction-types.ts'
import { useParams } from 'react-router-dom'
import omit from 'lodash/omit'
import { Stack } from 'rsuite'
import EnvInfractionTargetAddedByUnitForm from './env-infraction-target-added-by-unit-form.tsx'
import useAddOrUpdateInfractionEnv from '../../../hooks/use-add-update-infraction-env.tsx'
import { ActionTargetTypeEnum } from '@common/types/env-mission-types.ts'

export interface EnvInfractionNewTargetProps {
  availableControlTypesForInfraction?: ControlType[]
  infractions?: Infraction[]
  actionTargetType?: ActionTargetTypeEnum
}

const EnvInfractionAddNewTarget: React.FC<EnvInfractionNewTargetProps> = ({ availableControlTypesForInfraction, actionTargetType }) => {
  const { missionId, actionId } = useParams()

  const [showInfractionForNewTarget, setShowInfractionForNewTarget] = useState<boolean>(false)

  const [formData, setFormData] = useState<Infraction | undefined>(undefined) // only 1 infraction for nav and fish

  const onChangeFormField = (field: string, value: any) => {
    setFormData((prevData: any) => ({ ...(prevData || {}), [field]: value }))
  }

  const onChangeTargetFormField = (field: string, value: any) => {
    setFormData((prevData: any) => ({ ...(prevData || {}), target: { ...(prevData || {}).target, [field]: value } }))
  }

  const [mutate] = useAddOrUpdateInfractionEnv()

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

    await mutate({ variables: { infraction: mutationData } })
    setShowInfractionForNewTarget(false)
  }

  return (
    <>
      {showInfractionForNewTarget ? (
        <div
          style={{
            width: '100%',
            backgroundColor: THEME.color.white,
            padding: '1rem'
          }}
        >
          <form onSubmit={(e: React.FormEvent) => onSubmit(e, formData)} data-testid={'new-target-form'}>
            <EnvInfractionTargetAddedByUnitForm
              actionTargetType={actionTargetType}
              infraction={formData}
              onChange={onChangeFormField}
              onChangeTarget={onChangeTargetFormField}
              availableControlTypesForInfraction={availableControlTypesForInfraction}
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
            <Button
              onClick={() => setShowInfractionForNewTarget(true)}
              accent={Accent.SECONDARY}
              size={Size.NORMAL}
              Icon={Icon.Plus}
              role={'add-target'}
            >
              Ajouter une infraction
            </Button>
          </Stack.Item>
        </Stack>
      )}
    </>
  )
}

export default EnvInfractionAddNewTarget
