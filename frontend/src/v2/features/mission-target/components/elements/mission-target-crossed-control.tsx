import { ControlType } from '@common/types/control-types'
import { Field, FieldArrayRenderProps, FieldProps } from 'formik'
import { Stack } from 'rsuite'
import { Target } from '../../../common/types/target-types'
import MissionTargetCrossControlForm from './mission-target-crossed-control-form'
import MissionTargetEnvInfractionList from './mission-target-env-infraction-list'

export interface MissionTargetCrossControlProps {
  isDisabled?: boolean
  fieldArray: FieldArrayRenderProps
  availableControlTypes?: ControlType[]
}

const MissionTargetCrossControl: React.FC<MissionTargetCrossControlProps> = ({
  fieldArray,
  isDisabled,
  availableControlTypes
}) => {
  return (
    <Stack direction="column" alignItems="flex-start" style={{ width: '100%' }}>
      <Stack.Item style={{ width: '100%' }}>
        {fieldArray.form.values.targets.map((target: Target, targetIndex: number) => (
          <Field name={`targets[${targetIndex}]`} key={`targets-form-[${targetIndex}]`}>
            {(fieldFormik: FieldProps<Target>) => (
              <MissionTargetCrossControlForm
                isDisabled={isDisabled}
                fieldFormik={fieldFormik}
                name={`targets[${targetIndex}]`}
                availableControlTypes={availableControlTypes}
              />
            )}
          </Field>
        ))}
      </Stack.Item>

      <Stack.Item style={{ width: '100%' }}>
        {fieldArray.form.values.targets.map((target: Target, targetIndex: number) => (
          <Field name={`targets[${targetIndex}]`} key={`targets-item-[${targetIndex}]`}>
            {(fieldFormik: FieldProps<Target>) => (
              <MissionTargetEnvInfractionList
                fieldFormik={fieldFormik}
                targetType={target.targetType}
                name={`targets[${targetIndex}]`}
                availableControlTypes={availableControlTypes}
              />
            )}
          </Field>
        ))}
      </Stack.Item>
    </Stack>
  )
}

export default MissionTargetCrossControl
