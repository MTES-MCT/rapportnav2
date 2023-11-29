import React from 'react'
import { Stack } from 'rsuite'
import { Legend, Select, Field, Checkbox, THEME } from '@mtes-mct/monitor-ui'
import { ControlType } from '../../../types/control-types'
import { Infraction } from '../../../types/infraction-types'
import InfractionForm from './infraction-form'
import {
  controlTitle,
  getDisabledControlTypes,
  vesselSizeToHumanString,
  vesselTypeToHumanString
} from '../controls/utils'
import Text from '../../../ui/text'
import { infractionTypeLabels } from '../../../types/env-mission-types'

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
    <Stack direction="column" spacing={'2rem'} style={{ width: '100%', padding: '1rem' }}>
      <Stack.Item style={{ width: '100%' }}>
        <Stack direction="row" spacing={'2rem'} style={{ width: '100%' }}>
          <Stack.Item style={{ width: '100%' }}>
            <Legend>Immatriculation</Legend>
            <Text as="h3" weight="medium">
              {infraction?.target?.vesselIdentifier}
            </Text>
          </Stack.Item>
          <Stack.Item style={{ width: '100%' }}>
            <Legend>Type de navire</Legend>
            <Text as="h3" weight="medium">
              {vesselTypeToHumanString(infraction?.target?.vesselType)}
            </Text>
          </Stack.Item>
          <Stack.Item style={{ width: '100%' }}>
            <Legend>Taille du navire</Legend>
            <Text as="h3" weight="medium">
              {vesselSizeToHumanString(infraction?.target?.vesselSize)}
            </Text>
          </Stack.Item>
        </Stack>
      </Stack.Item>
      <Stack.Item style={{ width: '100%' }}>
        <Legend>Identité de la personne contrôlée</Legend>
        <Text as="h3" weight="medium">
          {infraction?.target?.identityControlledPerson}
        </Text>
      </Stack.Item>
      <Stack.Item style={{ width: '100%' }}>
        <Legend>Type d'infraction</Legend>
        <Text as="h3" weight="medium">
          {infractionTypeLabels[infraction.target.infractionType!!].libelle}
        </Text>
      </Stack.Item>
      <Stack.Item style={{ width: '100%' }}>
        <Legend>Mise en demeure</Legend>
        {/* TODO  */}
        <Text as="h3" weight="medium">
          -
        </Text>
      </Stack.Item>
      <Stack.Item style={{ width: '100%' }}>
        <Legend>NATINF</Legend>
        <Text as="h3" weight="medium">
          {'TODO'}
        </Text>
      </Stack.Item>
      <Stack.Item style={{ width: '100%' }}>
        <Stack direction="row" spacing={'2rem'}>
          <Stack.Item>
            <Legend>Tribunal compétent</Legend>
            <Text as="h3" weight="medium">
              {infraction?.target?.relevantCourt}
            </Text>
          </Stack.Item>
          <Stack.Item>
            <Legend>&nbsp;</Legend>
            <Checkbox value={!!infraction?.target?.toProcess} label="À traiter" />
          </Stack.Item>
        </Stack>
      </Stack.Item>
      <Stack.Item style={{ width: '100%' }}>
        <Legend>Observations</Legend>
        <Text as="h3" weight="medium">
          {infraction?.target?.observations ?? '-'}
        </Text>
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
