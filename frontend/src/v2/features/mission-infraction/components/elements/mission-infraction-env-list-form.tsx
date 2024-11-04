import { ControlType } from '@common/types/control-types'
import { ActionTargetTypeEnum } from '@common/types/env-mission-types'
import { InfractionByTarget } from '@common/types/infraction-types'
import { FieldArrayRenderProps } from 'formik'
import MissionInfractionEnvFormNew from './mission-infraction-env-form-new'
import MissionInfractionEnvItem from './mission-infraction-env-item'

const DEFAULT_MAX = 10
export interface MissionInfractionEnvListProps {
  name: string
  max?: number
  fieldArray: FieldArrayRenderProps
  actionTargetType?: ActionTargetTypeEnum
  availableControlTypes?: ControlType[]
}

const MissionInfractionEnvList: React.FC<MissionInfractionEnvListProps> = ({
  max,
  name,
  fieldArray,
  actionTargetType,
  availableControlTypes
}) => {
  const handleRemove = (index: number) => {
    fieldArray.remove(index)
  }

  const handleAdd = (infraction: InfractionByTarget) => {
    fieldArray.push(infraction)
  }

  return (
    <div>
      <MissionInfractionEnvFormNew
        onSubmit={handleAdd}
        actionTargetType={actionTargetType}
        availableControlTypes={availableControlTypes}
        isDisabled={fieldArray.form.values.infractions.length >= (max ?? DEFAULT_MAX)}
      />
      {fieldArray.form.values.infractions?.map((byTarget: InfractionByTarget, index: number) => (
        <div key={index} style={{ marginBottom: '2em' }}>
          <MissionInfractionEnvItem
            name={name}
            index={index}
            byTarget={byTarget}
            actionTargetType={actionTargetType}
            onDelete={() => handleRemove(index)}
            availableControlTypes={availableControlTypes}
          />
        </div>
      ))}
    </div>
  )
}

export default MissionInfractionEnvList
