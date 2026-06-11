import { ControlType } from '@common/types/control-types'
import { THEME } from '@mtes-mct/monitor-ui'
import { FieldProps } from 'formik'
import { FC } from 'react'
import { Stack } from 'rsuite'
import { Control, Infraction, Target, TargetInfraction, TargetType } from '../../../common/types/target-types'
import { useTarget } from '../../hooks/use-target'
import TargetInfractionForm from './target-infraction-form'

export interface TargetInfractionListProps {
  name: string
  targetType?: TargetType
  fieldFormik: FieldProps<Target>
  availableControlTypes?: ControlType[]
}

const TargetInfractionList: FC<TargetInfractionListProps> = ({
  name,
  targetType,
  fieldFormik,
  availableControlTypes
}) => {
  const { deleteInfraction, fromInputToFieldValue } = useTarget()

  const handleSubmit = async (value?: TargetInfraction) => {
    if (!value) return
    const valueToSubmit = fromInputToFieldValue(value)
    await fieldFormik.form.setFieldValue(name, valueToSubmit)
  }

  const handleDelete = async (controlIndex: number, infractionIndex: number) => {
    const valueToSubmit = deleteInfraction(fieldFormik.field.value, controlIndex, infractionIndex)
    await fieldFormik.form.setFieldValue(name, valueToSubmit)
  }

  return (
    <div
      style={{
        width: '100%',
        marginTop: '1rem',
        marginBottom: '1rem'
      }}
    >
      {fieldFormik.field.value?.controls?.map((control: Control, controlIndex: number) => (
        <Stack.Item style={{ width: '100%' }} key={`${name}-${controlIndex}`}>
          {control.infractions?.map((infraction: Infraction, infractionIndex: number) => (
            <Stack
              direction="column"
              style={{ width: '100%', marginBottom: '.2em', backgroundColor: THEME.color.white }}
              key={`${name}-${controlIndex}-${infractionIndex}`}
            >
              <Stack.Item style={{ width: '100%' }}>
                <TargetInfractionForm
                  index={infractionIndex}
                  targetType={targetType}
                  onSubmit={value => handleSubmit(value)}
                  availableControlTypes={availableControlTypes}
                  onDelete={() => handleDelete(controlIndex, infractionIndex)}
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

export default TargetInfractionList
