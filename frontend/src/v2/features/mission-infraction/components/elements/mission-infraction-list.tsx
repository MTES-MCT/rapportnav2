import { ControlType } from '@common/types/control-types'
import { Field, FieldArrayRenderProps, FieldProps } from 'formik'
import { Infraction } from '../../../common/types/target-types'
import MissionInfractionFormNew from './mission-infraction-form-new'
import MissionInfractionItem from './mission-infraction-item'

const DEFAULT_MAX = 1
export interface MissionInfractionListProps {
  name: string
  max?: number
  controlType: ControlType
  fieldArray: FieldArrayRenderProps
}

const MissionInfractionList: React.FC<MissionInfractionListProps> = ({ max, name, controlType, fieldArray }) => {
  const handleRemove = (index: number) => {
    fieldArray.remove(index)
  }

  const handleAdd = (infraction: Infraction) => {
    fieldArray.push(infraction)
  }

  return (
    <div>
      {fieldArray.form.values.infractions?.map((infraction: Infraction, index: number) => (
        <Field name={`${name}.${index}`}>
          {(field: FieldProps<Infraction>) => (
            <MissionInfractionItem
              index={index}
              fieldFormik={field}
              name={`${name}.${index}`}
              controlType={controlType}
              handleRemove={handleRemove}
            />
          )}
        </Field>
      ))}
      <MissionInfractionFormNew
        onSubmit={handleAdd}
        controlType={controlType}
        isDisabled={fieldArray.form.values?.infractions?.length >= (max ?? DEFAULT_MAX)}
      />
    </div>
  )
}

export default MissionInfractionList
