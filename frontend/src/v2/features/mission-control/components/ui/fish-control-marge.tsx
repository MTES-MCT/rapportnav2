import { Dialog } from '@common/components/ui/custom-dialog.tsx'
import Text from '@common/components/ui/text.tsx'
import { Marge, MetricType, SpeciesControl } from '@common/types/fish-mission-types.ts'
import { Icon, Label, Message, NumberInput, Option, THEME } from '@mtes-mct/monitor-ui'
import { Dispatch, SetStateAction } from 'react'
import { Divider, Stack } from 'rsuite'
import BasicDialogAction from '../../../common/components/ui/basic-dialog-action.tsx'
import BasicDialogTitle from '../../../common/components/ui/basic-dialog-title.tsx'
import { SelectInput } from '../../../common/components/ui/formik-select-input.tsx'

const METRIC_TYPE_OPTIONS: Option<MetricType>[] = [
  { label: 'Kg', value: MetricType.KG },
  { label: '%', value: MetricType.PERCENT }
]

type FishControlMargeProps = {
  onClose: () => void
  currentMarge?: Marge
  species: SpeciesControl
  onChangeMarge: Dispatch<SetStateAction<Marge | undefined>>
  onSubmit: (response: boolean, data?: Marge) => void
}

const FishControlMarge: React.FC<FishControlMargeProps> = ({
  species,
  currentMarge,
  onChangeMarge,
  onClose,
  onSubmit
}) => {
  return (
    <Dialog>
      <Dialog.Title>
        <BasicDialogTitle
          onClose={onClose}
          label={`Mise à jour marge (${species.speciesCode} – ${species.speciesName})`}
        />
      </Dialog.Title>
      <Dialog.Body style={{ padding: '24px 24px 0px 24px' }}>
        <Stack direction="column" spacing=".5rem" alignItems="flex-start">
          <Stack.Item style={{ width: '100%' }}>
            <Stack direction="row" spacing=".2rem" style={{ width: '100%' }} alignItems="flex-end">
              <Stack.Item>
                <div style={{ display: 'flex', flexDirection: 'row' }}>
                  <Label style={{ marginRight: '.2rem' }}>Quantité déclarée : </Label>
                  <Text as="h3" weight="normal">
                    {`${species.declaredWeight} kg`}
                  </Text>
                </div>
              </Stack.Item>
              <Divider style={{ backgroundColor: THEME.color.charcoal }} vertical />
              <Stack.Item>
                <div style={{ display: 'flex', flexDirection: 'row' }}>
                  <Label style={{ marginRight: '.2rem' }}>Quantité pesée : </Label>
                  <Text as="h3" weight="normal">
                    {`${species.controlledWeight ?? '-'} kg`}
                  </Text>
                </div>
              </Stack.Item>
            </Stack>
          </Stack.Item>
          <Stack.Item style={{ width: '100%' }}>
            <Message level="WARNING" Icon={Icon.AttentionFilled}>
              <span style={{ fontWeight: 'bold' }}>
                Ce qui est à renseigner ici est
                <span style={{ textDecoration: 'underline' }}>la marge de tolérance en % ou en kg</span>
                <br />
                (en fonction dela réglementation applicable),
                <span style={{ textDecoration: 'underline' }}>si celle-ci est non conforme </span>
              </span>
              <span style={{ fontStyle: 'italic' }}>
                (dépassant la marge autorisée entre les estimations consignées dans le journal <br />
                de pêche et les quantités pesées débarquées)
              </span>
            </Message>
          </Stack.Item>
          <Stack.Item style={{ width: '60%' }}>
            <Stack direction="row" spacing=".2rem" style={{ width: '100%' }} justifyContent="flex-start">
              <Stack.Item style={{ width: '70%' }}>
                <NumberInput
                  name="value"
                  label="Marge"
                  isLight={false}
                  value={currentMarge?.value}
                  onChange={value => onChangeMarge(prev => ({ ...prev, value: value ? Number(value) : undefined }))}
                />
              </Stack.Item>
              <Stack.Item style={{ width: '30%' }}>
                <SelectInput
                  label="Unité"
                  name="metric"
                  isLight={false}
                  value={currentMarge?.metric}
                  options={METRIC_TYPE_OPTIONS}
                  onChange={metric => onChangeMarge(prev => ({ ...prev, metric: metric as MetricType | undefined }))}
                />
              </Stack.Item>
            </Stack>
          </Stack.Item>
        </Stack>
      </Dialog.Body>
      <Dialog.Action style={{ display: 'flex', justifyContent: 'flex-end', padding: '32px 24px 24px 24px' }}>
        <BasicDialogAction
          isValid={!!currentMarge?.metric && !!currentMarge?.value}
          onSubmit={response => onSubmit(response, response ? currentMarge : undefined)}
        />
      </Dialog.Action>
    </Dialog>
  )
}

export default FishControlMarge
