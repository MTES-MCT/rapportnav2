import { ControlType } from '@common/types/control-types'
import { THEME } from '@mtes-mct/monitor-ui'
import { FieldProps } from 'formik'
import { FC } from 'react'
import { Stack } from 'rsuite'
import { Control, Infraction, Target, TargetType } from '../../../common/types/target-types'
import { TargetInfraction } from '../../../mission-infraction/hooks/use-infraction-env-form'
import { useTarget } from '../../hooks/use-target'
import MissionTargetEnvInfractionForm from './mission-target-env-infraction-form'

export interface MissionTargetEnvInfractionListProps {
  name: string
  fieldFormik: FieldProps<Target>
  availableControlTypes?: ControlType[]
  targetType?: TargetType
}

const MissionTargetEnvInfractionList: FC<MissionTargetEnvInfractionListProps> = ({
  name,
  targetType,
  fieldFormik,
  availableControlTypes
}) => {
  const { deleteInfraction, fromInputToFieldValue } = useTarget()

  const handleSubmit = async (value?: TargetInfraction) => {
    if (!value) return
    const valueToSubmit = fromInputToFieldValue(value, fieldFormik.field.value)
    await fieldFormik.form.setFieldValue(name, valueToSubmit)
  }

  const handleDelete = async (controlType: ControlType, infractionIndex: number) => {
    const valueToSubmit = deleteInfraction(controlType, infractionIndex, fieldFormik.field.value)
    await fieldFormik.form.setFieldValue(name, valueToSubmit)
  }

  return (
    <div
      style={{
        width: '100%',
        marginBottom: '1rem',
        backgroundColor: THEME.color.white
      }}
    >
      {fieldFormik.field.value?.controls?.map((control: Control, controlIndex: number) => (
        <Stack.Item style={{ width: '100%' }} key={`${name}-${controlIndex}`}>
          {control.infractions?.map((infraction: Infraction, infractionIndex: number) => (
            <Stack
              direction="column"
              spacing={'0.5em'}
              style={{ width: '100%', marginBottom: '.2em' }}
              key={`${name}-${controlIndex}-${infractionIndex}`}
            >
              <Stack.Item style={{ width: '100%' }}>
                <MissionTargetEnvInfractionForm
                  index={infractionIndex}
                  targetType={targetType}
                  onSubmit={value => handleSubmit(value)}
                  availableControlTypes={availableControlTypes}
                  onDelete={() => handleDelete(control.controlType, infractionIndex)}
                  value={{ target: fieldFormik.field.value, control, infraction }}
                  key={`${name}.controls[${controlIndex}].infractions[${infractionIndex}]`}
                />
              </Stack.Item>
            </Stack>
          ))}
        </Stack.Item>
      ))}
    </div>
  )
}

export default MissionTargetEnvInfractionList
