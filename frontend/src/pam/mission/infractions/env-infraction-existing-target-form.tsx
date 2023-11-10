import React from 'react'
import { Stack } from 'rsuite'
import { Legend, Select, Field, Checkbox, THEME } from '@mtes-mct/monitor-ui'
import {
  ControlType,
  Infraction,
  InfractionEnvExistingTarget,
  InfractionEnvNewTarget,
  InfractionTarget,
  VESSEL_SIZE_OPTIONS,
  VESSEL_TYPE_OPTIONS
} from '../../mission-types'
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
  infraction?: Infraction
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
        <Stack direction="row" spacing={'2rem'} style={{ width: '100%' }}>
          <Stack.Item style={{ width: '100%' }}>
            <Legend>Immatriculation</Legend>
            <Field>{infraction?.target?.vesselIdentifier}</Field>
          </Stack.Item>
          <Stack.Item style={{ width: '100%' }}>
            <Legend>Type de navire</Legend>
            <Field>{infraction?.target?.vesselType}</Field>
          </Stack.Item>
          <Stack.Item style={{ width: '100%' }}>
            <Legend>Taille du navire</Legend>
            <Field>{infraction?.target?.vesselSize}</Field>
          </Stack.Item>
        </Stack>
      </Stack.Item>
      <Stack.Item style={{ width: '100%' }}>
        <Legend>Identité de la personne contrôlée</Legend>
        <Field>{infraction?.target?.identityControlledPerson}</Field>
      </Stack.Item>
      <Stack.Item style={{ width: '100%' }}>
        <Legend>Type d'infraction</Legend>
        <Field>{infraction?.target?.infractionType}</Field>
      </Stack.Item>
      <Stack.Item style={{ width: '100%' }}>
        <Legend>Mise en demeure</Legend>
        <Field>{'TODO'}</Field>
      </Stack.Item>
      <Stack.Item style={{ width: '100%' }}>
        <Legend>NATINF</Legend>
        <Field>{'TODO'}</Field>
      </Stack.Item>
      <Stack.Item style={{ width: '100%' }}>
        <Legend>Tribunal compétent</Legend>
        <Field>{infraction?.target?.relevantCourt}</Field>
        <Checkbox value={!!infraction?.target?.toProcess} label="À traiter" disabled={true} />
      </Stack.Item>
      <Stack.Item style={{ width: '100%' }}>
        <Legend>Observations</Legend>
        <Field>{infraction?.target?.observations ?? '-'}</Field>
      </Stack.Item>
      <Stack.Item style={{ width: '100%' }}>
        <Legend>Ajout d’une infraction pour cette cible</Legend>
        <Stack
          direction="column"
          spacing={'2rem'}
          style={{ width: '100%', backgroundColor: THEME.color.cultured, padding: '2rem' }}
        >
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
            <InfractionForm
              infraction={infraction}
              availableNatinfs={availableNatinfs}
              onChange={onChange}
              onCancel={onCancel}
            />
          </Stack.Item>
        </Stack>
      </Stack.Item>
    </Stack>
  )
}

export default EnvInfractionNewTargetForm
