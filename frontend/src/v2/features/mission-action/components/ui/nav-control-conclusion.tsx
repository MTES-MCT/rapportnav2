import { Label } from '@mtes-mct/monitor-ui'
import { Field, FieldProps } from 'formik'
import { FC } from 'react'
import { Stack } from 'rsuite'
import { FormikTextAreaInput } from '../../../common/components/ui/formik-textarea-input.tsx'
import { Target, TargetType } from '../../../common/types/target-types.ts'
import MissionTargetItemDefault from '../../../mission-target/components/elements/target-item-default.tsx'
import { useTarget } from '../../../mission-target/hooks/use-target.tsx'
import { ActionNavControlInput } from '../../types/action-type.ts'
import MissionActionDivingOperation from './mission-action-diving-operation.tsx'
import MissionActionIncidentDonwload from './mission-action-incident-download.tsx'

const NavControlConclusion: FC<{
  values: ActionNavControlInput
}> = ({ values }) => {
  const { allControlTypes } = useTarget()
  return (
    <Stack direction="column" spacing="0.5rem" alignItems="flex-start" style={{ width: '100%' }}>
      <Stack.Item style={{ width: '100%' }}>
        <Label>Conclusions</Label>
      </Stack.Item>
      <Stack.Item style={{ width: '100%' }}>
        <Stack direction="column" spacing="1rem" style={{ width: '100%', backgroundColor: 'white', padding: 16 }}>
          <Stack.Item style={{ width: '100%' }}>
            <Field name={`targets[0]`}>
              {(fieldFormik: FieldProps<Target>) => (
                <MissionTargetItemDefault
                  name={`targets[0]`}
                  fieldFormik={fieldFormik}
                  targetType={TargetType.DEFAULT}
                  availableControlTypes={allControlTypes}
                />
              )}
            </Field>
          </Stack.Item>
          <Stack.Item style={{ width: '100%' }}>
            <FormikTextAreaInput
              isLight={false}
              name="observations"
              data-testid="observations"
              label="Observations générales sur le contrôle"
            />
            <MissionActionIncidentDonwload />
          </Stack.Item>
          <Stack.Item style={{ width: '100%' }}>
            <MissionActionDivingOperation />
          </Stack.Item>
        </Stack>
      </Stack.Item>
    </Stack>
  )
}

export default NavControlConclusion
