import { ControlType } from '@common/types/control-types'
import { Field, FieldArrayRenderProps, FieldProps } from 'formik'
import { Stack } from 'rsuite'
import { Target } from '../../../common/types/target-types'
import { useTarget } from '../../hooks/use-target'
import MissionTargetEnvInfractionList from './mission-target-env-infraction-list'
import MissionTargetInquiryForm from './mission-target-inquiry-form'

export interface MissionTargetInquiryProps {
  isDisabled?: boolean
  fieldArray: FieldArrayRenderProps
  availableControlTypes?: ControlType[]
}

const MissionTargetInquiry: React.FC<MissionTargetInquiryProps> = ({
  fieldArray,
  isDisabled,
  availableControlTypes
}) => {
  const { computeControlTypeOnTarget } = useTarget()
  return (
    <Stack direction="column" alignItems="flex-start" style={{ width: '100%' }}>
      <Stack.Item style={{ width: '100%', marginBottom: 12 }}>
        {fieldArray.form.values.targets.map((target: Target, targetIndex: number) => (
          <Field name={`targets[${targetIndex}]`} key={`targets-form-[${targetIndex}]`}>
            {(fieldFormik: FieldProps<Target>) => (
              <MissionTargetInquiryForm
                isDisabled={isDisabled}
                fieldFormik={fieldFormik}
                name={`targets[${targetIndex}]`}
                availableControlTypes={computeControlTypeOnTarget(availableControlTypes, target ? [target] : [])}
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
                noDivider={true}
                fieldFormik={fieldFormik}
                targetType={target.targetType}
                name={`targets[${targetIndex}]`}
                availableControlTypes={[]}
              />
            )}
          </Field>
        ))}
      </Stack.Item>
    </Stack>
  )
}

export default MissionTargetInquiry
