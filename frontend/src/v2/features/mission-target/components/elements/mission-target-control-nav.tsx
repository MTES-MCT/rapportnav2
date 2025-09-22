import { ControlType } from '@common/types/control-types'
import { Label } from '@mtes-mct/monitor-ui'
import { Field, FieldArray, FieldArrayRenderProps, FieldProps } from 'formik'
import { FC } from 'react'
import { Stack } from 'rsuite'
import { Control, Target } from '../../../common/types/target-types'
import MissionControlForm from '../../../mission-control/components/elements/mission-control-form'

type MissionTargetControlNavProps = {
  name: string
  label: string
  hideGensDeMer?: boolean
  fieldArray: FieldArrayRenderProps
  controlsToComplete?: ControlType[]
}

const MissionTargetControlNav: FC<MissionTargetControlNavProps> = ({
  label,
  name,
  fieldArray,
  hideGensDeMer,
  controlsToComplete
}) => {
  return (
    <Stack direction="column" spacing="0.5rem" style={{ width: '100%' }}>
      <Stack.Item style={{ width: '100%' }}>
        <Label>{label}</Label>
      </Stack.Item>
      <Stack.Item style={{ width: '100%' }}>
        {(fieldArray.form.values.targets || []).map((target: Target, targetIndex: number) => (
          <Stack direction="column" alignItems="flex-start" style={{ width: '100%' }} key={`${name}-${targetIndex}`}>
            <Stack.Item style={{ width: '100%' }}>
              <FieldArray name={`${name}.controls`}>
                {(fieldArray2: FieldArrayRenderProps) => (
                  <Stack direction="column" alignItems="flex-start" spacing={'.5rem'} style={{ width: '100%' }}>
                    {fieldArray2.form.values.targets[targetIndex].controls?.map(
                      (control: Control, controlIndex: number) => (
                        <Stack.Item
                          style={{ width: '100%', marginBottom: '.2em' }}
                          key={`${name}-${targetIndex}-${controlIndex}`}
                        >
                          {(!hideGensDeMer || control.controlType !== ControlType.GENS_DE_MER) && (
                            <Field name={`targets[${targetIndex}].controls[${controlIndex}]`}>
                              {(field: FieldProps<Control>) => (
                                <MissionControlForm
                                  fieldFormik={field}
                                  controlType={control.controlType}
                                  name={`targets[${targetIndex}].controls[${controlIndex}]`}
                                  isToComplete={controlsToComplete?.includes(control.controlType)}
                                />
                              )}
                            </Field>
                          )}
                        </Stack.Item>
                      )
                    )}
                  </Stack>
                )}
              </FieldArray>
            </Stack.Item>
          </Stack>
        ))}
      </Stack.Item>
    </Stack>
  )
}

export default MissionTargetControlNav
