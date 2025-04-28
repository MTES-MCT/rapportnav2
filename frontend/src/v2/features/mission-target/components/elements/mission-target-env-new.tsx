import { ControlType } from '@common/types/control-types'
import { ActionTargetTypeEnum, MissionSourceEnum, VehicleTypeEnum } from '@common/types/env-mission-types'
import { Icon, THEME } from '@mtes-mct/monitor-ui'
import { FieldArrayRenderProps } from 'formik'
import { useState } from 'react'
import WithIconButton from '../../../common/components/ui/with-icon-button'
import MissionInfractionEnvForm from '../../../mission-infraction/components/elements/mission-infraction-env-form'
import { TargetInfraction } from '../../../mission-infraction/hooks/use-infraction-env-form2'
import { useTarget } from '../../hooks/use-target'

export interface MissionTargetEnvNewProps {
  actionId?: string
  isDisabled?: boolean
  vehicleType?: VehicleTypeEnum
  fieldArray: FieldArrayRenderProps
  controlsToComplete: ControlType[]
  actionTargetType?: ActionTargetTypeEnum
  availableControlTypes?: ControlType[]
}

const MissionTargetEnvNew: React.FC<MissionTargetEnvNewProps> = ({
  actionId,
  fieldArray,
  isDisabled,
  vehicleType,
  actionTargetType,
  availableControlTypes
}) => {
  const [showForm, setShowForm] = useState<boolean>(false)
  const { getTargetType, fromInputToFieldValue } = useTarget()

  const handleShow = () => setShowForm(true)
  const handleClose = () => setShowForm(false)

  const handleSubmit = async (value?: TargetInfraction) => {
    if (!value) return
    const valueToSubmit = {
      ...fromInputToFieldValue(value),
      actionId,
      startDateTimeUtc: new Date(),
      source: MissionSourceEnum.RAPPORTNAV,
      targetType: getTargetType(actionTargetType)
    }
    fieldArray.push(valueToSubmit)
    handleClose()
  }

  return (
    <div>
      {showForm && (
        <div
          style={{
            width: '100%',
            padding: '1rem',
            marginBottom: '1rem',
            backgroundColor: THEME.color.white
          }}
        >
          <MissionInfractionEnvForm
            withTarget={true}
            onClose={handleClose}
            onSubmit={handleSubmit}
            vehicleType={vehicleType}
            value={{} as TargetInfraction}
            targetType={getTargetType(actionTargetType)}
            availableControlTypes={availableControlTypes}
          />
        </div>
      )}
      {!showForm && (
        <WithIconButton label={'Ajouter une infraction'} onClick={handleShow} disabled={isDisabled} icon={Icon.Plus} />
      )}
    </div>
  )
}

export default MissionTargetEnvNew
