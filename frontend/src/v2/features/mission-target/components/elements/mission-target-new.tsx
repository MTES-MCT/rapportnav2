import { ControlType } from '@common/types/control-types'
import { ActionTargetTypeEnum, MissionSourceEnum, VehicleTypeEnum } from '@common/types/env-mission-types'
import { UTCDate } from '@date-fns/utc'
import { Accent, Button, Icon, Size, THEME } from '@mtes-mct/monitor-ui'
import { FieldArrayRenderProps } from 'formik'
import { useState } from 'react'
import { Stack } from 'rsuite'
import { TargetInfraction } from '../../../common/types/target-types'
import MissionInfractionForm from '../../../mission-infraction/components/elements/mission-infraction-form'
import { useTarget } from '../../hooks/use-target'

export interface MissionTargetNewProps {
  actionId?: string
  isDisabled?: boolean
  vehicleType?: VehicleTypeEnum
  fieldArray: FieldArrayRenderProps
  actionTargetType?: ActionTargetTypeEnum
  availableControlTypes?: ControlType[]
}

const MissionTargetNew: React.FC<MissionTargetNewProps> = ({
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
      source: MissionSourceEnum.RAPPORT_NAV,
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
          <MissionInfractionForm
            editTarget={true}
            editControl={true}
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
        <Stack style={{ marginBottom: '.7em' }}>
          <Stack.Item style={{ width: '100%' }}>
            <Button
              size={Size.NORMAL}
              Icon={Icon.Plus}
              onClick={handleShow}
              accent={Accent.SECONDARY}
              role={'target-new-button'}
              data-testid={'target-new-button'}
              style={{ width: 'inherit' }}
              disabled={isDisabled || !availableControlTypes?.length}
            >
              Ajouter une infraction
            </Button>
          </Stack.Item>
        </Stack>
      )}
    </div>
  )
}

export default MissionTargetNew
