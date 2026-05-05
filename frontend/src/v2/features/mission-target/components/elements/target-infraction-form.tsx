import { ControlType } from '@common/types/control-types'
import { THEME } from '@mtes-mct/monitor-ui'
import { FC, useState } from 'react'
import { Stack } from 'rsuite'
import { TargetInfraction, TargetType } from '../../../common/types/target-types'
import MissionInfractionForm from '../../../mission-infraction/components/elements/mission-infraction-form'
import InfractionNavSummary from '../../../mission-infraction/components/ui/infraction-nav-summary'

export interface TargetInfractionFormProps {
  index: number
  value: TargetInfraction
  targetType?: TargetType
  onDelete: (index: number) => void
  availableControlTypes?: ControlType[]
  onSubmit: (value?: TargetInfraction) => Promise<unknown>
}

const TargetInfractionForm: FC<TargetInfractionFormProps> = ({
  index,
  value,
  onDelete,
  onSubmit,
  targetType,
  availableControlTypes
}) => {
  const [showForm, setShowForm] = useState(false)

  const handleSubmit = async (value?: TargetInfraction) => {
    onSubmit(value)
    setShowForm(false)
  }

  return (
    <Stack
      direction="column"
      spacing={'0.5rem'}
      style={{ width: '100%', marginBottom: '.2em', backgroundColor: THEME.color.cultured }}
      key={`infraction-${index}`}
      data-testid="target-infraction-form"
    >
      {!showForm && (
        <Stack.Item
          style={{
            width: '100%'
          }}
        >
          <InfractionNavSummary
            index={index}
            onDelete={() => onDelete}
            onEdit={() => setShowForm(true)}
            infraction={value?.infraction}
            controlType={value?.control?.controlType}
          />
        </Stack.Item>
      )}
      {showForm && (
        <Stack.Item
          style={{
            width: '100%',
            padding: '1rem',
            backgroundColor: THEME.color.cultured
          }}
        >
          <MissionInfractionForm
            value={value}
            editTarget={false}
            editControl={true}
            editInfraction={true}
            onSubmit={handleSubmit}
            targetType={targetType}
            onClose={() => setShowForm(false)}
            availableControlTypes={availableControlTypes}
          />
        </Stack.Item>
      )}
    </Stack>
  )
}

export default TargetInfractionForm
