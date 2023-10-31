import React from 'react'
import styled from 'styled-components'
import { FlexboxGrid, Stack, Toggle } from 'rsuite'
import {
  Accent,
  Icon,
  Button,
  Size,
  THEME,
  Label,
  MultiSelect,
  Textarea,
  Select,
  TextInput
} from '@mtes-mct/monitor-ui'
import Title from '../../../ui/title'
import { VESSEL_SIZE_OPTIONS } from '../../mission-types'
import InfractionForm from './infraction-form'

interface EnvInfractionNewTargetFormProps {
  data?: any
  availableNatinfs?: string[]
  onSubmit?: (data: any) => void
  onCancel?: () => void
}

const EnvInfractionNewTargetForm: React.FC<EnvInfractionNewTargetFormProps> = ({
  data,
  availableNatinfs,
  onSubmit,
  onCancel
}) => {
  const saveAndQuitMission = () => {
    // TODO add save
  }
  const finishMission = () => {}

  const onChange = (field: string, value: any) => {}

  return (
    <Stack direction="column" spacing={'2rem'} style={{ width: '100%' }}>
      <Stack.Item style={{ width: '100%' }}>
        <Select
          label="Type de contrôle avec infraction"
          options={VESSEL_SIZE_OPTIONS}
          value={undefined}
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
                  value={undefined}
                  name="vesselSize"
                  onChange={(nextValue: OptionValue) => onChange('vesselSize', nextValue)}
                />
              </Stack.Item>
              <Stack.Item style={{ width: '60%' }}>
                <Select
                  label="Type de navire"
                  options={VESSEL_SIZE_OPTIONS}
                  value={undefined}
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
                  value={undefined}
                  name="vesselIdentifier"
                  onChange={(nextValue?: string) => onChange('vesselIdentifier', nextValue)}
                />
              </Stack.Item>
              <Stack.Item style={{ width: '60%' }}>
                <TextInput
                  label="Identité de la personne contrôlée"
                  value={undefined}
                  name="identityControlledPerson"
                  onChange={(nextValue?: string) => onChange('identityControlledPerson', nextValue)}
                />
              </Stack.Item>
            </Stack>
          </Stack.Item>
        </Stack>
      </Stack.Item>
      <Stack.Item style={{ width: '100%' }}>
        <InfractionForm />
      </Stack.Item>
    </Stack>
  )
}

export default EnvInfractionNewTargetForm
