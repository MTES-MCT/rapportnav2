import { ControlType } from '@common/types/control-types'
import { VehicleTypeEnum } from '@common/types/env-mission-types'
import { THEME } from '@mtes-mct/monitor-ui'
import { FieldProps } from 'formik'
import { FC } from 'react'
import { Target, TargetInfraction, TargetType } from '../../../common/types/target-types'
import MissionInfractionForm from '../../../mission-infraction/components/elements/mission-infraction-form'
import { useTarget } from '../../hooks/use-target'

export interface MissionTargetFormProps {
  name: string
  value: TargetInfraction
  onClose?: () => void
  editTarget?: boolean
  editInfraction?: boolean
  editControl: boolean
  vehicleType?: VehicleTypeEnum
  fieldFormik: FieldProps<Target>
  availableControlTypes?: ControlType[]
  targetType: TargetType
}

const MissionTargetForm: FC<MissionTargetFormProps> = ({
  name,
  value,
  onClose,
  vehicleType,
  fieldFormik,
  targetType,
  editTarget,
  editControl,
  editInfraction,
  availableControlTypes
}) => {
  const { fromInputToFieldValue } = useTarget()

  const handleClose = () => {
    if (onClose) onClose()
  }

  const handleSubmit = async (value?: TargetInfraction) => {
    if (!value) return
    const valueToSubmit = fromInputToFieldValue(value)
    await fieldFormik.form.setFieldValue(name, valueToSubmit)
    handleClose()
  }

  return (
    <div
      style={{
        width: '100%',
        padding: '1rem',
        marginBottom: '1rem',
        backgroundColor: THEME.color.white
      }}
    >
      <MissionInfractionForm
        value={value}
        onClose={handleClose}
        onSubmit={handleSubmit}
        targetType={targetType}
        editTarget={editTarget}
        editControl={editControl}
        vehicleType={vehicleType}
        editInfraction={editInfraction}
        availableControlTypes={availableControlTypes}
      />
    </div>
  )
}

export default MissionTargetForm
