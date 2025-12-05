import { ControlType } from '@common/types/control-types'
import { THEME } from '@mtes-mct/monitor-ui'
import { FieldProps } from 'formik'
import { FC } from 'react'
import { Divider, Stack } from 'rsuite'
import { Control, Infraction, Target, TargetInfraction, TargetType } from '../../../common/types/target-types'
import { useTarget } from '../../hooks/use-target'
import MissionTargetInfractionForm from './mission-target-infraction-form'

export interface MissionTargetInfractionListProps {
  name: string
  noDivider?: boolean
  targetType?: TargetType
  fieldFormik: FieldProps<Target>
  availableControlTypes?: ControlType[]
}

const MissionTargetInfractionList: FC<MissionTargetInfractionListProps> = ({
  name,
  noDivider,
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
                {!noDivider && (
                  <div data-testid={'target-infraction-list-divider'}>
                    <Divider style={{ margin: '12px 0' }} />
                  </div>
                )}
              </Stack.Item>
              <Stack.Item style={{ width: '100%' }}>
                <MissionTargetInfractionForm
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

export default MissionTargetInfractionList
