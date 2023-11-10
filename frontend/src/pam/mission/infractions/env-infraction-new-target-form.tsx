import React from 'react'
import { Stack } from 'rsuite'
import { Select, TextInput } from '@mtes-mct/monitor-ui'
import { ControlType, InfractionEnvNewTarget, VESSEL_SIZE_OPTIONS, VESSEL_TYPE_OPTIONS } from '../../mission-types'
import InfractionForm from './infraction-form'
import { controlTitle, getDisabledControlTypes } from '../controls/utils'

const CONTROL_TYPE_OPTIONS = [
  {
    label: `Infraction - ${controlTitle(ControlType.ADMINISTRATIVE)}`,
    value: ControlType.ADMINISTRATIVE
  },
  {
    label: `Infraction - ${controlTitle(ControlType.SECURITY)}`,
    value: ControlType.SECURITY
  },
  {
    label: `Infraction - ${controlTitle(ControlType.NAVIGATION)}`,
    value: ControlType.NAVIGATION
  },
  {
    label: `Infraction - ${controlTitle(ControlType.GENS_DE_MER)}`,
    value: ControlType.GENS_DE_MER
  }
]

interface EnvInfractionNewTargetFormProps {
  infraction?: InfractionEnvNewTarget
  availableControlTypes?: ControlType[]
  availableNatinfs?: string[]
  onChange: (field: string, value: any) => void
  onCancel: () => void
}

const EnvInfractionNewTargetForm: React.FC<EnvInfractionNewTargetFormProps> = ({
  infraction,
  availableControlTypes,
  availableNatinfs,
  onChange,
  onCancel
}) => {
  return (
    <Stack direction="column" spacing={'2rem'} style={{ width: '100%' }}>
      <Stack.Item style={{ width: '100%' }}>
        <Select
          label="Type de contrôle avec infraction"
          options={CONTROL_TYPE_OPTIONS}
          disabledItemValues={getDisabledControlTypes(availableControlTypes)}
          value={infraction?.controlType}
          name="controlType"
          onChange={(nextValue: OptionValue) => onChange('controlType', nextValue)}
        />
      </Stack.Item>
      <Stack.Item style={{ width: '100%' }}>
        <Stack direction="column" alignItems="baseline" spacing={'0.5rem'}>
          <Stack.Item style={{ width: '100%' }}>
            <Stack direction="row" alignItems="baseline" spacing={'0.5rem'}>
              <Stack.Item style={{ width: '40%' }}>
                <Select
                  label="Taille du navire"
                  options={VESSEL_SIZE_OPTIONS}
                  value={infraction?.vesselSize}
                  name="vesselSize"
                  onChange={(nextValue: OptionValue) => onChange('vesselSize', nextValue)}
                />
              </Stack.Item>
              <Stack.Item style={{ width: '60%' }}>
                <Select
                  label="Type de navire"
                  options={VESSEL_TYPE_OPTIONS}
                  value={infraction?.vesselType}
                  name="vesselType"
                  onChange={(nextValue: OptionValue) => onChange('vesselType', nextValue)}
                />
              </Stack.Item>
            </Stack>
          </Stack.Item>
          <Stack.Item style={{ width: '100%' }}>
            <Stack direction="row" alignItems="baseline" spacing={'0.5rem'}>
              <Stack.Item style={{ width: '40%' }}>
                <TextInput
                  label="Immatriculation"
                  value={infraction?.vesselIdentifier}
                  name="vesselIdentifier"
                  onChange={(nextValue?: string) => onChange('vesselIdentifier', nextValue)}
                />
              </Stack.Item>
              <Stack.Item style={{ width: '60%' }}>
                <TextInput
                  label="Identité de la personne contrôlée"
                  value={infraction?.identityControlledPerson}
                  name="identityControlledPerson"
                  onChange={(nextValue?: string) => onChange('identityControlledPerson', nextValue)}
                />
              </Stack.Item>
            </Stack>
          </Stack.Item>
        </Stack>
      </Stack.Item>
      <Stack.Item style={{ width: '100%' }}>
        <InfractionForm
          infraction={infraction}
          availableNatinfs={availableNatinfs}
          onChange={onChange}
          onCancel={onCancel}
        />
      </Stack.Item>
    </Stack>
  )
}

export default EnvInfractionNewTargetForm
