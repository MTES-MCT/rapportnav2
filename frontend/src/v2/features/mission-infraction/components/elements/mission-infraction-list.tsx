import { ControlType } from '@common/types/control-types'
import { FieldArrayRenderProps } from 'formik'
import { Infraction } from '../../../common/types/target-types'
import MissionInfractionFormNew2 from './mission-infraction-form-new'
import MissionInfractionItem2 from './mission-infraction-item'

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
        <MissionInfractionItem2
          name={name}
          index={index}
          key={`${name}-index`}
          infraction={infraction}
          handleRemove={handleRemove}
        />
      ))}
      <MissionInfractionFormNew2
        onSubmit={handleAdd}
        controlType={controlType}
        isDisabled={fieldArray.form.values?.infractions?.length >= (max ?? DEFAULT_MAX)}
      />
    </div>
  )
}

export default MissionInfractionList
