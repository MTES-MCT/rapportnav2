import { Label } from '@mtes-mct/monitor-ui'
import { Field, FieldProps } from 'formik'
import { FC } from 'react'
import { Stack } from 'rsuite'
import { FormikTextAreaInput } from '../../../common/components/ui/formik-textarea-input.tsx'
import { Target, TargetType } from '../../../common/types/target-types.ts'
import InfractionFishSummary from '../../../mission-infraction/components/ui/infraction-fish-summary.tsx'
import TargetItemDefault from '../../../mission-target/components/elements/target-item-default.tsx'
import { useTarget } from '../../../mission-target/hooks/use-target.tsx'
import { ActionFishControlInput } from '../../types/action-type.ts'
import MissionActionDivingOperation from './mission-action-diving-operation.tsx'
import MissionActionIncidentDonwload from './mission-action-incident-download.tsx'

const FishControlConclusion: FC<{
  values: ActionFishControlInput
}> = ({ values }) => {
  const hideCheckbox = true // TODO: remove when MonitorFish dev is done
  const { defaultControlTypes } = useTarget()
  return (
    <Stack direction="column" spacing="0.5rem" alignItems="flex-start" style={{ width: '100%' }}>
      <Stack.Item style={{ width: '100%' }}>
        <Label>Conclusions</Label>
      </Stack.Item>
      <Stack.Item style={{ width: '100%' }}>
        <Stack direction="column" spacing="1rem" style={{ width: '100%', backgroundColor: 'white', padding: 16 }}>
          <Stack.Item style={{ width: '100%' }}>
            <InfractionFishSummary infractions={values?.fishInfractions ?? []} />
          </Stack.Item>
          <Stack.Item style={{ width: '100%' }}>
            <Field name={`targets[0]`}>
              {(fieldFormik: FieldProps<Target>) => (
                <TargetItemDefault
                  name={`targets[0]`}
                  fieldFormik={fieldFormik}
                  targetType={TargetType.DEFAULT}
                  availableControlTypes={defaultControlTypes}
                  buttonLabel={`Ajouter une infraction (hors pol. pêche)`}
                />
              )}
            </Field>
          </Stack.Item>
          <Stack.Item style={{ width: '100%' }}>
            <FormikTextAreaInput
              isLight={false}
              name="observationsByUnit"
              data-testid="observations-by-unit"
              label="Observation de l'unité sur le contrôle"
            />
            {!hideCheckbox && <MissionActionIncidentDonwload />}
          </Stack.Item>
          <Stack.Item style={{ width: '100%' }}>{!hideCheckbox && <MissionActionDivingOperation />}</Stack.Item>
        </Stack>
      </Stack.Item>
    </Stack>
  )
}

export default FishControlConclusion
