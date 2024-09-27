import React, { useEffect, useState } from 'react'
import { Stack } from 'rsuite'
import { Select, TextInput } from '@mtes-mct/monitor-ui'
import { ControlType } from '@common/types/control-types.ts'
import { Infraction } from '@common/types/infraction-types.ts'
import InfractionForm from './infraction-form.tsx'
import {
  CONTROL_TYPE_OPTIONS,
  getDisabledControlTypes,
  VESSEL_SIZE_OPTIONS,
  VESSEL_TYPE_OPTIONS
} from '../../../utils/control-utils.ts'
import { VesselSizeEnum, VesselTypeEnum } from '@common/types/mission-types.ts'
import { ActionTargetTypeEnum, VehicleTypeEnum } from '@common/types/env-mission-types.ts'

interface EnvInfractionNewTargetFormProps {
  infraction?: Infraction
  availableControlTypesForInfraction?: ControlType[]
  onChange: (field: string, value: any) => void
  onChangeTarget: (field: string, value: any) => void
  onCancel: () => void
  actionTargetType?: ActionTargetTypeEnum
}

const EnvInfractionTargetAddedByUnitForm: React.FC<EnvInfractionNewTargetFormProps> = ({
  infraction,
  availableControlTypesForInfraction,
  onChange,
  onChangeTarget,
  onCancel,
  actionTargetType
}) => {
  const [identityControlledPersonValue, setIdentityControlledPersonValue] = useState<string | undefined>(undefined)
  const [vesselIdentifierValue, setVesselIdentifierValue] = useState<string | undefined>(undefined)

  useEffect(() => {
    setIdentityControlledPersonValue(infraction?.target?.identityControlledPerson)
    setVesselIdentifierValue(infraction?.target?.vesselIdentifier)
  }, [infraction])

  const handleIdentityControlledPersonChange = (nextValue?: string) => {
    setIdentityControlledPersonValue(nextValue)
  }
  const handleIdentityControlledPersonBlur = async () => {
    onChangeTarget('identityControlledPerson', identityControlledPersonValue)
  }
  const handleVesselIdentifierChange = (nextValue?: string) => {
    setVesselIdentifierValue(nextValue)
  }
  const handleVesselIdentifierBlur = async () => {
    onChangeTarget('vesselIdentifier', vesselIdentifierValue)
  }

  return (
    <Stack direction="column" spacing={'2rem'} style={{ width: '100%', padding: '1rem' }}>
      <Stack.Item style={{ width: '100%' }}>
        <Select
          label="Type de contrôle avec infraction"
          isRequired={true}
          options={CONTROL_TYPE_OPTIONS}
          disabledItemValues={getDisabledControlTypes(availableControlTypesForInfraction)}
          value={infraction?.controlType}
          name="controlType"
          onChange={(nextValue: ControlType | undefined) => onChange('controlType', nextValue)}
        />
      </Stack.Item>
      <Stack.Item style={{ width: '100%' }}>
        <Stack direction="column" alignItems="baseline" spacing={'0.5rem'}>

          {infraction?.target?.vehicleType === VehicleTypeEnum.VESSEL && (
            <Stack.Item style={{ width: '100%' }} data-testid={"stack-vessel-infraction-env"}>
              <Stack direction="row" alignItems="baseline" spacing={'0.5rem'}>
                <Stack.Item style={{ width: '40%' }}>
                  <Select
                    label="Taille du navire"
                    isRequired={true}
                    options={VESSEL_SIZE_OPTIONS}
                    value={infraction?.target?.vesselSize}
                    name="vesselSize"
                    onChange={(nextValue: VesselSizeEnum | undefined) => onChangeTarget('vesselSize', nextValue)}
                  />
                </Stack.Item>
                <Stack.Item style={{ width: '60%' }}>
                  <Select
                    label="Type de navire"
                    isRequired={true}
                    options={VESSEL_TYPE_OPTIONS}
                    value={infraction?.target?.vesselType}
                    name="vesselType"
                    onChange={(nextValue: VesselTypeEnum | undefined) => onChangeTarget('vesselType', nextValue)}
                  />
                </Stack.Item>
              </Stack>
            </Stack.Item>
          )}

          {(actionTargetType === ActionTargetTypeEnum.VEHICLE) && (
            <Stack.Item style={{ width: '100%' }}>
              <TextInput
                label="Immatriculation"
                isRequired={true}
                value={vesselIdentifierValue}
                name="vesselIdentifier"
                role="vesselIdentifier"
                onChange={handleVesselIdentifierChange}
                onBlur={handleVesselIdentifierBlur}
                data-testid={'vessel-identifier'}
              />
            </Stack.Item>
          )}

          <Stack.Item style={{ width: '100%', marginTop: '10px' }}>
            <Stack direction="row" alignItems="baseline" spacing={'0.5rem'}>
              <Stack.Item style={{ width: '100%' }}>
                <TextInput
                  label={
                  actionTargetType === ActionTargetTypeEnum.COMPANY
                    ? "Identité de la personne morale contrôlée"
                    : "Identité de la personne contrôlée"
                }
                  isRequired={true}
                  value={identityControlledPersonValue}
                  name="identityControlledPerson"
                  role="identityControlledPerson"
                  onChange={handleIdentityControlledPersonChange}
                  onBlur={handleIdentityControlledPersonBlur}
                />
              </Stack.Item>
            </Stack>
          </Stack.Item>
        </Stack>
      </Stack.Item>
      <Stack.Item style={{ width: '100%' }}>
        <InfractionForm infraction={infraction} onChange={onChange} onCancel={onCancel} />
      </Stack.Item>
    </Stack>
  )
}

export default EnvInfractionTargetAddedByUnitForm
