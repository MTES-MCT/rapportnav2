import { Dialog } from '@common/components/ui/custom-dialog.tsx'
import { Marge, MetricType, SpeciesControl } from '@common/types/fish-mission-types.ts'
import { Option } from '@mtes-mct/monitor-ui'
import { Dispatch, SetStateAction } from 'react'
import { Stack } from 'rsuite'
import BasicDialogAction from '../../../common/components/ui/basic-dialog-action.tsx'
import BasicDialogTitle from '../../../common/components/ui/basic-dialog-title.tsx'
import { SelectInput } from '../../../common/components/ui/formik-select-input.tsx'
import { StyledTextInput } from '../../../common/components/ui/formik-text-input.tsx'

const METRIC_TYPE_OPTIONS: Option<MetricType>[] = [
  { label: 'Kg', value: MetricType.KG },
  { label: '%', value: MetricType.PERCENT }
]

type FishControlMargeProps = {
  species: SpeciesControl
  currentMarge?: Marge
  onChangeMarge: Dispatch<SetStateAction<Marge | undefined>>
  onClose: () => void
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
        <Stack direction="row" spacing=".2rem" style={{ width: '100%' }}>
          <Stack.Item style={{ width: '50%' }}>
            <StyledTextInput
              name="value"
              label="Marge"
              isLight={false}
              value={currentMarge?.value?.toString()}
              onChange={value => onChangeMarge(prev => ({ ...prev, value: value ? Number(value) : undefined }))}
            />
          </Stack.Item>
          <Stack.Item style={{ width: '50%' }}>
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
      </Dialog.Body>
      <Dialog.Action style={{ display: 'flex', justifyContent: 'flex-end', padding: '32px 24px 24px 24px' }}>
        <BasicDialogAction
          isValid={false}
          onSubmit={response => onSubmit(response, response ? currentMarge : undefined)}
        />
      </Dialog.Action>
    </Dialog>
  )
}

export default FishControlMarge
