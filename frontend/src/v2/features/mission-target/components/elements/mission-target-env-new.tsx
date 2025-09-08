import { ControlType } from '@common/types/control-types'
import { ActionTargetTypeEnum, MissionSourceEnum, VehicleTypeEnum } from '@common/types/env-mission-types'
import { UTCDate } from '@date-fns/utc'
import { Accent, Button, Icon, Size, THEME } from '@mtes-mct/monitor-ui'
import { FieldArrayRenderProps } from 'formik'
import { useState } from 'react'
import { Stack } from 'rsuite'
import MissionInfractionEnvForm from '../../../mission-infraction/components/elements/mission-infraction-env-form'
import { TargetInfraction } from '../../../mission-infraction/hooks/use-infraction-env-form'
import { useTarget } from '../../hooks/use-target'

export interface MissionTargetEnvNewProps {
  actionId?: string
  isDisabled?: boolean
  vehicleType?: VehicleTypeEnum
  fieldArray: FieldArrayRenderProps
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
      startDateTimeUtc: new UTCDate(),
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
            editTarget={true}
            editInfraction={true}
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
        <Stack style={{ marginBottom: '.7em' }} justifyContent="flex-end">
          <Stack.Item>
            <Button
              size={Size.NORMAL}
              Icon={Icon.Plus}
              onClick={handleShow}
              accent={Accent.SECONDARY}
              role={'target-env-new-button'}
              disabled={isDisabled || !availableControlTypes?.length}
            >
              {'Ajouter une infraction'}
            </Button>
          </Stack.Item>
        </Stack>
      )}
    </div>
  )
}

export default MissionTargetEnvNew
