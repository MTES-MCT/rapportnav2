import { ControlType } from '@common/types/control-types'
import { VehicleTypeEnum } from '@common/types/env-mission-types'
import { THEME } from '@mtes-mct/monitor-ui'
import { FieldProps } from 'formik'
import { FC } from 'react'
import { Target, TargetType } from '../../../common/types/target-types'
import MissionInfractionEnvForm from '../../../mission-infraction/components/elements/mission-infraction-env-form'
import { TargetInfraction } from '../../../mission-infraction/hooks/use-infraction-env-form'
import { useTarget } from '../../hooks/use-target'

export interface MissionTargetEnvFormProps {
  name: string
  value: TargetInfraction
  onClose?: () => void
  editTarget?: boolean
  editInfraction?: boolean
  vehicleType?: VehicleTypeEnum
  fieldFormik: FieldProps<Target>
  availableControlTypes?: ControlType[]
  targetType: TargetType
}

const MissionTargetEnvForm: FC<MissionTargetEnvFormProps> = ({
  name,
  value,
  onClose,
  vehicleType,
  fieldFormik,
  targetType,
  editTarget,
  editInfraction,
  availableControlTypes
}) => {
  const { fromInputToFieldValue } = useTarget()

  const handleClose = () => {
    if (onClose) onClose()
  }

  const handleSubmit = async (value?: TargetInfraction) => {
    if (!value) return
    const valueToSubmit = fromInputToFieldValue(value, fieldFormik.field.value)
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
      <MissionInfractionEnvForm
        value={value}
        onClose={handleClose}
        onSubmit={handleSubmit}
        targetType={targetType}
        editTarget={editTarget}
        vehicleType={vehicleType}
        editInfraction={editInfraction}
        availableControlTypes={availableControlTypes}
      />
    </div>
  )
}

export default MissionTargetEnvForm
