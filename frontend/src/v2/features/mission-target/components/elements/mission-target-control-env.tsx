import { ControlType } from '@common/types/control-types'
import { Field, FieldArray, FieldArrayRenderProps, FieldProps } from 'formik'
import { Stack } from 'rsuite'
import { Control, Target } from '../../../common/types/target-types'
import MissionControlEnvForm from '../../../mission-control/components/elements/mission-control-env-form'
import { useTarget } from '../../hooks/use-target'

export interface MissionTargetControlEnvProps {
  name: string
  actionNumberOfControls: number
  fieldArray: FieldArrayRenderProps
  controlsToComplete: ControlType[]
}

const MissionTargetControlEnv: React.FC<MissionTargetControlEnvProps> = ({
  name,
  fieldArray,
  controlsToComplete,
  actionNumberOfControls
}) => {
  const { isDefaultTarget } = useTarget()

  return (
    <Stack direction="column" alignItems="flex-start" spacing={'1.5rem'} style={{ width: '100%' }}>
      {(fieldArray.form.values.targets || []).map((target: Target, targetIndex: number) => (
        <Stack.Item style={{ width: '100%' }} key={`${name}-${targetIndex}`}>
          {isDefaultTarget(target) && (
            <Stack direction="column" alignItems="flex-start" spacing={'1.5rem'} style={{ width: '100%' }}>
              <FieldArray name={`${name}.controls`}>
                {(fieldArray2: FieldArrayRenderProps) => (
                  <>
                    {fieldArray2.form.values.targets[targetIndex].controls?.map(
                      (control: Control, controlIndex: number) => (
                        <Stack.Item
                          style={{ width: '100%', marginBottom: '.2em' }}
                          key={`${name}-${targetIndex}-${controlIndex}`}
                        >
                          <Field name={`targets[${targetIndex}].controls[${controlIndex}]`}>
                            {(field: FieldProps<Control>) => (
                              <MissionControlEnvForm
                                fieldFormik={field}
                                controlType={control.controlType}
                                maxAmountOfControls={actionNumberOfControls}
                                name={`targets[${targetIndex}].controls[${controlIndex}]`}
                                isToComplete={controlsToComplete?.includes(control.controlType)}
                              />
                            )}
                          </Field>
                        </Stack.Item>
                      )
                    )}
                  </>
                )}
              </FieldArray>
            </Stack>
          )}
        </Stack.Item>
      ))}
    </Stack>
  )
}

export default MissionTargetControlEnv
